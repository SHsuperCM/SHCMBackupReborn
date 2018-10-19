package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import shcm.shsupercm.forge.shcmbackupreborn.client.ClientProxy;
import shcm.shsupercm.forge.shcmbackupreborn.client.gui.GuiRestore;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.Reference;
import shcm.shsupercm.forge.shcmbackupreborn.common.storage.RestoreRequest;

import java.io.File;

public class RestoreHandler {
    private static volatile Thread threadRestore;
    private static volatile boolean running = false;
    private static volatile File worldDirectory;

    public static boolean tryRestore(boolean running, File worldDirectory, String backup) {
        if (running) {
            if (SHCMBackupReborn.PROXY instanceof ServerProxy) {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                for(EntityPlayerMP player : playerList.getPlayers())
                    player.connection.disconnect(new TextComponentTranslation("misc.shcmbackupreborn.serverrestorekick"));

                new RestoreRequest(backup).writeFile(new File(worldDirectory, Reference.PATH_RESTOREREQUEST));

                FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();

                return true;
            } else { // isClient
                Minecraft.getMinecraft().loadWorld(null);
                return tryRestore(false,worldDirectory,backup);
            }
        } else { // !running
            threadRestore = new Thread(() -> {
                HERE
                RestoreHandler.worldDirectory = null;
                RestoreHandler.running = false;
                RestoreHandler.threadRestore = null;
            });
            RestoreHandler.worldDirectory = worldDirectory;
            RestoreHandler.running = true;
            threadRestore.start();
            if (SHCMBackupReborn.PROXY instanceof ClientProxy) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRestore());
            } else {
                while (running && !threadRestore.isInterrupted()) try {
                    Thread.sleep(100);
                } catch (InterruptedException e) { }
            }
        }
        return false;
    }
}