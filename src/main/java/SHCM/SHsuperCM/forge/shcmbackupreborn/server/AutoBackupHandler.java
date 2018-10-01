package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class AutoBackupHandler {
    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if(WorldProfile.currentWorldProfile != null && WorldProfile.currentWorldProfile.lastBackup != -1 && WorldProfile.currentWorldProfile.autoBackupInterval > 0 && System.currentTimeMillis() - WorldProfile.currentWorldProfile.lastBackup >= WorldProfile.currentWorldProfile.autoBackupInterval)
            BackupsHandler.backup(WorldProfile.currentWorldProfile.file.getParentFile().getParentFile(),"gui.shcmbackupreborn.scheduled");
    }
}
