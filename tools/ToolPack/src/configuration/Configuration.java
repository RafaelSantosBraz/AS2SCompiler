/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package configuration;

import files.FileAux;
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

}
