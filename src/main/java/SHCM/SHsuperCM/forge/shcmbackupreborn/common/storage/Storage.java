package SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.*;

public abstract class Storage implements INBTSerializable<NBTTagCompound> {
    public boolean readStream(InputStream stream) {
        try {
            deserializeNBT(CompressedStreamTools.readCompressed(stream));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean readFile(File file) {
        try {
            return readStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean writeStream(OutputStream out) {
        try {
            CompressedStreamTools.writeCompressed(this.serializeNBT(),out);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeToFile(File file) {
        try {
            OutputStream out = new FileOutputStream(file,false);
            boolean o = writeStream(out);
            out.close();
            return o;
        } catch (IOException e) {
            return false;
        }
    }
}
