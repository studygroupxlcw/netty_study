package com.xsy.chat.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientHandler extends ChannelInboundHandlerAdapter{
    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        println msg
    }

    @Override
    void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        println "add"
    }

    @Override
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        println "remove"
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
        println "exception"
    }
}
