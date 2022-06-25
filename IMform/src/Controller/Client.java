package Controller;

import Model.Message;
import Model.MessageType;
import Model.MyException;
import Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author xiell
 */
public class Client {

    public  Socket getSocket() {
        return socket;
    }

    private  Socket socket;
    private String ip = InetAddress.getLocalHost().getHostAddress();
    private int port = 8888;
    private  ObjectOutputStream oOut;

    public  ObjectInputStream getoIn() {
        return oIn;
    }

    private  ObjectInputStream oIn;

    public Client() throws IOException {
        socket = new Socket(ip, port);
        System.out.println("服务器连接成功！");
        oOut = new ObjectOutputStream(socket.getOutputStream());
        oIn = new ObjectInputStream(socket.getInputStream());
    }

    public  Client(String ip, int port) throws IOException {
        this.ip=ip;
        this.port=port;
        socket = new Socket(ip, port);
        System.out.println("服务器连接成功！");
        oOut = new ObjectOutputStream(socket.getOutputStream());
        oIn = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * 客户端登录处理
     */
    public  boolean isLogin(String id, String password) throws MyException, IOException, ClassNotFoundException {
        Message message = new Message(id, password, MessageType.TYPE_LOGIN);
        //发送给服务器
        oOut.writeObject(message);
        //服务器返回消息序列化
        message = (Message) oIn.readObject();
        if (message.isFlag()) {
            User.setId(message.getUserId());
            User.setName(message.getUserName());
        } else {
            throw new MyException("Error:" + message.getInfo());
        }
        return message.isFlag();
    }
    /**
     * 客户端注册处理
     */
    public boolean isRegister(String id, String name, String password, String confirmPassword) throws MyException, IOException, ClassNotFoundException {
        //验证密码准确性
        int maxPassword = 18;
        int minPassword = 8;
        if (password.length() > maxPassword || password.length() < minPassword) {
            throw new MyException("\"Error(1.3):密码长度范围大于等于8小于等于18！\n提示：请重新输入密码\"");
        }

        if (!password.equals(confirmPassword)) {
            throw new MyException("Error(1.4):两次密码不一致！\n提示：请重新输入密码");
        }

        Message message = new Message(id, password, name, MessageType.TYPE_REGISTER);
        //发送给服务器
        oOut.writeObject(message);
        //服务器返回消息序列化
        message = (Message) oIn.readObject();
        if (message.isFlag()) {
            User.setId(message.getUserId());
            User.setName(message.getUserName());
        } else {
            //抛出服务器返回的错误信息！
            throw new MyException("Error:" + message.getInfo());
        }
        return message.isFlag();
    }

    /**
     * 查询好友列表
     */
    public  Vector<String> select(String id) throws SQLException, IOException, Exception {
        Message message = new Message(id, MessageType.TYPE_SELECT);
        //发送给服务器
        oOut.writeObject(message);
        //设置服务器读取状态为enull
        ReadInfoThread.setMessageFlag(MessageFlag.enull);
        //相当于阻塞在此处等待结果
        while (true){
            Thread.sleep(100);
            if (ReadInfoThread.getMessageFlag()==MessageFlag.etrue){
                return ReadInfoThread.getVector();
            }
        }
    }

    /**
     * 删除好友
     */
    public boolean isDelete(String id) throws SQLException, MyException, Exception {
        if (id == null) {
            throw new MyException("Error:id不能为空，请先输入id");
        }
        Message message = new Message(id, MessageType.TYPE_DELETE);
        //发送给服务器
        oOut.writeObject(message);

        //调用该方法获取服务器返回结果，此方法避免socket一直读取好友消息而导致其他操作结果异常
        return messageFlag();
    }

    /**
     * 添加好友
     */
    public boolean isInsert(String id) throws SQLException, MyException, Exception {
        if (id == null) {
            throw new MyException("Error:id不能为空，请先输入id");
        }
        Message message = new Message(id, MessageType.TYPE_INSERT);
        //发送给服务器
        oOut.writeObject(message);

        return messageFlag();
    }

    /**
     * 发送消息
     */
    public void isSent(String chatID, String info) throws MyException{
        if (info == "") {
            throw new MyException("Error:消息不能为空，请先输入消息");
        }
        Message message = new Message(User.getName(), chatID, MessageType.TYPE_SEND, info);
        SendInfoThread sendInfoThread=new SendInfoThread(oOut,oIn,message);
        sendInfoThread.start();
    }

    /**给服务器发送断开请求*/
    public  void off() throws IOException{
        Message message = new Message(MessageType.TYPE_OFF);
        //发送给服务器
        oOut.writeObject(message);
    }

    /**给服务器发送读取聊天记录请求*/
    public String message(String chatID)  throws Exception{
        Message message=new Message(User.getId(), chatID, MessageType.TYPE_MESSAGE);
        //发送给服务器
        oOut.writeObject(message);
        //设置服务器读取状态为enull
        ReadInfoThread.setMessageFlag(MessageFlag.enull);
        //相当于阻塞在此处等待结果
        while (true){
            Thread.sleep(100);
            if (ReadInfoThread.getMessageFlag()==MessageFlag.etrue){
                return ReadInfoThread.getMessageLogging();
            }else {
                return "服务器无历史记录！";
            }
        }
    }
    /**
     * 此方法避免socket一直读取好友消息而导致其他操作结果异常
     * */
    private  boolean messageFlag() throws Exception{
        //设置服务器读取状态为enull
        ReadInfoThread.setMessageFlag(MessageFlag.enull);
        //相当于阻塞在此处等待结果
        while (true){
            Thread.sleep(100);
            if (ReadInfoThread.getMessageFlag()==MessageFlag.etrue){
                return true;
            }else if(ReadInfoThread.getMessageFlag()==MessageFlag.efalse){
                return false;
            }
        }
    }
}
