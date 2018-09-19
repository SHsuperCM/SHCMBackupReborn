package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.Reference;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class WorldEvents {
    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if(event.getWorld().isRemote || !event.getWorld().provider.isSurfaceWorld()) return;
        File dir = event.getWorld().getSaveHandler().getWorldDirectory();
        File wpFile = new File(dir + Reference.PATH_WORLDPROFILE);
        wpFile.getParentFile().mkdirs();
    }
}
