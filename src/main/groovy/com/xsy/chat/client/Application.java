package com.xsy.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                                  @Override
                                  protected void initChannel(SocketChannel ch) throws Exception {
                                      ch.pipeline()
                                              .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                                              .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                              .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                              .addLast(new ClientHandler());
                                  }
                              }
                );

        Channel channel = bootstrap.connect("140.82.4.127", 9001).sync().channel();


        BufferedReader to_server_in = new BufferedReader(new InputStreamReader(System.in));

        boolean flag = true;
        while (flag){
            String string = to_server_in.readLine();
            if ("close" == string){
                break;
            }
            channel.writeAndFlush(string + "\r\n");
        }

        to_server_in.close();
        channel.closeFuture().sync();
    }
}
