package com.xsy.chat.client

import com.xsy.chat.server.ServerHandler
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
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
                        .addLast(new StringEncoder(CharsetUtil.UTF_8))
                        .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                        .addLast(new StringDecoder(CharsetUtil.UTF_8))
                        .addLast(new ClientHandler())
            }
        })
        def channel = bootstrap.connect("140.82.4.127", 9000).sync().channel()
        for(;;) {
            def data = "${System.in.newReader().readLine()}"
            if (data == "close") {
                break;
            }
            channel.writeAndFlush("${data}\r\n")
        }
        channel.close()
    }

}