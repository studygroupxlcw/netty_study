package com.xsy.chat.server

import com.xsy.chat.data.Data
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
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
        EventLoopGroup boss = new NioEventLoopGroup()
        EventLoopGroup workers = new NioEventLoopGroup()

        ServerBootstrap serverBootstrap = new ServerBootstrap()
        serverBootstrap.group(boss, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new ProtobufVarint32FrameDecoder())
                        .addLast(new ProtobufDecoder(Data.Message.defaultInstance))
                        .addLast(new ProtobufVarint32LengthFieldPrepender())
                        .addLast(new ProtobufEncoder())
                        .addLast(new ServerHandler())
            }
        })

        def channelFuture = serverBootstrap.bind(9001).sync()
        channelFuture.channel().closeFuture().sync()
    }

}
