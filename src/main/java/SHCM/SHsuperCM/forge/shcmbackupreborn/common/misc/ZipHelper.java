package SHCM.SHsuperCM.forge.shcmbackupreborn.common.misc;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    public static boolean zip(File fileToZip, File zipDestination, Predicate<File> filter) {
        try {
            FileOutputStream fos = new FileOutputStream(zipDestination);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            zipFile(fileToZip, fileToZip.getName(), zipOut, filter);
            zipOut.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean unzip(File zip, File destinationRootDirectory) {
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {

                FileOutputStream fos = new FileOutputStream(new File(destinationRootDirectory,zipEntry.getName()));
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    //TODO not include root directory in zip
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut, Predicate<File> filter) throws IOException {
        if(!filter.test(fileToZip)) return;
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut, filter);
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
}
