package com.xsy.flink.server

import com.xsy.chat.server.util.ConnectionManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConnectionManager.instance.addChannel(ctx.channel())
    }

}
