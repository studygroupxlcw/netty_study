package com.xsy.chat.client

import com.xsy.chat.data.Data
import com.xsy.chat.server.util.ConnectionManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientHandler extends ChannelInboundHandlerAdapter{

    @Override
    void channelRead(ChannelHandlerContext ctx, Object obj) {
        Data.Message msg = (Data.Message) obj
        if (msg.msgType == Data.Message.MsgType.USER) {
            println "[${msg.chatMsg.fromUser}]: ${msg.chatMsg.msg}"
        }
    }


    @Override
    void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        println "added"
    }

    @Override
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        println "removed"
    }

    @Override
    void channelActive(ChannelHandlerContext ctx) throws Exception {
        println "active"
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) throws Exception {
        println "inactive"
    }

    @Override
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace()
        ctx.close()
    }
}
