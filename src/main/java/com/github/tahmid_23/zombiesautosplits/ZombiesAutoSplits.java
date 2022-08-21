package com.github.tahmid_23.zombiesautosplits;

import com.github.steanky.ethylene.codec.yaml.YamlCodec;
import com.github.steanky.ethylene.core.BasicConfigHandler;
import com.github.steanky.ethylene.core.ConfigHandler;
import com.github.steanky.ethylene.core.codec.ConfigCodec;
import com.github.steanky.ethylene.core.processor.SyncFileConfigLoader;
import com.github.tahmid_23.zombiesautosplits.config.ZombiesAutoSplitsConfig;
import com.github.tahmid_23.zombiesautosplits.config.ZombiesAutoSplitsConfigProcessor;
import com.github.tahmid_23.zombiesautosplits.event.ClientSoundEvents;
import com.github.tahmid_23.zombiesautosplits.tick.KeyInputHandler;
import com.github.tahmid_23.zombiesautosplits.packet.AutoSplitSoundInterceptor;
import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;
import com.github.tahmid_23.zombiesautosplits.splitter.internal.InternalSplitter;
import com.github.tahmid_23.zombiesautosplits.splitter.socket.LiveSplitSocketSplitter;
import com.minenash.customhud.HudElements.supplier.StringSupplierElement;
import com.minenash.customhud.mod_compat.CustomHudRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ZombiesAutoSplits implements ClientModInitializer {

    public static final String MODID = "zombiesautosplits";

    private static ZombiesAutoSplits instance = null;

    private final KeyBinding autoSplitsKeybind = new KeyBinding("Toggle AutoSplits", GLFW.GLFW_KEY_SEMICOLON,
            "Tahmid's Mods");

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final Logger logger = LoggerFactory.getLogger(MODID);

    private final ConfigHandler configHandler = new BasicConfigHandler();

    private final ConfigHandler.ConfigKey<ZombiesAutoSplitsConfig> configKey = new ConfigHandler.ConfigKey<>(ZombiesAutoSplitsConfig.class, "zombiesautosplits_config");

    private final Collection<LiveSplitSplitter> splitters = new ArrayList<>(2);

    private InternalSplitter internalSplitter;

    private final AutoSplitSoundInterceptor soundInterceptor = new AutoSplitSoundInterceptor(MinecraftClient.getInstance(), logger, splitters);

    private ZombiesAutoSplitsConfig config;

    public static ZombiesAutoSplits getInstance() {
        return instance;
    }

    private CompletableFuture<ZombiesAutoSplitsConfig> loadConfigFromFile() {
        return configHandler.writeDefaults().thenCompose((unused) -> configHandler.loadData(configKey));
    }

    public ZombiesAutoSplitsConfig getConfig() {
        return config;
    }

    public void setConfig(ZombiesAutoSplitsConfig config) {
        for (LiveSplitSplitter splitter : splitters) {
            splitter.cancel();
        }
        splitters.clear();
        internalSplitter = null;

        this.config = config;

        if (config.useLiveSplits()) {
            splitters.add(new LiveSplitSocketSplitter(executor, config.host(), config.port()));
        }
        if (config.useInternal()) {
            splitters.add(internalSplitter = new InternalSplitter(executor));
        }
    }

    public void saveConfig() {
        configHandler.writeData(configKey, config).exceptionally(throwable -> {
            logger.error("Failed to save config", throwable);
            return null;
        });
    }

    @Override
    public void onInitializeClient() {
        ConfigCodec codec = new YamlCodec();
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("zombiesautosplits");
        String configFileName;
        if (codec.getPreferredExtensions().isEmpty()) {
            configFileName = "config";
        }
        else {
            configFileName = "config." + codec.getPreferredExtensions().get(0);
        }
        try {
            Files.createDirectories(configPath);
        } catch (IOException e) {
            logger.error("Failed to create config directory", e);
            return;
        }
        configPath = configPath.resolve(configFileName);

        ZombiesAutoSplitsConfig defaultConfig = new ZombiesAutoSplitsConfig(ZombiesAutoSplitsConfig.DEFAULT_HOST, ZombiesAutoSplitsConfig.DEFAULT_PORT, ZombiesAutoSplitsConfig.DEFAULT_USE_LIVE_SPLITS, ZombiesAutoSplitsConfig.DEFAULT_USE_INTERNAL);
        configHandler.registerLoader(configKey, new SyncFileConfigLoader<>(new ZombiesAutoSplitsConfigProcessor(), defaultConfig, configPath, codec));

        ZombiesAutoSplitsConfig loadedConfig;
        try {
            loadedConfig = loadConfigFromFile().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Failed to load config", e);
            return;
        }
        setConfig(loadedConfig);

        ClientTickEvents.END_CLIENT_TICK.register(new KeyInputHandler(autoSplitsKeybind, soundInterceptor));
        ClientSoundEvents.PLAY_SOUND.register(soundInterceptor);

        KeyBindingHelper.registerKeyBinding(autoSplitsKeybind);

        CustomHudRegistry.registerElement("zombies_autosplits_timer", (_str) -> new StringSupplierElement(() -> {
            if (internalSplitter == null) {
                return "N/A";
            }

            long millis = internalSplitter.getMillis();
            long minutesPart = millis / 60000;
            long secondsPart = (millis % 60000) / 1000;
            long tenthSecondsPart = (millis % 1000) / 100;
            return String.format("%d:%02d:%d", minutesPart, secondsPart, tenthSecondsPart);
        }));

        instance = this;
    }
}
