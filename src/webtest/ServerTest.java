package webtest;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * TCP
 * 多线程实现
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        //创建并监听端口
        ServerSocket socket = new ServerSocket(8080);
        System.out.println("Server is running....(Just listening)");
        //开启线程，处理接口数据
        for (;;) {
            //
            Socket socket_l = socket.accept();
            //输出接收到的接口地址
            System.out.println("Connected from " + socket_l.getRemoteSocketAddress());
            //启动线程处理接口接收到信息
            Thread handleThread = new SocketHandle(socket_l);
            handleThread.run();
        }
    }
}

class SocketHandle extends Thread {
    Socket socket;

    SocketHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream input = this.socket.getInputStream();
            try (OutputStream output = this.socket.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                System.out.println("Client disconnected.....");
            }
        }


    }

    private void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //读取http请求
        boolean requestStatus = false;
        String startFirst = ((BufferedReader) reader).readLine();
        if (startFirst.startsWith("GET / HTTP/")) {
            requestStatus = true;
        }
        for (; ; ) {
            String header = ((BufferedReader) reader).readLine();
            if (header.isEmpty()) {
                break;
            }
            System.out.println(header);
        }
        System.out.println(requestStatus ? "requestStatus: Request OK" : "requestStatus: Request ERROR");

        //错误请求
        if (!requestStatus) {
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else {//正确响应
            String printInfo = "<html><body><h1> Hello World,local server is coming</h1></body></html>";
            int length = printInfo.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n");
            writer.write(printInfo);
            writer.flush();
        }
    }
}
