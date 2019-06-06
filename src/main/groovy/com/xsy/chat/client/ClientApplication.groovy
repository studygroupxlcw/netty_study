package com.xsy.chat.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.util.CharsetUtil

class ClientApplication {

    static void main(String[] a) {
        EventLoopGroup workers = new NioEventLoopGroup()

        Bootstrap clientBootstrap = new Bootstrap()
        clientBootstrap.group(workers)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                        .addLast(new StringDecoder(CharsetUtil.UTF_8))
                        .addLast(new StringEncoder(CharsetUtil.UTF_8))
                        .addLast(new ClientHandler())
            }
        })

        def channelFuture = clientBootstrap.connect("140.82.4.127", 9001).sync().channel()
        while (true) {
            String message = System.in.newReader().readLine()
            if ("close" == message) {
                break
            }
            channelFuture.writeAndFlush(message + "\r\n")

        }
        channelFuture.close()
    }

}
