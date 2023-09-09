package com.github.tahmid_23.zombiesautosplits.packet;

import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
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

    private boolean AAr10 = false;

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
                || soundEffect.getSoundName().equals("mob.enderdragon.end")
                || (soundEffect.getSoundName().equals("mob.guardian.curse") && !AAr10)) {
            AAr10 = soundEffect.getSoundName().equals("mob.guardian.curse");
            splitter.startOrSplit().exceptionally(throwable -> {
                logger.warn("Failed to split", throwable);
                minecraft.addScheduledTask(() -> {
                    if (minecraft.thePlayer != null) {
                        IChatComponent message = new ChatComponentText("Failed to split!");
                        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                        minecraft.thePlayer.addChatComponentMessage(message);
                    }
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
