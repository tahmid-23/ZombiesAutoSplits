package com.github.tahmid_23.zombiesautosplits.tick;

import com.github.tahmid_23.zombiesautosplits.packet.AutoSplitSoundInterceptor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class KeyInputHandler implements ClientTickEvents.EndTick {

    private final KeyBinding keyBinding;

    private final AutoSplitSoundInterceptor packetInterceptor;

    public KeyInputHandler(KeyBinding keyBinding, AutoSplitSoundInterceptor packetInterceptor) {
        this.keyBinding = Objects.requireNonNull(keyBinding, "keyBinding");
        this.packetInterceptor = Objects.requireNonNull(packetInterceptor, "packetInterceptor");
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (keyBinding.isPressed() ) {
            Text toggledComponent;
            boolean toggled = packetInterceptor.toggle();

            PlayerEntity player = client.player;
            if (player == null) {
                return;
            }

            if (toggled) {
                toggledComponent = new LiteralText("ON").formatted(Formatting.GREEN);
            }
            else {
                toggledComponent = new LiteralText("OFF").formatted(Formatting.RED);
            }

            Text message = new LiteralText("")
                    .formatted(Formatting.YELLOW)
                    .append("Toggled AutoSplits ")
                    .append(toggledComponent)
                    .append("!");
            client.player.sendMessage(message, false);
        }
    }


}
