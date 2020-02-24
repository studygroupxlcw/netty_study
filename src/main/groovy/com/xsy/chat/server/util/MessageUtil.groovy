package com.xsy.chat.server.util;

import com.xsy.chat.data.Data;

class MessageUtil {

     static Data.Message generateChatMsg(String fromUser, String content, String toUser = "") {
        return Data.Message.newBuilder()
                .setMsgType(Data.Message.MsgType.USER)
                .setChatMsg(Data.ChatMsg.newBuilder()
                        .setFromUser(fromUser)
                        .setMsg(content)
                        .setToUser(toUser)
                        .build())
                .build()
    }

    static Data.Message generateSysResponse(String msg) {
        Data.Message response = Data.Message.newBuilder()
                .setMsgType(Data.Message.MsgType.USER)
                .setChatMsg(Data.ChatMsg.newBuilder()
                .setFromUser("SERVER")
                .setMsg(msg)
                .build())
                .build()
        response
    }

    static Data.Message generateSysCmd(Data.SystemMsg.Option option, String value = "") {
        return Data.Message.newBuilder().setMsgType(Data.Message.MsgType.SYSTEM)
        .setSystemMsg(Data.SystemMsg.newBuilder().setOption(option).setValue(value).build())
        .build()
    }
}
