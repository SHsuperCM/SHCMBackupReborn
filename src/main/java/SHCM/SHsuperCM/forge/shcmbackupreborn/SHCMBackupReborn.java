package SHCM.SHsuperCM.forge.shcmbackupreborn;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.AutoBackupHandler;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.BackupsHandler;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.command.CommandBackup;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.command.CommandDebug;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.command.CommandSHCMBackupReborn;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = SHCMBackupReborn.MODID)
public class SHCMBackupReborn {
    public static final String MODID = "shcmbackupreborn";
    public static Logger logger;

    @SidedProxy(clientSide = "SHCM.SHsuperCM.forge." + MODID + ".client.ClientProxy", serverSide = "SHCM.SHsuperCM.forge." + MODID + ".server.ServerProxy")
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
        WorldProfile profile = BackupsHandler.validateWorldProfile(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getSaveHandler().getWorldDirectory());

        WorldProfile.currentWorldProfile = profile;
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        WorldProfile.currentWorldProfile = null;
    }
}
