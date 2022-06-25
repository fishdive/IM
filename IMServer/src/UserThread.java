import Model.Message;
import Model.MessageType;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * 客户端处理线程
 *
 * @author leilei
 * @version 1.0
 */
class UserThread implements Runnable {
    /**
     * 客户端的用户名称，唯一
     */
    private String name;
    private String id;
    private final Socket socket;

    public void setVector(Vector<UserThread> vector) {
        this.vector = vector;
    }

    /**
     * 客户端处理线程的集合
     */
    private Vector<UserThread> vector;
    /**
     * 输入流
     */
    private ObjectInputStream oIn;
    /**
     * 输出流
     */
    private ObjectOutputStream oOut;
    /**
     * 标记
     */
    private boolean flag = true;

    public UserThread(Socket socket, Vector<UserThread> vector) {
        this.socket = socket;
        this.vector = vector;
        //把当前线程也加入vector线程集合中
        vector.add(this);
    }

    @Override
    public void run() {
        try {
            //1、构造输入输出流
            System.out.println("客户端：" + socket.getInetAddress().getHostAddress() + "已连接！");
            oIn = new ObjectInputStream(socket.getInputStream());
            oOut = new ObjectOutputStream((socket.getOutputStream()));
            //2、循环读取
            while (flag) {
                //读取消息对象
                Message message = (Message) oIn.readObject();
                //获取消息类型，登录还是发送消息
                int type = message.getType();
                //3、判断
                switch (type) {
                    //如果是发送消息
                    case MessageType.TYPE_SEND:
                        sent(message);
                        break;
                    //如果是登录
                    case MessageType.TYPE_LOGIN:
                        login(message);
                        break;
                    //如果是注册
                    case MessageType.TYPE_REGISTER:
                        register(message);
                        break;
                    //如果是查找好友
                    case MessageType.TYPE_SELECT:
                        select(message);
                        break;
                    //如果是删除好友
                    case MessageType.TYPE_DELETE:
                        delete(message);
                        break;
                    //如果是添加好友
                    case MessageType.TYPE_INSERT:
                        insert(message);
                        break;
                    case MessageType.TYPE_OFF:
                        Server.cliceOff(this);
                        flag = true;
                    case MessageType.TYPE_MESSAGE:
                        readFile(message);
                        break;
                    default:
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息
     */
    private void sent(Message message) {
        boolean flag = false;
        //发送给谁
        String to = message.getTo();
        UserThread ut;
        try {
            //遍历vector，找到接收信息的客户端
            int size = vector.size();
            for (int i = 0; i < size; i++) {
                ut = vector.get(i);
                //如果名字相同，且不是自己，就把信息发给它
                if (to.equals(ut.id) && ut != this) {
                    ut.oOut.writeObject(message);
                    System.out.println(name + "发送给" + message.getTo() + "成功！");
                    //跳出循环
                    i = size;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //服务器聊天记录的保存
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //自己的聊天记录文件
            writeFile("src\\Info\\" + id + "\\" + message.getTo() + ".txt",
                    df.format(System.currentTimeMillis()) + "\n" + name + "(我)：" + message.getInfo() + "\n");
            //对方的聊天记录文件
            writeFile("src\\Info\\" + message.getTo() + "\\" + id + ".txt",
                    df.format(System.currentTimeMillis()) + "\n[" + message.getFrom() + "]对我说：" + message.getInfo() + "\n");
        }
    }

    /**
     * 客户端登录请求的处理
     */
    public void login(Message message) {
        boolean flag = false;
        String id = message.getUserId();
        String password = message.getUserPassword();
        String name = null;
        try {
            //检查登录id与密码是否正确是否正确
            ResultSet rs;
//            String sql = "SELECT id,name FROM users where id=? and password=?";
//            String sql = "SELECT id,name FROM users where id='" + id + "' and password='" + password + "'";
//            String sql = "SELECT id,name FROM users where id='" + id + "' and password=''or '1'='1'";
            rs = Mysql.login(id,password);

            // 展开结果集数据库
            if (rs.next()) {
                this.id = rs.getString("id");
                this.name = rs.getString("name");
                message.setUserName(this.name);
                flag = true;
                System.out.println("欢迎您：" + this.name);
            } else {
                message.setInfo("登录失败，请检查id与密码！");
            }
            // 完成后关闭
            rs.close();
            message.setFlag(flag);
            this.oOut.writeObject(message);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端注册请求的处理
     */
    public void register(Message message) {
        boolean flag = false;
        String id = message.getUserId();
        String password = message.getUserPassword();
        String name = message.getUserName();
        String sql;
        try {
            //验证注册ID合法性
            sql = "SELECT id FROM users where id='" + id + "'";
            if (!Mysql.isSelect(sql)) {
                //向数据库添加注册信息
                sql = "insert into users values('" + id + "','" + name + "','" + password + "')";
                if (Mysql.isUpdate(sql)) {
                    flag = true;
                } else {
                    message.setInfo("注册失败，请重试！");
                }
            } else {
                message.setInfo("ID已存在！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            message.setFlag(flag);
            try {
                this.oOut.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端查找好友的处理
     */
    public void select(Message message) {
        Vector<String> vector = new Vector<String>();
        String id = message.getUserId();
        String sql;
        try {
            sql = "select id,`name` from contacts where user_id='" + id + "'";
            ResultSet rs = Mysql.select(sql);
            while (rs.next()) {
                vector.add("账号：" + rs.getString("id") + "                 昵称：" + rs.getString("name"));
            }
            rs.close();
            this.oOut.writeObject(message);
            this.oOut.writeObject(vector);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端删除好友请求的处理
     */
    private void delete(Message message) {
        boolean flag = false;
        String id = message.getUserId();
        String sql;
        try {
            sql = "select * from contacts where user_id='" + this.id + "' and id='" + id + "'";
            if (Mysql.isSelect(sql)) {
                sql = "DELETE from contacts WHERE user_id='" + this.id + "' and id='" + id + "'";
                if (Mysql.isUpdate(sql)) {
                    flag = true;
                }
            } else {
                message.setInfo("需要删除的好友不存在");
            }
            message.setFlag(flag);

            this.oOut.writeObject(message);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端添加好友请求的处理
     */
    private void insert(Message message) {
        boolean flag = false;
        String id = message.getUserId();
        String sql;
        try {
            String name;
            ResultSet rs = Mysql.select("select name from users where id = '" + id + "'");
            if (rs.next()) {
                name = rs.getString("name");
                sql = "INSERT into contacts VALUES('" + id + "','" + name + "','" + this.id + "');";
                if (Mysql.isUpdate(sql)) {
                    flag = true;
                }
            } else {
                message.setInfo("需要添加的好友账号不存在");
            }
            message.setFlag(flag);

            this.oOut.writeObject(message);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String filePath, String str) {
        //创建字符流写入对象
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
            bufferedWriter.write(str);
        } catch (Exception e) {
        } finally {
            try {
                //关闭文件
                bufferedWriter.close();
            } catch (Exception e2) {
            }
        }
    }

    private void readFile(Message message) {
        BufferedReader bufferedReader = null;
        String msg = "";
        try {
            File file = new File("src\\Info\\" + message.getUserId() + "\\" + message.getTo() + ".txt");
            File file2 = new File("src\\Info\\" + message.getTo() + "\\" + message.getUserId() + ".txt");

            //文件不存在则新建
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            if (!file2.exists()) {
                file2.getParentFile().mkdirs();
                file2.createNewFile();
            }
            bufferedReader = new BufferedReader(new FileReader("src\\Info\\" + message.getUserId() + "\\" + message.getTo() +
                    ".txt"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                msg += str + "\n";
            }
            message.setInfo(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //判断是否有记录
            if (msg == "") {
                message.setFlag(false);
            } else {
                message.setFlag(true);
            }
            //关闭文件
            try {
                bufferedReader.close();
                oOut.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}