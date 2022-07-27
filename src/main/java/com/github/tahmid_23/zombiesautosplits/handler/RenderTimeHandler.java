package com.github.tahmid_23.zombiesautosplits.handler;

import com.github.tahmid_23.zombiesautosplits.splitter.internal.InternalSplitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class RenderTimeHandler {

    private final Minecraft minecraft;

    private final FontRenderer fontRenderer;

    private InternalSplitter internalSplitter;

    private final int color;

    public RenderTimeHandler(Minecraft minecraft, FontRenderer fontRenderer, int color) {
        this.minecraft = Objects.requireNonNull(minecraft, "minecraft");
        this.fontRenderer = Objects.requireNonNull(fontRenderer, "fontRenderer");
        this.color = color;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (internalSplitter == null) {
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            long millis = internalSplitter.getMillis();
            long minutesPart = millis / 60000;
            long secondsPart = (millis % 60000) / 1000;
            long tenthSecondsPart = (millis % 1000) / 100;
            String time = String.format("%d:%02d:%d", minutesPart, secondsPart, tenthSecondsPart);
            int width = fontRenderer.getStringWidth(time);
            ScaledResolution scaledResolution = new ScaledResolution(minecraft);
            int screenWidth = scaledResolution.getScaledWidth();
            int screenHeight = scaledResolution.getScaledHeight();
            fontRenderer.drawString(time, screenWidth - width, screenHeight - fontRenderer.FONT_HEIGHT, color);
        }
    }

    public void setSplitter(InternalSplitter internalSplitter) {
        this.internalSplitter = Objects.requireNonNull(internalSplitter, "internalSplitter");
    }

}
