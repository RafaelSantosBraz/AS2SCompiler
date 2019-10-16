/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;

import java.io.File;
import parser.CParser;
import parser.JavaParser;

/**
 *
 * @author Rafael Braz
 */
public class Analyzer {

    public static final String C = "c";
    public static final String JAVA = "java";

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

}
