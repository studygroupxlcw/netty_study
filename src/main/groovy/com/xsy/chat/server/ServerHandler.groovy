package com.xsy.chat.server

import com.sun.xml.internal.ws.util.StringUtils
import com.xsy.chat.server.util.ConnectionManager
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter


class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            if (msg.startsWith("\$set:")) {
                systemCommand(msg.substring(5), ctx.channel())
            } else if ("who".equalsIgnoreCase(msg)){
                ctx.writeAndFlush("[SERVER]: Chat Room User List: ${ConnectionManager.instance.getAllConnection().join("; ")} \r\n")
            }else {
                ctx.writeAndFlush("[${ConnectionManager.instance.getAttrs("name",  ctx.channel()) == null ? "Myself" : ConnectionManager.instance.getAttrs("name",  ctx.channel())}]: ${msg}\r\n")
                ConnectionManager.instance.sendToOther("[${ConnectionManager.instance.getAttrs("name",  ctx.channel()) == null ? ctx.channel().remoteAddress() : ConnectionManager.instance.getAttrs("name",  ctx.channel())}]: ${msg}\r\n", ctx.channel())
            }
        } else {
            ctx.writeAndFlush("[SERVER]: Invalid Message!!!")
        }
    }

    void systemCommand(String s, Channel ch) {
        // name=XXX,sex=XXX
        s.split(",").each {
            command ->
                if (!command.contains("=")) {
                    return
                }
                String[] attrs = command.split("=")
                if(attrs.length != 2) {
                    return
                }
                switch (attrs[0].toLowerCase()) {
                    case "name" :
                        ConnectionManager.instance.setAttrs("name", attrs[1], ch)
                        break;
                }
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
