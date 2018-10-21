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
import shcm.shsupercm.forge.shcmbackupreborn.common.storage.WorldProfile;

import java.io.File;

public class RestoreHandler {
    private static volatile Thread threadRestore;
    private static volatile boolean running = false;
    private static volatile File worldDirectory;
    private static volatile String backup;

    public static void tryRestore(boolean running, File worldDirectory, String backup) {
        if (running) {
            if (SHCMBackupReborn.PROXY instanceof ServerProxy) {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                for(EntityPlayerMP player : playerList.getPlayers())
                    player.connection.disconnect(new TextComponentTranslation("misc.shcmbackupreborn.serverrestorekick"));

                WorldProfile.currentWorldProfile.restoreBackup = backup;
                WorldProfile.currentWorldProfile.writeFile();

                FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
            } else { // isClient
                Minecraft.getMinecraft().loadWorld(null);
                tryRestore(false,worldDirectory,backup);
            }
        } else { // !running
            threadRestore = new Thread(() -> {
                //todo HERE

                //FileUtils.unzip();
                FileUtils.delete();

                RestoreHandler.worldDirectory = null;
                RestoreHandler.running = false;
                RestoreHandler.threadRestore = null;
                RestoreHandler.backup = null;
            });
            RestoreHandler.worldDirectory = worldDirectory;
            RestoreHandler.running = true;
            RestoreHandler.backup = backup;
            threadRestore.start();
            if (SHCMBackupReborn.PROXY instanceof ClientProxy) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRestore());
            } else {
                while (RestoreHandler.running && !threadRestore.isInterrupted()) try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
        }
    }
}