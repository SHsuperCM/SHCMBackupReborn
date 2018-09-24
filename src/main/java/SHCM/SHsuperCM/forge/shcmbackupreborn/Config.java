package SHCM.SHsuperCM.forge.shcmbackupreborn;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@net.minecraftforge.common.config.Config(modid = SHCMBackupReborn.MODID)
public class Config {




    @SubscribeEvent
    public static void SyncConfig(ConfigChangedEvent event) {
        if(event.getModID().equals(SHCMBackupReborn.MODID))
            ConfigManager.sync(SHCMBackupReborn.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    }
}
