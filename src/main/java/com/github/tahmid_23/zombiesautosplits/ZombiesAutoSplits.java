package com.github.tahmid_23.zombiesautosplits;

import com.github.tahmid_23.zombiesautosplits.handler.ConfigChangedHandler;
import com.github.tahmid_23.zombiesautosplits.handler.ConnectionEstablishedHandler;
import com.github.tahmid_23.zombiesautosplits.handler.KeyInputHandler;
import com.github.tahmid_23.zombiesautosplits.netty.NettyPacketHandler;
import com.github.tahmid_23.zombiesautosplits.packet.AutoSplitPacketInterceptor;
import com.github.tahmid_23.zombiesautosplits.packet.PacketInterceptor;
import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;
import com.github.tahmid_23.zombiesautosplits.splitter.socket.LiveSplitSocketSplitter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Mod(modid = ZombiesAutoSplits.MODID, name = "Zombies AutoSplits", version = "1.0", clientSideOnly = true,
guiFactory = "com.github.tahmid_23.zombiesautosplits.gui.ZombiesAutoSplitsGuiFactory")
public class ZombiesAutoSplits {

    public static final String MODID = "zombiesautosplits";

    public static ZombiesAutoSplits instance;

    private final KeyBinding autoSplitsKeybind = new KeyBinding("Toggle AutoSplits", Keyboard.KEY_SEMICOLON,
            "Tahmid's Mods");

    private Executor executor;

    private AutoSplitPacketInterceptor packetInterceptor;

    private Configuration config;

    public static ZombiesAutoSplits getInstance() {
        return instance;
    }

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        executor = Executors.newSingleThreadExecutor();

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        reloadConfig();
    }

    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        Iterable<PacketInterceptor> interceptors = Collections.singleton(packetInterceptor);
        MinecraftForge.EVENT_BUS.register(new ConnectionEstablishedHandler() {
            @Override
            protected ChannelHandler createChannelHandler() {
                return new NettyPacketHandler(interceptors);
            }
        });
        MinecraftForge.EVENT_BUS.register(new ConfigChangedHandler(this));
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler(Minecraft.getMinecraft(), autoSplitsKeybind,
                packetInterceptor));

        ClientRegistry.registerKeyBinding(autoSplitsKeybind);

        instance = this;
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        config.save();

        String host = config.getString("host", Configuration.CATEGORY_GENERAL, "localhost",
                "The local IP to connect to LiveSplits");
        int port = config.getInt("port", Configuration.CATEGORY_GENERAL, 16384, 0,
                65535, "The port to connect to LiveSplits");

        LiveSplitSplitter splitter = new LiveSplitSocketSplitter(executor, host, port);
        if (packetInterceptor != null) {
            packetInterceptor.setSplitter(splitter);
        }
        else {
            packetInterceptor = new AutoSplitPacketInterceptor(Minecraft.getMinecraft(), splitter);
        }
    }

}
