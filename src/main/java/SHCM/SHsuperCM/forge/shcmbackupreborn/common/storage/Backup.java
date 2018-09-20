package SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage;

import net.minecraft.nbt.NBTTagCompound;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Backup extends Storage {
    public String directoryName = "", comment = "";
    public long timeSum = 0;
    public String timeText = "";


    public Backup() {
    }

    public Backup(String directoryName, String comment) {
        this.directoryName = directoryName;
        this.comment = comment;
        this.timeSum = System.currentTimeMillis()/1000;
        this.timeText = Instant.ofEpochSecond(timeSum).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();



        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
