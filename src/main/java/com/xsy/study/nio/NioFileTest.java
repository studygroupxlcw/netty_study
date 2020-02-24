package com.xsy.study.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileTest {

    public static void main(String[] a) throws Exception {
        FileInputStream inputStream = new FileInputStream("test/input.txt");
        FileOutputStream outputStream = new FileOutputStream("test/output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10);

        while (true) {
            int read = inputChannel.read(buffer);

            System.out.println("read : " + read);
            if (-1 == read) {
                break;
            }

            buffer.flip();
            outputChannel.write(buffer);
            buffer.flip();
        }
    }

}
