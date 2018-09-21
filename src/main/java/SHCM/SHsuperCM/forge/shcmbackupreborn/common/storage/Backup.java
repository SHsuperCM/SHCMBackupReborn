package SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage;

import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Backup extends Storage {
    public String directoryName = "", comment = "";
    public long timeSecondsEpoch = 0;
    public String timeText = "";
    public File directory;

    public Backup(File directory) {
        this.directoryName = (this.directory = directory).getName();
        readFile(new File(directory,"\\backup.dat"));
    }

    public Backup(File directory, String comment) {
        this.directoryName = (this.directory = directory).getName();
        this.comment = comment;

        this.timeText = getTimeText(this.timeSecondsEpoch = System.currentTimeMillis()/1000);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setLong("timeSecondsEpoch", timeSecondsEpoch);
        compound.setString("comment", comment);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        comment = nbt.getString("comment");
        timeText = getTimeText(timeSecondsEpoch = nbt.getLong("timeSecondsEpoch"));
    }

    private static String getTimeText(long timeSecondsEpoch) {
        return Instant.ofEpochSecond(timeSecondsEpoch).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
