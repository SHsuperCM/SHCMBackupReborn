package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shcm.shsupercm.forge.shcmbackupreborn.Config;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.Reference;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.FileUtils;
import shcm.shsupercm.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.nio.file.InvalidPathException;

@Mod.EventBusSubscriber
public class BackupsHandler {

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        AutoBackup.tick();
    }

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

    private static boolean[] oldSaveStates;

    public static boolean backup(File directory, String comment) throws InvalidPathException {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        long start = System.currentTimeMillis();
        boolean ingame = WorldProfile.currentWorldProfile != null;

        if (ingame) {
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
            if (ingame) {
                for (int i = 0; i < oldSaveStates.length; i++) {
                    WorldServer worldServer = server.worlds[i];

                    if (worldServer != null)
                        worldServer.disableLevelSaving = oldSaveStates[i];
                }
            }
            throw e;
        }

        backupDestination.delete();

        boolean o = FileUtils.zip(directory, backupDestination, false, file -> !file.getAbsolutePath().endsWith(Reference.PATH_ROOT_BACKUPS));

        if (o)
            SHCMBackupReborn.logger.info("Backed up the world to " + backupDestination.getAbsolutePath());

        WorldProfile worldProfile = new WorldProfile();
        worldProfile.readFile(new File(directory, Reference.PATH_WORLDPROFILE));
        worldProfile.lastBackup = datetimeEpoch;
        worldProfile.writeFile();

        File[] fArr = new File(directory, Reference.PATH_ROOT_BACKUPS).listFiles();
        if (fArr != null && worldProfile.trimMaxBackups != 0 && fArr.length > 1 && fArr.length - 1 > worldProfile.trimMaxBackups) {
            if (ingame)
                server.getPlayerList().sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.backup.starttrim"));
            new Thread(new TrimRunnable(worldProfile)).start();
        }

        if (ingame) {
            WorldProfile.currentWorldProfile = worldProfile;
            for (int i = 0; i < oldSaveStates.length; i++) {
                WorldServer worldServer = server.worlds[i];

                if (worldServer != null)
                    worldServer.disableLevelSaving = oldSaveStates[i];
            }

            server.getPlayerList().sendMessage(o ? new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackup", (System.currentTimeMillis() - start), datetime, comment.equals("misc.shcmbackupreborn.scheduled") ? I18n.format("misc.shcmbackupreborn.scheduled") : comment) : new TextComponentTranslation("chat.shcmbackupreborn.backup.endbackupfailed"));
        }
        return o;
    }

    private static class AutoBackup {
        private static void tick() {
            if(WorldProfile.currentWorldProfile != null && WorldProfile.currentWorldProfile.lastBackup != -1 && WorldProfile.currentWorldProfile.autoBackupInterval > 0 && System.currentTimeMillis() - WorldProfile.currentWorldProfile.lastBackup >= WorldProfile.currentWorldProfile.autoBackupInterval)
                backup(WorldProfile.currentWorldProfile.file.getParentFile().getParentFile(),"misc.shcmbackupreborn.scheduled");
        }
    }
    private static class TrimRunnable implements Runnable {
        final WorldProfile worldProfile;

        public TrimRunnable(WorldProfile worldProfile) {
            this.worldProfile = worldProfile;
        }

        @Override
        public void run() {
            worldProfile.trim();
        }
    }
}
