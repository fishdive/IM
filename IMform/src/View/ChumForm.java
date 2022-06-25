package View;

import Controller.Entrance_Static;
import Controller.PlayMusic;
import Controller.ReadInfoThread;
import Model.BaseForm;
import Model.MyException;
import Model.User;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author xiell
 */
public class ChumForm extends BaseForm {

    private JList list;
    private JLabel labelId;
    private JTextField textId;
    private JButton btnInsert;
    private JButton btnDelete;
    private JScrollPane scrollPane;

    public ChumForm() {
        super("好友管理");


        labelId = new JLabel("账号：", SwingConstants.CENTER);
        labelId.setBounds(400, 100, 100, 30);
        super.panel.add(labelId);
        textId = new JTextField();
        textId.setBounds(500, 100, 200, 30);
        super.panel.add(textId);
        btnInsert = new JButton("添加好友");
        btnInsert.setBounds(450, 250, 100, 30);
        super.panel.add(btnInsert);
        btnDelete = new JButton("删除好友");
        btnDelete.setBounds(600, 250, 100, 30);
        super.panel.add(btnDelete);

        //添加好友
        btnInsert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (Entrance_Static.client.isInsert(textId.getText())) {
                        JOptionPane.showMessageDialog(null, "添加好友成功！", "提示", JOptionPane.CLOSED_OPTION);
                        list.setListData(Entrance_Static.client.select(User.getId()));
                    } else {
                        JOptionPane.showMessageDialog(null, "添加好友失败！", "错误", JOptionPane.CLOSED_OPTION);
                    }
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "不可知异常，请让技术人员来处理！", "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                }
            }
        });

        //删除好友
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (Entrance_Static.client.isDelete(textId.getText())) {
                        JOptionPane.showMessageDialog(null, "删除好友成功！", "提示", JOptionPane.CLOSED_OPTION);
                        list.setListData(Entrance_Static.client.select(User.getId()));
                    } else {
                        JOptionPane.showMessageDialog(null, "删除好友失败！", "错误", JOptionPane.CLOSED_OPTION);
                    }
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "不可知异常，请让技术人员来处理！", "错误", JOptionPane.CLOSED_OPTION);
                    ex.printStackTrace();
                }
            }
        });

        //背景音乐管理按钮
        String path = "src\\images\\stop.jpeg";
        JButton button = new JButton(new ImageIcon(path));
        button.setBounds(600, 400, 50, 50);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (PlayMusic.isPlayFlag()) {
                    PlayMusic.setPlayFlag(false);
                    button.setIcon(new ImageIcon("src\\images\\start.jpeg"));
                    PlayMusic.close();
                } else {
                    PlayMusic.setPlayFlag(true);
                    button.setIcon(new ImageIcon("src\\images\\stop.jpeg"));
                    PlayMusic.playMusic();
                }
            }
        });
        super.panel.add(button);

    }

    @Override
    public void show() {
        try {
            //读取消息线程
            Entrance_Static.readInfoThread = new ReadInfoThread(Entrance_Static.client.getoIn());
            Entrance_Static.readInfoThread.start();

            list = new JList(Entrance_Static.client.select(User.getId()));
            list.setBorder(BorderFactory.createTitledBorder("联系人"));
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            list.setBounds(50, 50, 300, 400);
            scrollPane = new JScrollPane(list);
            scrollPane.setBounds(50, 50, 300, 400);
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    //判断鼠标双击
                    if (evt.getClickCount() == 2) {

                        //截取好友列表中的好友id信息
                        String str = list.getSelectedValue().toString();
                        String chatID = "";
                        for (int i = 0; i < str.length(); i++) {
                            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                                chatID += str.charAt(i);
                            }
                            if (str.charAt(i) == '呢') {
                                i = str.length();
                            }
                        }

                        //获取服务器中关于和该好友的聊天记录
                        String msg="";
                        try {
                         msg=Entrance_Static.client.message(chatID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Entrance_Static.chatForm.setStyle(str, chatID,msg);
                        Entrance_Static.chatForm.show();
                    }
                }
            });
            super.panel.add(scrollPane);
        } catch (IOException ex) {  //自定义异常
            JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
            ex.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "不可知异常，请让技术人员来处理！", "错误", JOptionPane.CLOSED_OPTION);
            ex.printStackTrace();
        }
        super.show();
    }
}
