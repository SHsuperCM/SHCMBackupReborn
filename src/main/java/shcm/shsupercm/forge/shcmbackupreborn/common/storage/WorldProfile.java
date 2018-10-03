package shcm.shsupercm.forge.shcmbackupreborn.common.storage;

import shcm.shsupercm.core.snbt.misc.Storage;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

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

    public enum TrimBehavior {
        delete_excess {
            @Override
            void trim(File backupsDirectory) {

            }
        },
        archive_on_threshold {
            @Override
            void trim(File backupsDirectory) {

            }
        };

        abstract void trim(File backupsDirectory);
    }
}
