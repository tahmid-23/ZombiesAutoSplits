package com.github.tahmid_23.zombiesautosplits.packet;

import net.minecraft.network.Packet;

import java.util.function.Consumer;

@FunctionalInterface
public interface PacketInterceptor extends Consumer<Packet<?>> {

    void intercept(Packet<?> packet);

    default void accept(Packet<?> packet) {
        intercept(packet);
    }

}
