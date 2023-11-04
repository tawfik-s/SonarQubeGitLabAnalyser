package com.technoverse.platformManager.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.stereotype.Service;

import java.io.*;
public class UnCompressUtil {


    public static void unTar(String tarFile, String destFile) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(tarFile);

            BufferedInputStream bis = new BufferedInputStream(fis);
            GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);

            TarArchiveInputStream tarInput = new TarArchiveInputStream(gzis);

            ArchiveEntry entry;
            while ((entry = tarInput.getNextEntry()) != null) {
                // Get the name of the entry (file or directory)
                String entryName = entry.getName();

                // Create the output file for the entry
                File outputFile = new File(destFile, removeFirstFolderName(entryName));

                // If the entry is a directory, create the directory
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // If the entry is a file, create parent directories and extract the file
                    outputFile.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = tarInput.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                }
            }

            // Close the TarArchiveInputStream
            tarInput.close();

            System.out.println("Tar archive extraction complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String removeFirstFolderName(String path) {
        // Split the path string into a list of folder names.
        String[] folderNames = path.split("/");

        // Remove the first folder name from the list.
        String[] newFolderNames = new String[folderNames.length - 1];
        System.arraycopy(folderNames, 1, newFolderNames, 0, newFolderNames.length);

        // Join the list of folder names back into a string.
        String newPath = String.join("/", newFolderNames);

        return newPath;
    }

}
