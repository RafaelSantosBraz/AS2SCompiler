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
 * represents the first mechanism of the framework - encapsule the analysis
 * process
 * @author Rafael Braz
 */
public class Analyzer {

    public static final String C = "c"; // use this const to refer C lang
    public static final String JAVA = "java"; // use this const to refer Java lang

    // converts source-code in a directory into a CST
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
