package com.github.tahmid_23.zombiesautosplits.handler;

import com.github.tahmid_23.zombiesautosplits.ZombiesAutoSplits;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Objects;

public class ConnectionEstablishedHandler {

    private final ChannelHandler channelHandler;

    public ConnectionEstablishedHandler(ChannelHandler channelHandler) {
        this.channelHandler = Objects.requireNonNull(channelHandler, "channelHandler");
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ChannelPipeline pipeline = event.manager.channel().pipeline();
        pipeline.addBefore("packet_handler", "zombies_auto_splits", channelHandler);
    }

}
