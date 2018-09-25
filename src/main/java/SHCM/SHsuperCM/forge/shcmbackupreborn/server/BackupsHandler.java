package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.misc.Reference;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.misc.FileUtils;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;

public class BackupsHandler {

    public static WorldProfile validateWorldProfile(File world) {
        if(!world.exists()) return null;

        File backups = new File(world, Reference.PATH_ROOT_BACKUPS);

        if(!backups.exists())
            backups.mkdirs();

        File fileWorldProfile = new File(world,Reference.PATH_WORLDPROFILE);
        WorldProfile worldProfile = new WorldProfile();
        if(fileWorldProfile.exists())
            worldProfile.readFile(fileWorldProfile);
        worldProfile.writeToFile(fileWorldProfile);

        worldProfile.directory = fileWorldProfile;

        return worldProfile;
    }

    public static boolean backup(File directory, String comment, boolean ingame) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(ingame) {
            server.getPlayerList().sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.backup.startbackup"));

            server.getPlayerList().saveAllPlayerData();

            oldSaveStates = new boolean[server.worlds.length];

            for (int i = 0; i < oldSaveStates.length; i++) {
                WorldServer worldServer = server.worlds[i];
                if (worldServer == null) continue;

                oldSaveStates[i] = worldServer.disableLevelSaving;

                try {
                    worldServer.saveAllChunks(true, null);
                    worldServer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                worldServer.disableLevelSaving = true;
            }
        }

        File backupDestination = new File(directory, Reference.PATH_ROOT_BACKUPS + "\\" + new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date()) + "_" + comment);

        boolean o = FileUtils.zip(directory,backupDestination, false, file -> !file.getAbsolutePath().endsWith(Reference.PATH_ROOT_BACKUPS));


        if(ingame) {
            for (int i = 0; i < oldSaveStates.length; i++) {
                WorldServer worldServer = server.worlds[i];

                if (worldServer != null)
                    worldServer.disableLevelSaving = oldSaveStates[i];
            }
            server.getPlayerList().sendMessage(o ? new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackup",backupDestination.getName()) : new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackupfailed"));
        }
        return o;
    }

    private static boolean[] oldSaveStates;
}
