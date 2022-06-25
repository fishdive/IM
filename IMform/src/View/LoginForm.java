package View;

import Controller.Entrance_Static;
import Controller.PlayMusic;
import Model.BaseForm;
import Model.MyException;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author xiell
 */
public class LoginForm extends BaseForm {
    private JLabel labelId;
    private JLabel labelPassword;
    private JTextField textId;
    private JPasswordField textPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginForm() {

        //生成控件
        super("登录");
        labelId = new JLabel("账号：", SwingConstants.CENTER);
        labelId.setBounds(200, 50, 100, 30);
        super.panel.add(labelId);
        textId = new JTextField();
        textId.setBounds(350, 50, 200, 30);
        super.panel.add(textId);
        labelPassword = new JLabel("密码：", SwingConstants.CENTER);
        labelPassword.setBounds(200, 200, 100, 30);
        super.panel.add(labelPassword);
        textPassword = new JPasswordField();
        textPassword.setBounds(350, 200, 200, 30);
        super.panel.add(textPassword);
        btnLogin = new JButton("登录");
        btnLogin.setBounds(250, 350, 100, 30);
        super.panel.add(btnLogin);
        btnRegister = new JButton("注册");
        btnRegister.setBounds(400, 350, 100, 30);
        super.panel.add(btnRegister);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (Entrance_Static.client.isLogin(textId.getText(), String.valueOf(textPassword.getPassword()))) {
                        JOptionPane.showMessageDialog(null, "登录成功！即将进入聊天界面~~~~~", "欢迎", JOptionPane.CLOSED_OPTION);
                        close();
                        Entrance_Static.chum.show();
                        PlayMusic.playMusic();
                    }
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "不可知异常，请让技术人员来处理！", "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                }
            }
        });

        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hide();
                Entrance_Static.register.show();
            }
        });
    }
}
