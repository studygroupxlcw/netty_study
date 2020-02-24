package com.xsy.study.nio;

import com.xsy.chat.data.Data;

public class ProtobufTest {

    public static void main(String[] args) {
        Data.Message msg = Data.Message.newBuilder().build();
        System.out.println(msg.toString());
    }

}
