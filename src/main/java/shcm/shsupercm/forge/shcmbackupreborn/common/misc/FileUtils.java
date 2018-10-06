package shcm.shsupercm.forge.shcmbackupreborn.common.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

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
                for (File childFile : fileToZip.listFiles())
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut, true, filter);
            } else
                for (File childFile : fileToZip.listFiles())
                    zipFile(childFile, childFile.getName(), zipOut, true, filter);

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
