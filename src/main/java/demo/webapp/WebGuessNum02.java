package demo.webapp;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * <head>web编程GuessNum02</head>
 *
 * <p>
 *     <ol>
 *         <li>
 *             通过最原始的socket编程交互
 *         </li>
 *         <li>
 *              从最简单的GET获取参数
 *         </li>
 *         <li>
 *             返回简单的页面
 *         </li>
 *     </ol>
 * </p>
 */
public class WebGuessNum02 {
    public static void main(String[] args) throws IOException {
//        GuessNum guessNum = new GuessNum();
//        guessNum.guessNumApp();
        socketServer();
    }


    public static void socketServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server is running....(listening port: 8888)");
//        编译前              编译后
//        while (1)；         mov eax,1
//                          test eax,eax
//                          je foo+23h
//                          jmp foo+18h
//        编译前                 编译后
//        for (；；)；          jmp foo+23h 　　
        for (; ; ) {
            Socket socket = serverSocket.accept();
            System.out.println("connected from " + socket.getRemoteSocketAddress());
            Thread handelThread = new GuessNumSocketHandle02(socket);
            handelThread.run();
        }
    }
}


class GuessNumSocketHandle02 extends Thread {
    Socket socket;

    public GuessNumSocketHandle02(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream inputStream = this.socket.getInputStream();
            try (OutputStream outputStream = this.socket.getOutputStream()) {
                handle(inputStream, outputStream);
            }
        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Client disconnected.....");
            }
            e.printStackTrace();
        }
    }

    private void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        boolean requestStatus = false;
        String firstLine = reader.readLine();
        if (firstLine.startsWith("GET")) {
            requestStatus = true;
        }
        String[] headerStrings = firstLine.split(" ");

        String name = null;
        String[] nameStrings = headerStrings[1].split("=");
        String parameter = nameStrings[0].substring(2);
        if (parameter.equalsIgnoreCase("name")){
            name = nameStrings[1];
        }else{
            name = "World";
        }

        for (; ; ) {
            String header = reader.readLine();
            if (header.isEmpty()) {
                break;
            }
            System.out.println("===============");
            System.out.println(header);
        }
        System.out.println("-----------------");
        System.out.println(requestStatus ? "requestStatus: Request OK" : "requestStatus: Request ERROR");

        if (!requestStatus) {
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            //清空输出流中的缓存
            writer.flush();
        } else {
            String printInfo = "<html><body><h1>hello, " + name +" !!!</h1></body></html>";
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
