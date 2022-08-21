package com.github.tahmid_23.zombiesautosplits.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundInstance;

public final class ClientSoundEvents {

    public static final Event<PlaySound> PLAY_SOUND = EventFactory.createArrayBacked(PlaySound.class, callbacks -> (sound) -> {
        for (PlaySound callback : callbacks) {
            callback.onPlaySound(sound);
        }
    });

    private ClientSoundEvents() {
        throw new UnsupportedOperationException();
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface PlaySound {

        void onPlaySound(SoundInstance sound);

    }

}
