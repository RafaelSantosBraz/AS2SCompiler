/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package files;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Tools for creating and manipulating files and directories for the Compiler.
 *
 * @author Rafael Braz
 */
public class FileAux {

    /**
     * replace '/' separators for correct the File.separator.
     *
     * @param path
     * @return
     */
    public static String pathConverter(String path) {
        return FilenameUtils.separatorsToSystem(path);
    }

    /**
     * Creates all the directories from a given path.
     *
     * @param path folder's path.
     * @return
     */
    public static boolean createDirs(String path) {
        File dir = new File(path);
        return dir.exists() ? true : dir.mkdirs();
    }

    /**
     * Creates all the directories from a given path.
     *
     * @param path folder's path.
     * @return
     */
    public static boolean createDirs(File path) {
        return path.exists() ? true : path.mkdirs();
    }

    /**
     * Creates all the directories from ALL given paths.
     *
     * @param paths
     * @return
     */
    public static boolean createDirs(File... paths) {
        for (File f : paths) {
            if (!f.exists() && !f.mkdirs()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deletes all files and folders of a specified path.
     *
     * @param path folder's path.
     * @return
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    /**
     * Deletes all files and folders of a specified path.
     *
     * @param path folder's path.
     * @return
     */
    public static boolean deleteDir(File path) {
        try {
            FileUtils.deleteDirectory(path);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * list all files with given extensions in a directory, recursively or not.
     *
     * @param dir initial directory to search files from.
     * @param exts array of the desired file extensions.
     * @param recursive search within sub-directories?
     * @return list of all files.
     */
    public static File[] listFiles(File dir, String[] exts, boolean recursive) {
        return FileUtils.listFiles(dir, exts, recursive).toArray(File[]::new);
    }
}
