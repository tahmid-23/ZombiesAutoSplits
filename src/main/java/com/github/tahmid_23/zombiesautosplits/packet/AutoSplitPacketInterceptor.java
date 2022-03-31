package com.github.tahmid_23.zombiesautosplits.packet;

import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class AutoSplitPacketInterceptor implements PacketInterceptor {

    private final Minecraft minecraft;

    private final Logger logger;

    private LiveSplitSplitter splitter;

    private boolean enabled = true;

    public AutoSplitPacketInterceptor(Minecraft minecraft, Logger logger, LiveSplitSplitter splitter) {
        this.minecraft = Objects.requireNonNull(minecraft, "minecraft");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.splitter = Objects.requireNonNull(splitter, "splitter");
    }

    @Override
    public void intercept(Packet<?> packet) {
        if (!(enabled && packet instanceof S29PacketSoundEffect)) {
            return;
        }

        S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
        if (soundEffect.getSoundName().equals("mob.wither.spawn")
                || soundEffect.getSoundName().equals("mob.enderdragon.end")) {
            splitter.startOrSplit().exceptionally(throwable -> {
                logger.warn("Failed to split", throwable);
                minecraft.addScheduledTask(() -> {
                    IChatComponent message = new ChatComponentText("Failed to split!");
                    message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    minecraft.thePlayer.addChatComponentMessage(message);
                });

                return null;
            });
        }
    }

    public void setSplitter(LiveSplitSplitter splitter) {
        this.splitter = Objects.requireNonNull(splitter, "splitter");
    }

    public boolean toggle() {
        return enabled = !enabled;
    }

}
