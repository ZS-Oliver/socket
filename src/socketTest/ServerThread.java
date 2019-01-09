package socketTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

/**
 * 服务器端线程处理类
 */
public class ServerThread implements Runnable {

    Scanner sc = new Scanner(System.in);

    Socket socket = null;
    String info = null;
    BufferedReader br = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    // 线程操作,响应客户端的请求
    @Override
    public void run() {

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 登录应答
            for (int i = 0; i < Server.serverList.size(); i++) {
                if (Server.serverList.get(i) == socket) {
                    info = "欢迎" + socket.getInetAddress().getHostAddress() + "进入聊天室！";
                } else {
                    info = "用户：" + socket.getInetAddress().getHostAddress() + "已连接  " + Server.sdf.format(new Date());
                }

                PrintWriter pw = new PrintWriter(Server.serverList.get(i).getOutputStream(), true);
                pw.println(info);
                pw.flush();
            }

            while ((info = br.readLine()) != null) {
                for (int i = 0; i < Server.serverList.size(); i++) {
                    if (Server.serverList.get(i) != socket) {
                        PrintWriter pw = new PrintWriter(Server.serverList.get(i).getOutputStream(), true);
                        pw.println(Server.sdf.format(new Date()) + '\n' + socket.getInetAddress().getHostAddress() + ": " + info + "\n\n");
                        pw.flush();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//        new Thread(() -> {
//            try {
//                PrintWriter pw = new PrintWriter(socket.getOutputStream());
//                String word = sc.next();
//                for (Socket s : Server.serverList) {
//                    socket = s;
//                    System.out.println(s);
//                    pw.println(word);
//                    pw.flush();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

}
