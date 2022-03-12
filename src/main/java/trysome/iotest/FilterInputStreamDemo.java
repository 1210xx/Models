package trysome.iotest;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FilterInputStreamDemo {
    public static void main(String[] args) throws IOException{
        byte[] data = "hello, world !".getBytes(StandardCharsets.UTF_8);
        try (CountInputStream inputStream = new CountInputStream(new ByteArrayInputStream(data))){
            int n;
            while((n = inputStream.read()) != -1){
                System.out.println((char)n);
            }
            System.out.println("Total read "+ inputStream.getBytesRead() + " bytes");
        }
    }
}

class CountInputStream extends FilterInputStream {
    private int count = 0;

    CountInputStream(InputStream in){
        super(in);
    }

    public int getBytesRead(){
        return this.count;
    }

    public int read() throws IOException{
        int n = in.read();
        if (n != -1){
            this.count ++;
        }

        return n;
    }

    public int read(byte[] b, int off, int len) throws IOException{
        int n = in.read();
        if (n != -1){
            this.count += n;
        }
        return n;
    }
}