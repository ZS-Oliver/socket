package socketTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务端
 */
public class Server {
    static List<Socket> serverList = new ArrayList<>();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public static void main(String[] args) {
        try {
            // 创建一个服务器端Socket,指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(1024);
            Socket socket = null;

            System.out.println("服务器已启动,等待客户端的连接...");

            // 循环监听等待客户端的连接
            while (true) {
                // 调用accept()方法监听,等待客户端的连接
                socket = serverSocket.accept();
                serverList.add(socket);

                // 创建一个新的线程
                ServerThread serverThread = new ServerThread(socket);

                // 启动线程
                new Thread(serverThread).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
