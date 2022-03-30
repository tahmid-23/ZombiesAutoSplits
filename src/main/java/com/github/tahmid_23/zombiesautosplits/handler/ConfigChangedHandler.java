package com.github.tahmid_23.zombiesautosplits.handler;

import com.github.tahmid_23.zombiesautosplits.ZombiesAutoSplits;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class ConfigChangedHandler {

    private final ZombiesAutoSplits zombiesAutoSplits;

    public ConfigChangedHandler(ZombiesAutoSplits zombiesAutoSplits) {
        this.zombiesAutoSplits = Objects.requireNonNull(zombiesAutoSplits, "zombiesAutoSplits");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ZombiesAutoSplits.MODID)) {
            zombiesAutoSplits.reloadConfig();
        }
    }

}
