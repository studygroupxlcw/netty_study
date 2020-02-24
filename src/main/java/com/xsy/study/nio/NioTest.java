package com.xsy.study.nio;

import java.nio.IntBuffer;

public class NioTest {

    public static void main(String[] a) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < 8; i++) {
            buffer.put(1);
        }

        buffer.rewind();

        for (int i = 0; i < 5; i++) {
            buffer.put(2);
        }

        buffer.rewind();

        while(buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }

}
