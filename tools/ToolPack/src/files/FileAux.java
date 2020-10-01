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
        File parent = new File(path).getParentFile();
        return parent.exists() ? true : parent.mkdirs();
    }

    /**
     * Deletes all files and folders of a specified path.
     *
     * @param path folder's path.
     * @return
     */
    public static boolean deleteDir(String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
