package Model;

import Controller.Client;
import Controller.Entrance_Static;
import Controller.PlayMusic;
import Controller.ReadInfoThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * 窗体基类
 *
 * @author leilei
 * @version 1.0
 */
public class BaseForm {
    protected JFrame frame;
    protected JPanel panel;

    protected BaseForm() {
    }

    /**
     * @param title 窗体标题
     */
    protected BaseForm(String title) {
        frame = new JFrame(title);
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Image image = new ImageIcon("src\\images\\bg.jpg").getImage();
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        //设置布局为null，通过设置各个控件位置实现布局
        frame.setBounds(450, 100, 800, 500);
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        panel.setLayout(null);

        //logo标签
        JLabel label = new JLabel("©九江职业技术学院-计算机本2101");
        label.setBounds(350, 400, 200, 100);
        frame.add(panel);
        panel.add(label);
        //窗体关闭后把与服务器通信也中断,重写父类监听器，把关闭接收服务器消息线程也放进去
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    Entrance_Static.client.off();
                    //中断读取服务器消息线程
                    if (Entrance_Static.readInfoThread.isAlive()){
                        Entrance_Static.readInfoThread.interrupt();
                    }
                    //关闭socket连接
                    Entrance_Static.client.getSocket().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println(123);
                System.exit(0);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 显示窗体
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * 隐藏窗体
     */
    public void hide() {
        frame.setVisible(false);
    }

    /**
     * 关闭窗体
     */
    public void close() {
        frame.dispose();
    }

}
