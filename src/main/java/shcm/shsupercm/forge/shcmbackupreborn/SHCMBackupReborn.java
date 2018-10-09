package shcm.shsupercm.forge.shcmbackupreborn;

import net.minecraftforge.fml.server.FMLServerHandler;
import shcm.shsupercm.forge.shcmbackupreborn.common.storage.WorldProfile;
import shcm.shsupercm.forge.shcmbackupreborn.server.BackupsHandler;
import shcm.shsupercm.forge.shcmbackupreborn.server.command.CommandBackup;
import shcm.shsupercm.forge.shcmbackupreborn.server.command.CommandDebug;
import shcm.shsupercm.forge.shcmbackupreborn.server.command.CommandSHCMBackupReborn;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Mod(modid = SHCMBackupReborn.MODID)
public class SHCMBackupReborn {
    public static final String MODID = "shcmbackupreborn";
    public static Logger logger;

    @SidedProxy(clientSide = "shcm.shsupercm.forge." + MODID + ".client.ClientProxy", serverSide = "shcm.shsupercm.forge." + MODID + ".server.ServerProxy")
    public static CommonProxy PROXY;

    @Mod.Instance
    public static SHCMBackupReborn INSTANCE;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBackup());
        event.registerServerCommand(new CommandSHCMBackupReborn());
        event.registerServerCommand(new CommandDebug());
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartedEvent event) {
        File directory = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName());

        WorldProfile.currentWorldProfile = BackupsHandler.validateWorldProfile(directory);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        File directory = new File(FMLCommonHandler.instance().getSavesDirectory(), event.getServer().getFolderName());


    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        WorldProfile.currentWorldProfile = null;
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppedEvent event) {
        if(CommandDebug.shouldRestart) {
            CommandDebug.shouldRestart = false;
            try {
                FMLServerHandler.instance().getServer().init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
