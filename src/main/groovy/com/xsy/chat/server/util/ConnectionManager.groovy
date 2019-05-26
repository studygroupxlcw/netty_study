package com.xsy.chat.server.util

import io.netty.channel.Channel
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.ChannelMatcher
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor

@Singleton
class ConnectionManager {

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    void sendToAll(String msg) {
        channelGroup.writeAndFlush(msg)
    }

    void sendToOther(String msg, Channel ch) {
        ChannelMatcher matcher = new ChannelMatcher() {
            @Override
            boolean matches(Channel channel) {
                return ch != channel
            }
        }

        channelGroup.writeAndFlush(msg, matcher)
    }

    void addChannel(Channel ch) {
        channelGroup.add(ch)
    }

    List<String> getAllConnection() {
        List<String> connections = []
        channelGroup.each {
            connections.add(it.remoteAddress().toString())
        }

        return connections
    }
}
