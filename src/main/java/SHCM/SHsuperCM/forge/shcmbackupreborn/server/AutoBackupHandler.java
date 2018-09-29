package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class AutoBackupHandler {
    public static WorldProfile currentWorldProfile = null;
    public static long lastbackup = -1;

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if(lastbackup != -1 && currentWorldProfile != null && currentWorldProfile.autoBackupInterval > 0 && System.currentTimeMillis() - lastbackup >= currentWorldProfile.autoBackupInterval)
            BackupsHandler.backup(currentWorldProfile.directory,"gui.shcmbackupreborn.scheduled", true);
    }
}
