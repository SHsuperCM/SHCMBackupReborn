package SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage;

import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class WorldProfile extends Storage {public WorldProfile() {}
    public long autoBackupInterval = 0; //0 for disabled, >0 for millis interval
    public int trimAmount = 0; //0 for disabled, >0 for max backups before trimming
    public boolean archiveTrimmings = false; //false for delete excess, true for zip excess and move to config excess path
    public File directory;

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setLong("autoBackupInterval",autoBackupInterval);
        compound.setInteger("trimAmount",trimAmount);
        compound.setBoolean("archiveTrimmings",archiveTrimmings);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        autoBackupInterval = nbt.getLong("autoBackupInterval");
        trimAmount = nbt.getInteger("trimAmount");
        archiveTrimmings = nbt.getBoolean("archiveTrimmings");
    }


}
