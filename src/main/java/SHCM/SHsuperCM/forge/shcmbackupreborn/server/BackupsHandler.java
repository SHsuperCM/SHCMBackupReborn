package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import SHCM.SHsuperCM.forge.shcmbackupreborn.SHCMBackupReborn;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.misc.Reference;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.misc.FileUtils;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

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
        worldProfile.writeFile(fileWorldProfile);

        return worldProfile;
    }

    public static boolean backup(File directory, String comment) throws InvalidPathException {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        long start = System.currentTimeMillis();
        boolean ingame = WorldProfile.currentWorldProfile != null;

        if(ingame) {
            server.getPlayerList().sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.backup.startbackup"));

            server.getPlayerList().saveAllPlayerData();

            oldSaveStates = new boolean[server.worlds.length];

            for (int i = 0; i < oldSaveStates.length; i++) {
                WorldServer worldServer = server.worlds[i];
                if (worldServer == null) continue;

                oldSaveStates[i] = worldServer.disableLevelSaving;

                worldServer.disableLevelSaving = true;

                try {
                    worldServer.saveAllChunks(true, null);
                    worldServer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        long datetimeEpoch = System.currentTimeMillis();
        String datetime = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        File backupDestination = new File(directory, Reference.PATH_ROOT_BACKUPS + "\\" + datetime + "_" + comment);

        try {
            backupDestination.toPath();
        } catch (InvalidPathException e) {
            if(ingame) {
                for (int i = 0; i < oldSaveStates.length; i++) {
                    WorldServer worldServer = server.worlds[i];

                    if (worldServer != null)
                        worldServer.disableLevelSaving = oldSaveStates[i];
                }
            }
            throw e;
        }

        backupDestination.delete();

        boolean o = FileUtils.zip(directory,backupDestination, false, file -> !file.getAbsolutePath().endsWith(Reference.PATH_ROOT_BACKUPS));

        if(o)
            SHCMBackupReborn.logger.info("Backed up the world to " + backupDestination.getAbsolutePath());

        WorldProfile worldProfile = new WorldProfile();
        worldProfile.readFile(new File(directory,Reference.PATH_WORLDPROFILE));
        worldProfile.lastBackup = datetimeEpoch;
        worldProfile.writeFile();

        if(ingame) {
            WorldProfile.currentWorldProfile = worldProfile;
            for (int i = 0; i < oldSaveStates.length; i++) {
                WorldServer worldServer = server.worlds[i];

                if (worldServer != null)
                    worldServer.disableLevelSaving = oldSaveStates[i];
            }
            server.getPlayerList().sendMessage(o ? new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackup", (System.currentTimeMillis() - start), datetime, comment.equals("gui.shcmbackupreborn.scheduled") ? I18n.format("gui.shcmbackupreborn.scheduled") : comment) : new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackupfailed"));
        }
        return o;
    }

    private static boolean[] oldSaveStates;
}
