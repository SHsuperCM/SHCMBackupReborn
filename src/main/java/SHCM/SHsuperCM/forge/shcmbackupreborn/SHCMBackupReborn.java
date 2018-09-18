package SHCM.SHsuperCM.forge.shcmbackupreborn;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
}
