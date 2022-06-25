package Controller;

import Model.Message;
import Model.MessageType;
import View.ChatForm;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Vector;

enum MessageFlag {
    /**
     * enull表示未接收到服务器消息，etrut表示服务器处理结果为真，efalse表示结果为假
     */
    enull, etrue, efalse
}

/**
 * 读取其他客户端发来消息
 *
 * @author leilei
 * @version 1.0
 */
public class ReadInfoThread extends Thread {
    /**
     * 输入流 用来读操作
     */
    private ObjectInputStream oIn;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 标记
     */
    private boolean flag = true;

    /**
     * 服务器返回的序列化消息包
     */
    private Message message;

    private static MessageFlag messageFlag;

    public static MessageFlag getMessageFlag() {
        return messageFlag;
    }

    public static void setMessageFlag(MessageFlag messageFlag) {
        ReadInfoThread.messageFlag = messageFlag;
    }

    public static Vector<String> getVector() {
        return vector;
    }

    /**
     * 好友列表集合
     */
    private static Vector<String> vector;

    public static String getMessageLogging() {
        return messageLogging;
    }

    /**
     * 消息记录
     * */
    private static String messageLogging;
    public ReadInfoThread() {
    }

    public ReadInfoThread(ObjectInputStream oIn) {
        this.oIn = oIn;
    }

    @Override
    public void run() {

        try {
            //循环 不断读取消息
            while (flag) {
                //读取信息
                Message message = (Message) oIn.readObject();
                //获取消息类型
                int type = message.getType();
                //3、判断
                switch (type) {
                    //如果是发送消息
                    case MessageType.TYPE_SEND:
                        Read(message);
                        break;
                    //如果是查找好友
                    case MessageType.TYPE_SELECT:
                        select();
                        break;
                    //如果是删除好友,添加删除共用update方法即可
                    case MessageType.TYPE_DELETE:
                        //如果是添加好友
                    case MessageType.TYPE_INSERT:
                        update(message);
                        break;
                    case MessageType.TYPE_OFF:
                        oIn.close();
                        return;
                    case MessageType.TYPE_MESSAGE:
                        redMessage(message);
                    default:
                }
            }
            //没有数据就关闭
            if (oIn != null) {
                oIn.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Read(Message message) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //输出用户名+内容
        ChatForm.setMessage(df.format(System.currentTimeMillis()) + "\n[" + message.getFrom() + "]对我说：" + message.getInfo() + "\n");
    }

    public void select() {
        try {
            vector = (Vector) oIn.readObject();
            messageFlag = MessageFlag.etrue;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(Message message) {
        if (message.isFlag()) {
            messageFlag = MessageFlag.etrue;
        } else {
            messageFlag = MessageFlag.efalse;
            //如果不成功则返回服务器异常信息！
            JOptionPane.showMessageDialog(null, "Error:" + message.getInfo(), "错误", JOptionPane.CLOSED_OPTION);
        }
    }
    public void redMessage(Message message){
        if (message.isFlag()) {
            messageLogging=message.getInfo();
            messageFlag = MessageFlag.etrue;
        } else {
            messageFlag = MessageFlag.efalse;
        }
    }
}
