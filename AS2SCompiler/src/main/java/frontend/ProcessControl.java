/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend;

import configuration.Configuration;
import java.io.File;
import files.FileAux;

/**
 * provides a static method to execute the entire framework
 *
 * @author Rafael Braz
 */
public class ProcessControl {

    /**
     * Executes the entire framework.
     *
     */
    public static void execute() {
        if (!prepareOutPaths()) {
            System.err.println("Error: it was not possible to create or delete output folders! Verify permissions for files and folders then try again.");
            return;
        }
        if (!new Analyzer().createCST()) {
            System.err.println("Error: it was not possible to create the CST!");
            return;
        }
//        if (!new Analyzer().createShorterCST(Configuration.TEMP_DIR.getPath() + File.separator + "CST.xml",
//                Configuration.TEMP_DIR.getPath())) {
//            System.err.println("Warning: it was not possible to create the shorter CST!");
//        }
        Translator translator = new Translator();
        String tmap_path = Configuration.TMAP_DIR.getPath() + File.separator
                + (Configuration.INPUT_LANGUAGE.equals(Configuration.JAVA) ? "Java_CST_eCST.tmap" : "C_CST_eCST.tmap");
        if (!translator.createeCST(Configuration.TEMP_DIR.getPath() + File.separator + "CST.xml",
                tmap_path,
                "\"ruleinitial\"", Configuration.OUTPUT_DIR.getPath())) {
            System.err.println("Error: it was not possible to create the eCST!");
            return;
        }
        String auxTmapDir = Translator.inferAuxTmapsDir(tmap_path, Configuration.INPUT_LANGUAGE, Configuration.OUTPUT_LANGUAGE);
        if (!translator.adapteCST(Configuration.OUTPUT_DIR.getPath() + File.separator + "eCST.xml", auxTmapDir,  Configuration.INPUT_LANGUAGE, Configuration.OUTPUT_LANGUAGE)) {
            System.err.println("Error: it was not possible to adapt the eCST!");
            return;
        }
        String auxWriteTmapDir = Translator.inferAuxWriteTmapsDir(tmap_path, Configuration.INPUT_LANGUAGE);
        if (!translator.gerateCode(Configuration.OUTPUT_DIR.getPath() + File.separator + "eCSTadapted.xml", auxWriteTmapDir, Configuration.OUTPUT_LANGUAGE)) {
            System.err.println("Error: it was not possible to create the output program!");
            return;
        }
    }

    /**
     * Deletes all files from the output folder. If it does not exist yet, it
     * will be created.
     *
     * @return
     */
    private static boolean prepareOutPaths() {
        if (Configuration.OUTPUT_DIR.exists()) {
            if (!FileAux.deleteDir(Configuration.OUTPUT_DIR)) {
                return false;
            }
        }
        return FileAux.createDirs(
                Configuration.OBJ_CODE_DIR,
                Configuration.CST_DIR,
                Configuration.ECST_DIR,
                Configuration.ADAPTED_ECST_DIR
        );
    }

}
