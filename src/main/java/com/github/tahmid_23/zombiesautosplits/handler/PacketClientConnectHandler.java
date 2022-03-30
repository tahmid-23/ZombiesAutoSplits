package com.github.tahmid_23.zombiesautosplits.handler;

import com.github.tahmid_23.zombiesautosplits.netty.NettyPacketHandler;
import com.github.tahmid_23.zombiesautosplits.packet.PacketInterceptor;
import io.netty.channel.ChannelHandler;

import java.util.Objects;

public class PacketClientConnectHandler extends ClientConnectHandler {

    private final Iterable<PacketInterceptor> interceptors;

    public PacketClientConnectHandler(Iterable<PacketInterceptor> interceptors) {
        this.interceptors = Objects.requireNonNull(interceptors, "interceptors");
    }

    @Override
    protected ChannelHandler createChannelHandler() {
        return new NettyPacketHandler(interceptors);
    }

}
