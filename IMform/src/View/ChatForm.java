package View;

import Controller.Entrance_Static;
import Model.BaseForm;
import Model.MyException;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/**
 * @author xiell
 */
public class ChatForm extends BaseForm {
    /**
     * 当前聊天好友的ID号
     */
    private String chatID;

    /**
     * 获取发送消息时间
     */
    private SimpleDateFormat df;
    /**
     * 对话消息记录
     */
    private static String message;
    private static JTextArea textArea;
    private JTextField text;
    private JButton btnSent;
    private JScrollPane scrollPane;


    public ChatForm() {
        super("聊天");

        //富文本框用来显示聊天记录
        textArea = new JTextArea();
        //设置为只读
        textArea.setEditable(false);
        textArea.setBounds(0, 0, 350, 400);
        //激活自动换行
        textArea.setLineWrap(true);
        //激活断行不断字
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("标楷体", Font.BOLD, 12));
        //设置滚动条
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 0, 350, 400);
        super.panel.add(scrollPane);

        text = new JTextField();
        text.setBounds(1, 401, 250, 60);
        super.panel.add(text);
        btnSent = new JButton("发送");
        btnSent.setBounds(253, 401, 80, 60);
        super.panel.add(btnSent);

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        btnSent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Entrance_Static.client.isSent(chatID, text.getText());
                    setMessage(df.format(System.currentTimeMillis()) + "\n" + User.getName() + "(我)：" + text.getText() + "\n");
                    text.setText("");

                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "不可知异常，请让技术人员来处理！", "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置聊天框内容
     */
    public void setStyle(String title, String chatID, String message) {
        frame.setTitle(title);
        frame.setBounds(100, 100, 350, 500);
        this.chatID = chatID;
        ChatForm.message = message;
        textArea.setText(message);
        text.setText("");
    }

    public static void setMessage(String s) {
        message += s;
        textArea.setText(message);
    }
}
