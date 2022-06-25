package Controller;

import View.ChatForm;
import View.ChumForm;
import View.LoginForm;
import View.RegisterForm;

import javax.swing.*;
import java.io.IOException;

/**
 * @author xiell
 */
public class Entrance_Static {
    public static LoginForm login;
    public static RegisterForm register;
    public static ChumForm chum;
    public static Client client;
    public static ChatForm chatForm;
    public static ReadInfoThread readInfoThread;

    public static void main(String[] args) {

        //窗体页面
        login = new LoginForm();
        register = new RegisterForm();
        chum=new ChumForm();
        chatForm=new ChatForm();

        //客户端通信类
        try {
            client=new Client();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "服务器连接失败！请关闭客户端重试！", "错误", JOptionPane.CLOSED_OPTION);
            e.printStackTrace();
        }finally {
            login.show();
        }
    }
}
