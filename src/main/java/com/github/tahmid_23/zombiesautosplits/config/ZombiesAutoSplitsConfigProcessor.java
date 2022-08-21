package com.github.tahmid_23.zombiesautosplits.config;

import com.github.steanky.ethylene.core.ConfigElement;
import com.github.steanky.ethylene.core.collection.ConfigNode;
import com.github.steanky.ethylene.core.collection.LinkedConfigNode;
import com.github.steanky.ethylene.core.processor.ConfigProcessException;
import com.github.steanky.ethylene.core.processor.ConfigProcessor;
import org.jetbrains.annotations.NotNull;

public class ZombiesAutoSplitsConfigProcessor implements ConfigProcessor<ZombiesAutoSplitsConfig> {
    @Override
    public @NotNull ZombiesAutoSplitsConfig dataFromElement(@NotNull ConfigElement element) throws ConfigProcessException {
        String host = element.getStringOrThrow("host");
        int port = element.getNumberOrThrow("port").intValue();
        if (port < 1 || port > 65535) {
            port = ZombiesAutoSplitsConfig.DEFAULT_PORT;
        }
        boolean useLiveSplits = element.getBooleanOrThrow("useLiveSplits");
        boolean useInternal = element.getBooleanOrThrow("useInternal");

        return new ZombiesAutoSplitsConfig(host, port, useLiveSplits, useInternal);
    }

    @Override
    public @NotNull ConfigElement elementFromData(@NotNull ZombiesAutoSplitsConfig config) {
        ConfigNode node = new LinkedConfigNode(4);
        node.putString("host", config.host());
        node.putNumber("port", config.port());
        node.putBoolean("useLiveSplits", config.useLiveSplits());
        node.putBoolean("useInternal", config.useInternal());

        return node;
    }
}
