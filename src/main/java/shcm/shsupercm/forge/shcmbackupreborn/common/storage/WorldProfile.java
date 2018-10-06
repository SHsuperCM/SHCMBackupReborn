package shcm.shsupercm.forge.shcmbackupreborn.common.storage;

import shcm.shsupercm.core.snbt.misc.Storage;
import net.minecraft.nbt.NBTTagCompound;
import shcm.shsupercm.forge.shcmbackupreborn.Config;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.FileUtils;
import shcm.shsupercm.forge.shcmbackupreborn.common.misc.Reference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorldProfile extends Storage {public WorldProfile() {}
    public static WorldProfile currentWorldProfile = null;


    public long autoBackupInterval = 0; //0 for disabled, >0 for millis interval
    public long lastBackup = -1; //when in epoch was the last backup
    public int trimMaxBackups = 0; //0 for disabled, >0 for max backups before trimming
    public TrimBehavior trimBehavior = TrimBehavior.delete_excess;

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setLong("autoBackupInterval",autoBackupInterval);
        compound.setLong("lastBackup",lastBackup);
        compound.setInteger("trimMaxBackups", trimMaxBackups);
        compound.setString("trimBehavior", trimBehavior.name());

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        autoBackupInterval = nbt.getLong("autoBackupInterval");
        lastBackup = nbt.getLong("lastBackup");
        trimMaxBackups = nbt.getInteger("trimMaxBackups");
        trimBehavior = TrimBehavior.valueOf(nbt.getString("trimBehavior"));
    }

    public void trim() {
        trimBehavior.trim(file.getParentFile(),trimMaxBackups);
    }

    public enum TrimBehavior {
        delete_excess {
            @Override
            protected void onTrim(File backupsDirectory, List<File> files, int maxBackups) {
                files.sort(Comparator.comparing(File::getName));
                for (int i = 0; i < files.size()-maxBackups; i++)
                    files.get(i).delete();
            }
        },
        archive_on_threshold {
            @Override
            protected void onTrim(File backupsDirectory, List<File> files, int maxBackups) throws IOException {
                files.sort(Comparator.comparing(File::getName));
                FileUtils.zip(backupsDirectory, new File(Config.archive_save_path, files.get(0).getName() + "---" + files.get(files.size()-1).getName() + ".zip"),false, f -> !f.getAbsolutePath().endsWith(Reference.PATH_WORLDPROFILE));
                files.forEach(File::delete);
            }
        };

        public static final List<String> valueNames = Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());

        protected abstract void onTrim(File backupsDirectory, List<File> files, int maxBackups) throws Exception;

        public void trim(File backupsDirectory, int maxBackups) {
            File[] filesArray = backupsDirectory.listFiles();
            if(filesArray == null) return;

            List<File> files = new ArrayList<>(Arrays.asList(filesArray));
            files.removeIf(f -> f.getAbsolutePath().endsWith(Reference.PATH_WORLDPROFILE));
            try {
                onTrim(backupsDirectory, files,maxBackups);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
