package com.github.tahmid_23.zombiesautosplits.mixin;

import com.github.tahmid_23.zombiesautosplits.event.ClientSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SoundSystem.class)
abstract class SoundSystemMixin {

    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V")
    private void play(SoundInstance sound, CallbackInfo callbackInfo) {
        ClientSoundEvents.PLAY_SOUND.invoker().onPlaySound(sound);
    }

}
