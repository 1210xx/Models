package trysome.iotest;

import java.io.*;

public class IOTest {
    public static void main(String[] args) throws IOException {
        byte[] data = { 72, 101, 108, 108, 111, 33 };
        try (InputStream input = new ByteArrayInputStream(data)) {
            String s = readAsString(input);
            System.out.println(s);
        }
    }

    /**
     * 测试程序，就真的需要在本地硬盘上放一个真实的文本文件
     * @throws IOException
     */
    public void testInputStream() throws IOException {
        String s;
        try (InputStream input = new FileInputStream("C:\\test\\README.txt")) {
            int n;
            StringBuilder sb = new StringBuilder();
            while ((n = input.read()) != -1) {
                sb.append((char) n);
            }
            s = sb.toString();
        }
        System.out.println(s);
    }

    /**
     * 代码稍微改造一下，提取一个readAsString()的方法
     * 向抽象编程原则的应用：接受InputStream抽象类型，
     * 而不是具体的FileInputStream类型，从而使得代码可以处理InputStream的任意实现类。
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readAsString(InputStream inputStream) throws IOException {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        while((n = inputStream.read()) != -1){
            stringBuilder.append((char) n);
        }
      return stringBuilder.toString();
    }
}
