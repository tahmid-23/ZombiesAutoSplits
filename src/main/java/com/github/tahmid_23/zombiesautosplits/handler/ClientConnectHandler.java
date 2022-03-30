package com.github.tahmid_23.zombiesautosplits.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public abstract class ClientConnectHandler {

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ChannelPipeline pipeline = event.manager.channel().pipeline();
        pipeline.addBefore("packet_handler", "zombies_auto_splits", createChannelHandler());
    }

    protected abstract ChannelHandler createChannelHandler();

}
