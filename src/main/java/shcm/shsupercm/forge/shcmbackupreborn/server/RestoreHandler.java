package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import shcm.shsupercm.forge.shcmbackupreborn.client.ClientProxy;
import shcm.shsupercm.forge.shcmbackupreborn.client.gui.GuiRestore;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.FileUtils;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.Reference;

import java.io.File;
import java.io.IOException;

public class RestoreHandler {
    private static volatile Thread threadRestore;
    private static volatile int running = 0,
                                curProgress = 0,
                                maxProgress = 0;
    private static volatile File worldDirectory = null;
    private static volatile String backup = null,
                                   curFile = "";

    public static synchronized void tryRestore(boolean running, File worldDirectory, String backup) throws AssertionError{
        assert (RestoreHandler.worldDirectory != null && RestoreHandler.backup != null) || new File(worldDirectory,Reference.PATH_ROOT_BACKUPS + File.separatorChar + backup).exists();
        if (running) {
            if (SHCMBackupReborn.PROXY instanceof ServerProxy) {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                for(EntityPlayerMP player : playerList.getPlayers())
                    player.connection.disconnect(new TextComponentTranslation("misc.shcmbackupreborn.serverrestorekick"));

                RestoreHandler.worldDirectory = worldDirectory;
                RestoreHandler.backup = backup;

                FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
            } else { // isClient
                Minecraft.getMinecraft().loadWorld(null);
                tryRestore(false,worldDirectory,backup);
            }
        } else { // !running
            if(worldDirectory == null && backup == null) {
                if(RestoreHandler.worldDirectory == null || RestoreHandler.backup == null)
                    return;
            } else {
                RestoreHandler.worldDirectory = worldDirectory;
                RestoreHandler.backup = backup;
            }
            threadRestore = new Thread(() -> {
                RestoreHandler.running = 1;

                RestoreHandler.curProgress = 1;
                RestoreHandler.maxProgress = 0;
                RestoreHandler.curFile = "deleting";

                FileUtils.delete(RestoreHandler.worldDirectory,false,file -> !file.getAbsolutePath().endsWith(Reference.PATH_ROOT_BACKUPS));

                try {
                    FileUtils.unzip(new File(RestoreHandler.worldDirectory,Reference.PATH_ROOT_BACKUPS + File.separatorChar + RestoreHandler.backup),RestoreHandler.worldDirectory,(progress,maxprogress,file) -> {
                        RestoreHandler.curProgress = progress;
                        RestoreHandler.maxProgress = maxprogress;
                        RestoreHandler.curFile = file;
                    });
                    RestoreHandler.curProgress = 1;
                    RestoreHandler.maxProgress = 0;
                    RestoreHandler.curFile = "";
                    RestoreHandler.running = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    RestoreHandler.running = -1;
                }

                RestoreHandler.worldDirectory = null;
                RestoreHandler.threadRestore = null;
                RestoreHandler.backup = null;
            });
            threadRestore.start();
            if (SHCMBackupReborn.PROXY instanceof ClientProxy) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRestore());
            } else {
                while (RestoreHandler.running == 1 && !threadRestore.isInterrupted()) try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
        }
    }
}