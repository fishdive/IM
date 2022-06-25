package Controller;

import Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 处理发送消息的线程类
 *
 * @author leilei
 * @version 1.0
 */
public class SendInfoThread extends Thread {
    private ObjectOutputStream oOut;
    private ObjectInputStream oIn;

    public Message getMessage() {
        return message;
    }

    private Message message;

    SendInfoThread(ObjectOutputStream oOut, ObjectInputStream oIn, Message message) {
        super();
        this.oOut = oOut;
        this.oIn = oIn;
        this.message = message;
    }

    @Override
    public void run() {
        //发送给服务器
        try {
            oOut.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
