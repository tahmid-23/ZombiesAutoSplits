package com.github.tahmid_23.zombiesautosplits.netty;

import com.github.tahmid_23.zombiesautosplits.packet.PacketInterceptor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.Packet;

import java.util.Objects;

public class NettyPacketHandler extends SimpleChannelInboundHandler<Packet<?>> {

    private final Iterable<PacketInterceptor> interceptors;

    public NettyPacketHandler(Iterable<PacketInterceptor> interceptors) {
        this.interceptors = Objects.requireNonNull(interceptors, "interceptors");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> msg) {
        for (PacketInterceptor interceptor : interceptors) {
            interceptor.intercept(msg);
        }

        ctx.fireChannelRead(msg);
    }

}
