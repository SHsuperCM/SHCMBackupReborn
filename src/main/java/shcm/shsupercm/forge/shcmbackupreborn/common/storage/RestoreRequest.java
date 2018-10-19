package shcm.shsupercm.forge.shcmbackupreborn.common.storage;

import net.minecraft.nbt.NBTTagCompound;
import shcm.shsupercm.core.snbt.misc.Storage;

public class RestoreRequest extends Storage {
    public String backup = "";

    public RestoreRequest() {}

    public RestoreRequest(String backup) {
        this.backup = backup;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setString("backup", this.backup);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.backup = nbt.getString("backup");
    }
}
