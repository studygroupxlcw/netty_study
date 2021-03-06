package com.xsy.chat.server.util

import io.netty.channel.Channel
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.ChannelMatcher
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor

@Singleton
class ConnectionManager {

    // 属性名--属性值和客户端关联在一起
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    private Map<String, Map<String, String>> attrMap = new HashMap<>()

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

    void setAttrs(String name, String value, Channel ch) {
        if (!attrMap.containsKey(ch.remoteAddress().toString())) {
            attrMap.put(ch.remoteAddress().toString(), new HashMap<String, String>())
        }
        attrMap.get(ch.remoteAddress().toString()).put(name, value)
    }

    String getAttrs(String name, Channel ch) {
        if (attrMap.containsKey(ch.remoteAddress().toString())) {
            attrMap.get(ch.remoteAddress().toString()).get(name)
        }
        return null
    }
}
