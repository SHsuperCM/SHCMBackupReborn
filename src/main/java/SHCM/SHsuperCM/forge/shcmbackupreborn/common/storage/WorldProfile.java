package SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage;

import SHCM.SHsuperCM.core.snbt.misc.Storage;
import net.minecraft.nbt.NBTTagCompound;

public class WorldProfile extends Storage {public WorldProfile() {}
    public static WorldProfile currentWorldProfile = null;


    public long autoBackupInterval = 0; //0 for disabled, >0 for millis interval
    public long lastBackup = -1; //when in epoch was the last backup
    public int trimMaxBackups = 0; //0 for disabled, >0 for max backups before trimming
    public String trimBehavior = "delete_excess"; //delete_excess or archive_on_threshold

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setLong("autoBackupInterval",autoBackupInterval);
        compound.setLong("lastBackup",lastBackup);
        compound.setInteger("trimMaxBackups", trimMaxBackups);
        compound.setString("trimBehavior", trimBehavior);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        autoBackupInterval = nbt.getLong("autoBackupInterval");
        lastBackup = nbt.getLong("lastBackup");
        trimMaxBackups = nbt.getInteger("trimMaxBackups");
        trimBehavior = nbt.getString("trimBehavior");
    }


}
