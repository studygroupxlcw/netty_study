package com.xsy.chat.server

import com.xsy.chat.server.util.ConnectionManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter


class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            if ("who".equalsIgnoreCase(msg)) {
                ctx.writeAndFlush("[SERVER]: Chat Room User List: ${ConnectionManager.instance.getAllConnection().join("; ")} \r\n")
            } else {
                ctx.writeAndFlush("[Myself]: ${msg}\r\n")
                ConnectionManager.instance.sendToOther("[${ctx.channel().remoteAddress()}]: ${msg}", ctx.channel())
            }
        } else {
            ctx.writeAndFlush("[SERVER]: Invalid Message!!!")
        }
    }

    @Override
    void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToAll("[SERVER]: ${ctx.channel().remoteAddress()} Add Chat Room\r\n")
        ConnectionManager.instance.addChannel(ctx.channel())
    }

    @Override
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToAll("[SERVER]: ${ctx.channel().remoteAddress()} Left Chat Room\r\n")
    }

    @Override
    void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToOther("[SERVER]: ${ctx.channel().remoteAddress()} Online\r\n", ctx.channel())
        ctx.writeAndFlush("[SERVER]: Chat Room User List: ${ConnectionManager.instance.getAllConnection().join("; ")} \r\n")
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToOther("[SERVER]: ${ctx.channel().remoteAddress()} Offline\r\n", ctx.channel())
    }

    @Override
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace()
        ctx.close()
    }
}
