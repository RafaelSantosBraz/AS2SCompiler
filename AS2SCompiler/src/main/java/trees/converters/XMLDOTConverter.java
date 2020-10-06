/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.converters;

import java.io.File;
import trees.TokenAttributes;

/**
 * converts a tree in a XML file into a corresponding Graphviz file
 *
 * @author Rafael Braz
 */
public class XMLDOTConverter {

    /**
     * converts a simgle XML file into a single GV file.
     *
     * @param inputPath
     * @param outputPath
     * @return
     */
    public boolean convertFromFile(String inputPath, String outputPath) {
        TreeXMLConverter convXML = new TreeXMLConverter();
        if (!convXML.convertFromFile(inputPath)) {
            return false;
        }
        DOTConverter<TokenAttributes> convDOT = new DOTConverter<>(convXML.getTree());
        return convDOT.convertToFile(outputPath);
    }

    /**
     * converts all XML files in a dir into GV files.
     *
     * @param inputDir
     * @param outputDir
     * @return
     */
    public boolean convertFromDir(String inputDir, String outputDir) {
        try {
            File directory = new File(inputDir);
            File outputDirectory = new File(outputDir);
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && f.getName().endsWith(".xml");
            });
            for (File f : files) {
                if (!convertFromFile(
                        f.getAbsolutePath(),
                        outputDirectory.getAbsolutePath() + File.separator + f.getName() + ".gv"
                )) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
