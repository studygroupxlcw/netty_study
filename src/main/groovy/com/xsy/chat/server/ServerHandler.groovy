package com.xsy.chat.server

import com.xsy.chat.data.Data
import com.xsy.chat.server.util.ConnectionManager
import com.xsy.chat.server.util.MessageUtil
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter


class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        Data.Message msg = (Data.Message) obj
        switch (msg.getMsgType()) {
            case Data.Message.MsgType.SYSTEM:
                systemCommand(msg.getSystemMsg(), ctx)
                break;
            case Data.Message.MsgType.USER:
                chatCommand(msg.getChatMsg(), ctx)
                break;
            default:
                ctx.writeAndFlush(MessageUtil.generateSysResponse("Invalid Message!!!"))
        }
    }

    void chatCommand(Data.ChatMsg chatMsg, ChannelHandlerContext ctx) {
        String toUser = chatMsg.toUser
        if ("all".equalsIgnoreCase(toUser)) {
            ConnectionManager.instance.sendToAll(MessageUtil.generateChatMsg(fetchUserName(ctx.channel()), chatMsg.msg))
        } else {
            ConnectionManager.instance.sendToOne(MessageUtil.generateChatMsg(fetchUserName(ctx.channel()), chatMsg.msg), toUser)
        }
    }

    void systemCommand(Data.SystemMsg msg, ChannelHandlerContext ctx) {
        switch (msg.option) {
            case Data.SystemMsg.Option.WHO:
                ctx.writeAndFlush(
                        MessageUtil.generateSysResponse("Chat Room User List: ${ConnectionManager.instance.getAllConnection().join("; ")}"))
                break;
            case Data.SystemMsg.Option.NAME:
                if (null == msg.value || "" == msg.value || "server".equalsIgnoreCase(msg.value) || "all".equalsIgnoreCase(msg.value)) {
                    ctx.writeAndFlush(MessageUtil.generateSysResponse("Invalid Name!!!"))
                } else {
                    ConnectionManager.instance.setAttrs("name", msg.value, ctx.channel())
                }
                break;
        }
    }

    private String fetchUserName(Channel ch) {
        return ConnectionManager.instance.getAttrs("name", ch) == null ?
                ch.remoteAddress()
                :
                ConnectionManager.instance.getAttrs("name", ch)
    }

    @Override
    void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToAll(MessageUtil.generateSysResponse("${ctx.channel().remoteAddress()} Add Chat Room"))
        ConnectionManager.instance.addChannel(ctx.channel())
    }

    @Override
    void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToAll(MessageUtil.generateSysResponse("${ctx.channel().remoteAddress()} Left Chat Room"))
    }

    @Override
    void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToOther(MessageUtil.generateSysResponse("${ctx.channel().remoteAddress()} Online"), ctx.channel())
        ctx.writeAndFlush(MessageUtil.generateSysResponse("Chat Room User List: ${ConnectionManager.instance.getAllConnection().join("; ")}"))
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.sendToOther(MessageUtil.generateSysResponse("${ctx.channel().remoteAddress()} Offline"), ctx.channel())
    }

    @Override
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace()
        ctx.close()
    }
}
