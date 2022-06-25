package View;

import Controller.Entrance_Static;
import Model.BaseForm;
import Model.MyException;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author xiell
 */
public class RegisterForm extends BaseForm {
    private JLabel labelId;
    private JLabel labelName;
    private JLabel labelPassword;
    private JLabel labelConfirmPassword;
    private JTextField textId;
    private JTextField textName;
    private JPasswordField textPassword;
    private JPasswordField textConfirmPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    public RegisterForm() {
        //生成控件
        super("注册");
        labelId = new JLabel("账号：", SwingConstants.CENTER);
        labelId.setBounds(200, 50, 100, 30);
        super.panel.add(labelId);
        textId = new JTextField();
        textId.setBounds(350, 50, 200, 30);
        super.panel.add(textId);
        labelName = new JLabel("昵称：", SwingConstants.CENTER);
        labelName.setBounds(200, 125, 100, 30);
        super.panel.add(labelName);
        textName = new JTextField();
        textName.setBounds(350, 125, 200, 30);
        super.panel.add(textName);
        labelPassword = new JLabel("密码：", SwingConstants.CENTER);
        labelPassword.setBounds(200, 200, 100, 30);
        super.panel.add(labelPassword);
        textPassword = new JPasswordField();
        textPassword.setBounds(350, 200, 200, 30);
        super.panel.add(textPassword);
        labelConfirmPassword = new JLabel("确认密码：", SwingConstants.CENTER);
        labelConfirmPassword.setBounds(200, 275, 100, 30);
        super.panel.add(labelConfirmPassword);
        textConfirmPassword = new JPasswordField();
        textConfirmPassword.setBounds(350, 275, 200, 30);
        super.panel.add(textConfirmPassword);
        btnLogin = new JButton("登录");
        btnLogin.setBounds(250, 350, 100, 30);
        super.panel.add(btnLogin);
        btnRegister = new JButton("注册");
        btnRegister.setBounds(400, 350, 100, 30);
        super.panel.add(btnRegister);

        //登录按钮监听器
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
                Entrance_Static.login.show();
            }
        });

        //注册按钮监听器
        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (Entrance_Static.client.isRegister(textId.getText(), textName.getText(), String.valueOf(textPassword.getPassword()),
                            String.valueOf(textConfirmPassword.getPassword()))) {
                        textId.setText("");
                        textName.setText("");
                        textPassword.setText("");
                        textConfirmPassword.setText("");
                        JOptionPane.showMessageDialog(null, "注册成功，请登录！", "结果", JOptionPane.CLOSED_OPTION);

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
    }
}
