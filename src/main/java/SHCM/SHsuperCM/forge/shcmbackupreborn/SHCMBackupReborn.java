package SHCM.SHsuperCM.forge.shcmbackupreborn;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.BackupsHandler;
import SHCM.SHsuperCM.forge.shcmbackupreborn.server.commands.CommandBackup;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = SHCMBackupReborn.MODID)
public class SHCMBackupReborn {
    public static final String MODID = "shcmbackupreborn";

    @SidedProxy(clientSide = "SHCM.SHsuperCM.forge." + MODID + ".client.ClientProxy", serverSide = "SHCM.SHsuperCM.forge." + MODID + ".server.ServerProxy")
    public static CommonProxy PROXY;

    @Mod.Instance
    public static SHCMBackupReborn INSTANCE;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
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
    }
    @Mod.EventHandler
    public void init(FMLServerStartedEvent event) {
        WorldProfile profile = BackupsHandler.validateWorldProfile(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getSaveHandler().getWorldDirectory());
    }
}
