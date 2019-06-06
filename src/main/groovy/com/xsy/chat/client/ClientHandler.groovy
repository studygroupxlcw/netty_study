package com.xsy.chat.client

/**
 * @Param
 * Created by Administrator on 2019/6/6 0006.
 */
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        println msg
    }

    @Override
    void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        println "channer added"
    }

    @Override
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        println  "channer removed"
    }

    @Override
    void channelActive(ChannelHandlerContext ctx) throws Exception {
        println  "channner  active"
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) throws Exception {
        println  "channer inactive"
    }

    @Override
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        println "channer exception"
    }
}
