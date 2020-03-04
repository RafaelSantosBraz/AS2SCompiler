/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package analyzer;

import converter.DOTConverter;
import converter.TreeXMLConverter;
import java.io.File;
import parser.CParser;
import parser.JavaParser;
import shorteners.TreeShortener;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Tree;

/**
 * represents the first mechanism of the framework - encapsule the analysis
 * process
 *
 * @author Rafael Braz
 */
public class Analyzer {

    /**
     * Use this const to refer to C lang.
     */
    public static final String C = "c";
    /**
     * Use this const to refer to Java lang.
     */
    public static final String JAVA = "java";

    /**
     * Converts all source-code in a directory (inputdir) to a CST.
     *
     * @param language
     * @param inputDir
     * @param outputDir
     * @return
     */
    public boolean createCST(String language, String inputDir, String outputDir) {
        switch (language) {
            case Analyzer.C:
                return new CParser().startParsing(inputDir, outputDir + File.separator + "temp");
            case Analyzer.JAVA:
                return new JavaParser().startParsing(inputDir, outputDir + File.separator + "temp");
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
