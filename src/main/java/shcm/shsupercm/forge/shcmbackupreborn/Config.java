package shcm.shsupercm.forge.shcmbackupreborn;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@net.minecraftforge.common.config.Config(modid = SHCMBackupReborn.MODID)
public class Config {
    @net.minecraftforge.common.config.Config.Name("Archive save path")
    @net.minecraftforge.common.config.Config.Comment({
            "If a world has chosen \'archive_on_threshold\' as the trim,",
            "behavior once it has reached the limit of backups all backups",
            "would be zipped up and moved to this path. In addition, the",
            "file \"[the path]\\archive.bat\" will run if it exists."})
    public static String archive_save_path = "C:\\Archived Minecraft Backups";

    @SubscribeEvent
    public static void SyncConfig(ConfigChangedEvent event) {
        if(event.getModID().equals(SHCMBackupReborn.MODID))
            ConfigManager.sync(SHCMBackupReborn.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    }
}
