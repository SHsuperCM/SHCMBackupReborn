package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import shcm.shsupercm.forge.shcmbackupreborn.client.ClientProxy;
import shcm.shsupercm.forge.shcmbackupreborn.client.gui.GuiRestore;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.FileUtils;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.Reference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestoreHandler {
    private static volatile Thread threadRestore;
    private static volatile int running = 0,
                                curProgress = 0,
                                maxProgress = 0;
    private static volatile File worldDirectory = null;
    private static volatile String backup = null,
                                   curFile = "";

    public static synchronized void tryRestore(boolean running, File worldDirectory, String backup) throws AssertionError {
        assert (RestoreHandler.worldDirectory != null && RestoreHandler.backup != null) || new File(worldDirectory,Reference.PATH_ROOT_BACKUPS + File.separatorChar + backup).exists();
        if (running) {
            if (SHCMBackupReborn.PROXY.getSide() == Side.SERVER) {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                for(EntityPlayerMP player : playerList.getPlayers())
                    player.connection.disconnect(new TextComponentTranslation("misc.shcmbackupreborn.serverrestorekick"));



                FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
            } else { // isClient
                RestoreHandler.worldDirectory = worldDirectory;
                RestoreHandler.backup = backup;
                Minecraft.getMinecraft().loadWorld(null);
                //tryRestore(false,worldDirectory,backup);
                //Todo fix creating the restore gui when working on ClientProxy from server thread
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
            if (SHCMBackupReborn.PROXY.getSide() == Side.SERVER) {
                while (RestoreHandler.running == 1 && !threadRestore.isInterrupted()) try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }

                if(RestoreHandler.running == -1)
                    SHCMBackupReborn.logger.error("There was an error while trying to restore the world from backups.");
            } else {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRestore());
            }
        }
    }

    public static List<String> getBackupsList(File worldDirectory) {
        List<String> list = new ArrayList<>();

        File[] files = new File(worldDirectory, Reference.PATH_ROOT_BACKUPS).listFiles();
        if (files != null)
            for (File file : files)
                if (!file.getAbsolutePath().endsWith(Reference.PATH_WORLDPROFILE))
                    list.add(file.getName());

        list.sort(String::compareTo);

        return list;
    }
}