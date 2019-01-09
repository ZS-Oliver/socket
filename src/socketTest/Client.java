package socketTest;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端
 */
public class Client {
    public static List<Socket> serList = new ArrayList<>();
    static Socket socket = null;

    public static void main(String[] args) {

        // 创建客户端窗口
        SocketUI socketUI = new SocketUI();
        socketUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        socketUI.setLocation((width - socketUI.WIDTH) / 2, (height - socketUI.HEIGHT) / 2);
        socketUI.setVisible(true);


        try {
            // 创建客户端Socket,指定服务器地址和端口号
            socket = new Socket("localhost", 1024);
            // 输出欢迎信息
            readMsgFromServer(socketUI);

            if (!serList.contains(socket)) {
                new Thread(() -> {
                    readMsgFromServer(socketUI);

                }).start();
                serList.add(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readMsgFromServer(SocketUI socketUI) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info = null;
            while ((info = br.readLine()) != null) {
                socketUI.jta.append(info);
                socketUI.jta.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
