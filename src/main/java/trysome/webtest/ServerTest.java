package trysome.webtest;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * TCP
 * 多线程实现
 */
public class ServerTest {
    // 全局HttpClient:
    //static HttpClient httpClient = HttpClient.newBuilder().build();

    public static void main(String[] args) throws IOException {
        serverFun();


    }
    //JDK11
    public static void httpFun() {
        String url = "https://www.sina.com.cn/";
//        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                // 设置Header:
//                .header("User-Agent", "Java HttpClient").header("Accept", "*/*")
                // 设置超时:
//                .timeout(Duration.ofSeconds(5))
                // 设置版本:
//                .version(Version.HTTP_2).build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // HTTP允许重复的Header，因此一个Header可对应多个Value:
//        Map<String, List<String>> headers = response.headers().map();
//        for (String header : headers.keySet()) {
//            System.out.println(header + ": " + headers.get(header).get(0));
//        }
//        System.out.println(response.body().substring(0, 1024) + "...");

    }

    public static void serverFun() throws IOException {
        //创建并绑定端口
        ServerSocket socket = new ServerSocket(8080);
        System.out.println("Server is running....(Just listening)");
        //开启线程，处理接口数据
        for (; ; ) {
            //监听于此端口连接的socket并且接收发送信息。
            //accept()表示每当有新的客户端连接进来后，就返回一个Socket实例，
            // 这个Socket实例就是用来和刚连接的客户端进行通信的。
            // 由于客户端很多，要实现并发处理，我们就必须为每个新的Socket创建一个新线程来处理，
            // 这样，主线程的作用就是接收新的连接，每当收到新连接后，就创建一个新线程进行处理
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
        //创建输入流，将抽象inputStream字节流转换为InputStreamReader直接流，然后转换为缓冲字符流BufferedReader
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
            //判断连接的socket的头部是否为空
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
