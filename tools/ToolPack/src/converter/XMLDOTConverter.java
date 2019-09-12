/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.File;
import trees.cstecst.TokenAttributes;

/**
 *
 * @author Rafael Braz
 */
public class XMLDOTConverter {

    public boolean convertFromFile(String inputPath, String outputPath) {
        TreeXMLConverter convXML = new TreeXMLConverter();
        if (!convXML.convertFromFile(inputPath)) {
            return false;
        }
        DOTConverter<TokenAttributes> convDOT = new DOTConverter<>(convXML.getTree());
        return convDOT.convertToFile(outputPath);
    }

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
