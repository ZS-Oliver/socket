package socketTest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class SocketUI extends JFrame {

    final int WIDTH = 700;
    final int HEIGHT = 600;

    JButton bSend = new JButton("发送");
    JButton bQuit = new JButton("退出");

    JTextField jtf = new JTextField();
    JTextArea jta = new JTextArea();

    String[][] online = null;
    String[] title = {"在线用户列表"};

    JTable jtbOnline = new JTable(new DefaultTableModel(online, title) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });

    // 聊天消息滚动效果
    JScrollPane jsChat = new JScrollPane(jta);
    // 在线用户滚动效果
    JScrollPane jsOnline = new JScrollPane(jtbOnline);

    public SocketUI() {

        // 聊天室窗口属性
        setTitle("测试聊天室");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(null);

        // 按钮属性
        bSend.setBounds(480, 500, 85, 45);

        bQuit.setBounds(595, 500, 85, 45);

        bSend.setFont(new Font("宋体", Font.BOLD, 18));

        bQuit.setFont(new Font("宋体", Font.BOLD, 18));

        this.add(bSend);
        this.add(bQuit);

        // 聊天框属性
        jtf.setBounds(20, 500, 430, 45);

        jtf.setFont(new Font("宋体", Font.BOLD, 16));

        // 为聊天框增加回车监听
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == '\n') {
                    jta.setCaretPosition(jta.getDocument().getLength());

                    //显示发送消息
                    jtaAddBlock(Server.sdf.format(new Date()).length(), jta);
                    jta.append(Server.sdf.format(new Date()));
                    jtaAddBlock1(jtf.getText().length(), jta);
                    jta.append(jtf.getText() + "\n\n");

                    //向服务器发送聊天信息
                    OutputStream os = null;
                    try {
                        os = Client.socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(os);
                        pw.println(jtf.getText());
                        pw.flush();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    //文本输入框清除
                    jtf.setText("");
                }
            }
        });
        this.add(jtf);

        // 信息显示框属性
        jta.setEditable(false);

        jta.setFont(new Font("宋体", Font.BOLD, 16));

        jta.setLineWrap(true);

        // 信息显示框滚动条属性
        jsChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        jsChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jsChat.setBounds(20, 20, 660, 450);

        this.add(jsChat);


        // 发送响应
        bSend.addActionListener

                (event -> {
                    //显示最新消息
                    jta.setCaretPosition(jta.getDocument().getLength());

                    //显示发送消息
                    jtaAddBlock(Server.sdf.format(new Date()).length(), jta);
                    jta.append(Server.sdf.format(new Date()));
                    jtaAddBlock1(jtf.getText().length(), jta);
                    jta.append(jtf.getText() + "\n\n");

                    //向服务器发送聊天信息
                    OutputStream os = null;
                    try {
                        os = Client.socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(os);
                        pw.println(jtf.getText());
                        pw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //文本输入框清除
                    jtf.setText("");
                });


        // 退出响应
        bQuit.addActionListener
                (event -> {

                    for (Socket s : Client.serList) {

                        if (s == Client.socket) {
                            try {
                                Client.socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    System.exit(0);

                });
    }

    public void jtaAddBlock(int length, JTextArea jta) {
        for (int i = 0; i < 560 / 8 - length; i++) {
            jta.append(" ");
        }
    }

    public void jtaAddBlock1(int length, JTextArea jta) {
        for (int i = 0; i < 570 / 8 - length * 2; i++) {
            jta.append(" ");
        }
    }

}
