/*
 * Copyright (c) 14:36 27/09/18
 * Made by SHsuperCM
 */

package shcm.shsupercm.core.snbt.misc;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.*;

/**
 * Parent class for types who need to be saved on disk as an NBT dat format
 *
 * <h1>Example usage:</h1>
 *<pre>
 * {
 *     // Assuming MyStorageClass extends Storage and is not abstract
 *
 *     MyStorageClass msc = new MyStorageClass(); // Best practice is to have a default constructor exactly for this
 *     msc.readFile([ARBITRARY FILE]); // Will either read the file properly into msc(returning true) or fail and leave msc with default values(returning false)
 *
 *     [MANIPULATE msc, SAVE FOR LATER USE, DO SOME OTHER THINGS WITH IT]
 *
 *     // Whenever needed:
 *
 *     msc.writeFile(); // Saves the file to the place it was last read from or written to
 *     // Or
 *     msc.writeFile([ARBITRARY FILE]); // To "save as" in the parameter's file
 * }
 *</pre>
 */
public abstract class Storage implements INBTSerializable<NBTTagCompound> {
    public File file = null;

    /**
     * Reads stream to the object
     * @param stream Stream to read from
     * @return Success
     */
    public boolean readStream(InputStream stream) {
        try {
            deserializeNBT(CompressedStreamTools.readCompressed(stream));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads file to the object and sets local "last" file
     * @param file File to read from or null for local "last" file
     * @return Success
     */
    public boolean readFile(File file) {
        if(file == null && this.file == null) return false;
        if(file != null) this.file = file;
        try {
            return readStream(new FileInputStream(file == null ? this.file : file));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Reads local "last" file to the object ( like doing readFile(null) )
     * @return Success
     */
    public boolean readFile() { return readFile(null); }


    /**
     * Writes the object to stream
     * @param out stream to write to
     * @return Success
     */
    public boolean writeStream(OutputStream out) {
        try {
            CompressedStreamTools.writeCompressed(this.serializeNBT(),out);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes the object to file and sets local "last" file
     * @param file File to write to or null for local "last" file
     * @return Success
     */
    public boolean writeFile(File file) {
        if(file == null && this.file == null) return false;
        if(file != null) this.file = file;
        try {
            OutputStream out = new FileOutputStream(file == null ? this.file : file,false);
            boolean o = writeStream(out);
            out.close();
            return o;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes the object to local "last" file ( like doing writeFile(null) )
     * @return Success
     */
    public boolean writeFile() { return writeFile(null); }
}

