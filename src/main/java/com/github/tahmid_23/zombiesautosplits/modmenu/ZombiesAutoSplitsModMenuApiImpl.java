package com.github.tahmid_23.zombiesautosplits.modmenu;

import com.github.tahmid_23.zombiesautosplits.ZombiesAutoSplits;
import com.github.tahmid_23.zombiesautosplits.config.ZombiesAutoSplitsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.LiteralText;

public class ZombiesAutoSplitsModMenuApiImpl implements ModMenuApi {

    private final ConfigScreenFactory<?> screenFactory = screen -> {
        ZombiesAutoSplits autoSplits = ZombiesAutoSplits.getInstance();
        ZombiesAutoSplitsConfig autoSplitsConfig = autoSplits.getConfig();
        ZombiesAutoSplitsConfig.Builder autoSplitsConfigBuilder = autoSplitsConfig.toBuilder();

        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new LiteralText("Zombies AutoSplits Config"));

        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();
        ConfigCategory main = configBuilder.getOrCreateCategory(new LiteralText("Config"));
        main.addEntry(entryBuilder.startStrField(new LiteralText("Host"), autoSplitsConfig.host())
                .setDefaultValue(ZombiesAutoSplitsConfig.DEFAULT_HOST)
                .setTooltip(new LiteralText("The host of the LiveSplits server. Most likely localhost."))
                .setSaveConsumer(autoSplitsConfigBuilder::setHost)
                .build());
        main.addEntry(entryBuilder.startIntField(new LiteralText("Port"), autoSplitsConfig.port())
                .setDefaultValue(ZombiesAutoSplitsConfig.DEFAULT_PORT)
                .setMin(1)
                .setMax(65535)
                .setTooltip(new LiteralText("The port of the LiveSplits server. Use -1 for the internal splitter."))
                .setSaveConsumer(autoSplitsConfigBuilder::setPort)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Use LiveSplits"), autoSplitsConfig.useLiveSplits())
                .setDefaultValue(ZombiesAutoSplitsConfig.DEFAULT_USE_LIVE_SPLITS)
                .setTooltip(new LiteralText("Whether to use the LiveSplits splitter."))
                .setSaveConsumer(autoSplitsConfigBuilder::setUseLiveSplits)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Use Internal"), autoSplitsConfig.useInternal())
                .setDefaultValue(ZombiesAutoSplitsConfig.DEFAULT_USE_INTERNAL)
                .setTooltip(new LiteralText("Whether to use the internal splitter."))
                .setSaveConsumer(autoSplitsConfigBuilder::setUseInternal)
                .build());

        configBuilder.setSavingRunnable(() -> {
            autoSplits.setConfig(autoSplitsConfigBuilder.build());
            autoSplits.saveConfig();
        });

        return configBuilder.build();
    };

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screenFactory;
    }
}
