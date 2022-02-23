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
        //创建并绑定端口
        ServerSocket socket = new ServerSocket(8080);
        System.out.println("Server is running....(Just listening)");
        //开启线程，处理接口数据
        for (;;) {
            //监听于此端口连接的socket并且接收发送信息。
            Socket socket_l = socket.accept();
            //输出接收到的接口地址
            System.out.println("Connected from " + socket_l.getRemoteSocketAddress());
            //启动线程处理接口接收到信息
            Thread handleThread = new SocketHandle(socket_l);
            //启动线程
            handleThread.run();
        }
    }
}

//socket 处理类
class SocketHandle extends Thread {
    //socket 字段
    Socket socket;
    //socketHandle 无参构造方法
    SocketHandle(Socket socket) {
        this.socket = socket;
    }

    // 线程必须重写run方法实现业务
    @Override
    public void run() {
        //父类的run方法
        super.run();
        try {
            //获取socket的输入流，有异常抛出
            InputStream input = this.socket.getInputStream();
            //如果输出无异常
            try (OutputStream output = this.socket.getOutputStream()) {
                //通过输入流（请求）和处理得到输出流
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                //关闭socket接口
                this.socket.close();
            } catch (IOException ex) {
                System.out.println("Client disconnected.....");
            }
        }


    }
    //具体的处理方式，只包括正确的请求和错误请求
    private void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        //创建输入流
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        //创建输出流
        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //读取http请求
        boolean requestStatus = false;
        //获取输入流的第一条信息，是否以GET / HTTP/1.（程序中只针对get做出处理）
        String startFirst = ((BufferedReader) reader).readLine();
        if (startFirst.startsWith("GET / HTTP/1.")) {
            requestStatus = true;
        }
        //循环处理
        for (; ; ) {
            //读取传入的头部信息
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
            //清空输出流中的缓存
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
