package com.xsy.chat.client

import com.xsy.chat.data.Data
import com.xsy.chat.server.ServerHandler
import com.xsy.chat.server.util.MessageUtil
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.util.CharsetUtil

class Application {

    static void main(String[] a) {
        EventLoopGroup workers = new NioEventLoopGroup()

        Bootstrap bootstrap = new Bootstrap()
        bootstrap.group(workers)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(Data.Message.defaultInstance))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                        .addLast(new ClientHandler())
            }
        })
        def channel = bootstrap.connect("localhost", 9001).sync().channel()
        for(;;) {
            def data = "${System.in.newReader().readLine()}"
            if (data == "close") {
                break;
            }
            if (data.startsWith("\$set:")) {
                systemCommand(data.substring(5), channel)
            } else if ("who".equalsIgnoreCase(data)){
                channel.writeAndFlush(MessageUtil.generateSysCmd(Data.SystemMsg.Option.WHO))
            }else {
                String[] fields = data.split("\\|#\\|")
                if (fields.length == 1) {
                    channel.writeAndFlush(MessageUtil.generateChatMsg("", data, "all"))
                } else if (fields.length == 2) {
                    channel.writeAndFlush(MessageUtil.generateChatMsg("", fields[1], fields[0]))
                }
            }
        }
        channel.close()
        workers.shutdownGracefully()
    }

    static void systemCommand(String s, Channel ch) {
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
                        ch.writeAndFlush(MessageUtil.generateSysCmd(Data.SystemMsg.Option.NAME, attrs[1]))
                        break;
                }
        }
    }
}