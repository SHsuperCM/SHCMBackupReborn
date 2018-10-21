package shcm.shsupercm.forge.shcmbackupreborn.common.misc;

import org.apache.logging.log4j.util.TriConsumer;

import java.io.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    /**
     * Will zip {@code fileToZip} and any children files and save the zip in {@code zipDestination}.
     * If {@code fileToZip} is directory will zip recursively all contents and if {@code includeRoot} will zip the directory as well.
     * Will zip {@code fileToZip} or any of its children only if {@code filter} returns true.
     *
     * @param fileToZip
     * @param zipDestination
     * @param includeRoot
     * @param filter
     * @return
     */
    public static boolean zip(File fileToZip, File zipDestination, boolean includeRoot, Predicate<File> filter) {
        try {
            zipDestination.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(zipDestination);
            ZipOutputStream zipOut = new ZipOutputStream(fos);


            zipFile(fileToZip, fileToZip.getName(), zipOut, includeRoot, filter);

            zipOut.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Will unzip {@code zip} file to {@code destDir} and report progress to {@code progressCallback}.
     *
     * @param zip
     * @param destDir
     * @param progressCallback
     * @throws IOException
     */
    public static void unzip(File zip, File destDir, TriConsumer<Integer, Integer, String> progressCallback) throws IOException {
        destDir.mkdirs();

        ZipFile zipFile = new ZipFile(zip);
        int max = zipFile.size(), cur = 0;
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zip));
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {
            progressCallback.accept(cur,max,entry.getName());
            File file = new File(destDir.getAbsolutePath(), entry.getName());
            if (entry.isDirectory())
                destDir.mkdirs();
            else {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] bytesIn = new byte[4096];
                int read;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
            cur++;
        }
        zipIn.close();
    }

    /**
     * Deletes {@code file}.
     * If file is directory will delete recursively all contents and if {@code includeRoot} will delete the directory.
     * Will delete file or any of its children only if {@code filter} returns true.
     *
     * @param file
     * @param includeRoot
     * @param filter
     */
    public static void delete(File file, boolean includeRoot, Predicate<File> filter) {
        if(!filter.test(file)) return;
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null)
                for (File childFile : files)
                    delete(childFile,true, filter);

            if(includeRoot)
                file.delete();
        } else {
            file.delete();
        }
    }

    /**
     * Recursively adds files to a {@code ZipOutputStream}
     *
     * @param fileToZip
     * @param fileName
     * @param zipOut
     * @param includeRoot
     * @param filter
     * @throws IOException
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut, boolean includeRoot, Predicate<File> filter) throws IOException {
        if(!filter.test(fileToZip)) return;
        if (fileToZip.isDirectory()) {
            if(includeRoot) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    zipOut.closeEntry();
                } else {
                    zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                    zipOut.closeEntry();
                }
                File[] files = fileToZip.listFiles();
                if(files != null)
                    for (File childFile : files)
                        zipFile(childFile, fileName + "/" + childFile.getName(), zipOut, true, filter);
            } else {
                File[] files = fileToZip.listFiles();
                if(files != null)
                    for (File childFile : files)
                        zipFile(childFile, childFile.getName(), zipOut, true, filter);
            }

            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void main(String[] args) {
        delete(new File("C:\\Users\\שחר\\Desktop\\Forge Modding\\Focused EnvIronMent\\SHCMBackupReborn\\run\\saves\\New World - Copy"),false,file -> !file.getAbsolutePath().endsWith(Reference.PATH_ROOT_BACKUPS));
    }
}
