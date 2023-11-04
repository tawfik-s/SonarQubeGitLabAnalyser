package com.technoverse.platformManager.utils;

import java.io.File;

public class FileDirectoryCleaningUtil {

    public static void cleanDirectories(String... directories) {
        for (String directory : directories) {
            File dir = new File(directory);
            if (dir.exists() && dir.isDirectory()) {
                deleteDirectory(dir);
            }else if(!dir.isDirectory()){
                dir.delete();
            }
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}
