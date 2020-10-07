/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend;

import configuration.Configuration;
import trees.converters.DOTConverter;
import trees.converters.TreeXMLConverter;
import java.io.File;
import frontend.parsers.CParser;
import frontend.parsers.JavaParser;
import trees.shorteners.TreeShortener;
import trees.TokenAttributes;
import trees.Tree;

/**
 * represents the first mechanism of the framework - encapsule the analysis
 * process
 *
 * @author Rafael Braz
 */
public class Analyzer {

    /**
     * Converts all source-code in inputdir to CSTs.
     *
     * @return
     */
    public boolean createCST() {
        switch (Configuration.INPUT_LANGUAGE) {
            case Configuration.C:
                return new CParser().startParsing(
                        Configuration.INPUT_DIR.getPath(),
                        Configuration.TEMP_DIR.getPath());
            case Configuration.JAVA:
                return new JavaParser().startParsing(
                        Configuration.INPUT_DIR.getPath(),
                        Configuration.TEMP_DIR.getPath());
            default:
                return false;
        }
    }

    /**
     * Creates a shorter version of a CST (without chains of nodes that have
     * only one child).
     *
     * @param CSTPath
     * @param outputDir
     * @return
     */
    public boolean createShorterCST(String CSTPath, String outputDir) {
        try {
            TreeXMLConverter conv = new TreeXMLConverter();
            conv.convertFromFile(CSTPath);
            TreeShortener tShort = new TreeShortener();
            Tree<TokenAttributes> tree = tShort.shortenTree(conv.getTree());
            DOTConverter<TokenAttributes> dot = new DOTConverter<>(tree);
            dot.convertToFile(outputDir + File.separator + "CSTshort.gv");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
