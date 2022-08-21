package com.github.tahmid_23.zombiesautosplits.packet;

import com.github.tahmid_23.zombiesautosplits.event.ClientSoundEvents;
import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Objects;

public class AutoSplitSoundInterceptor implements ClientSoundEvents.PlaySound {

    private final MinecraftClient minecraft;

    private final Logger logger;

    private final Collection<LiveSplitSplitter> splitters;

    private boolean enabled = true;

    public AutoSplitSoundInterceptor(MinecraftClient minecraft, Logger logger, Collection<LiveSplitSplitter> splitters) {
        this.minecraft = Objects.requireNonNull(minecraft, "minecraft");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.splitters = Objects.requireNonNull(splitters, "splitters");
    }

    @Override
    public void onPlaySound(SoundInstance sound) {
        if (!enabled) {
            return;
        }

        Identifier identifier = sound.getId();
        if (identifier.equals(SoundEvents.ENTITY_WITHER_SPAWN.getId()) || identifier.equals(SoundEvents.ENTITY_ENDER_DRAGON_DEATH.getId())) {
            for (LiveSplitSplitter splitter : splitters) {
                splitter.startOrSplit().exceptionally(throwable -> {
                    logger.warn("Failed to split", throwable);
                    minecraft.execute(() -> {
                        if (minecraft.player != null) {
                            BaseText message = new LiteralText("Failed to split!");
                            message.formatted(Formatting.RED);
                            minecraft.player.sendMessage(message, false);
                        }
                    });

                    return null;
                });
            }
        }
    }

    public boolean toggle() {
        return enabled = !enabled;
    }

}
