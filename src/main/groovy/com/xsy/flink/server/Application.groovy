package com.xsy.flink.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Application {

    static void main(String[] args) {
        new Thread(new SendTimeTask()).start()
        EventLoopGroup boss = new NioEventLoopGroup()
        EventLoopGroup workers = new NioEventLoopGroup(100)

        ServerBootstrap serverBootstrap = new ServerBootstrap()
        serverBootstrap.group(boss, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new StringEncoder(CharsetUtil.UTF_8))
                        .addLast(new StringDecoder(CharsetUtil.UTF_8))
                        .addLast(new ServerHandler())
            }
        })

        def channelFuture = serverBootstrap.bind(9009).sync()
        channelFuture.channel().closeFuture().sync()
    }

}
