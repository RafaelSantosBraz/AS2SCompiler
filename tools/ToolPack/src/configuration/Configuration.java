/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package configuration;

import cli.ArgumentsCLI;
import files.FileAux;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * static and dynamic properties of the compiler.
 *
 * @author Rafael Braz
 */
public class Configuration {

    /**
     * flag for marking dev/release operations. true is release, false is dev.
     */
    public static boolean RELEASE_FLAG = true;

    /**
     * directory of Tmap files.
     */
    public static final String TMAP_DIR
            = Configuration.RELEASE_FLAG
                    ? FileAux.pathConverter("lib/Tmaps/")
                    : FileAux.pathConverter("../../runtime/Tmaps/");

    /**
     * the current version label for the compiler.
     */
    public static final String VERSION = "AS2SCompiler v1.0-beta.3";

    /**
     * represents the C language.
     */
    public static final String C = "c";

    /**
     * represents the Java language.
     */
    public static final String JAVA = "java";

    /**
     * represents all languages.
     */
    public static final ArrayList<String> LANGUAGES
            = new ArrayList<>(Arrays.asList(new String[]{C, JAVA}));

    /**
     * keeps the command line arguments and options. It already has an instance.
     */
    public static final ArgumentsCLI ARGUMENTS_CLI = new ArgumentsCLI();

    /**
     * output directory.
     */
    public static File OUTPUT_DIR = null;

    /**
     * directory for the generated code.
     */
    public static File OBJ_CODE_DIR = null;

    /**
     * directory for temp files (CST, eCST, and adapted eCST) in XML and
     * DOT/Graphviz.
     */
    public static File TEMP_DIR = null;

    /**
     * directory for the CST files.
     */
    public static File CST_DIR = null;

    /**
     * directory for the eCST files.
     */
    public static File ECST_DIR = null;

    /**
     * directory for the adapted eCST files.
     */
    public static File ADAPTED_ECST_DIR = null;

    /**
     * sets all folders paths and File instances.
     */
    public static void setOutputDirs() {
        OUTPUT_DIR = ARGUMENTS_CLI.getOutputDirectory();
        OBJ_CODE_DIR = new File(FileAux.pathConverter(OUTPUT_DIR.getPath() + "/object_code/"));
        TEMP_DIR = new File(FileAux.pathConverter(OUTPUT_DIR.getPath() + "/temp/"));
        CST_DIR = new File(FileAux.pathConverter(TEMP_DIR.getPath() + "/cst/"));
        ECST_DIR = new File(FileAux.pathConverter(TEMP_DIR.getPath() + "/ecst/"));
        ADAPTED_ECST_DIR = new File(FileAux.pathConverter(TEMP_DIR.getPath() + "/adapted_ecst/"));
    }
}
