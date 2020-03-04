/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package controller;

import analyzer.Analyzer;
import java.io.File;
import translator.Translator;
import converter.XMLDOTConverter;

/**
 * provides a static method to execute the entire framework
 *
 * @author Rafael Braz
 */
public class ProcessControl {

    /**
     * Executes the entire framework.
     * 
     * @param params
     */
    public static void execute(String[] params) {
        if (params.length != 5) {
            System.out.println("StSCompiler : <in. lang.> <out. lang.> <in. dir.> <out. dir.> <tmap path>");
            return;
        }
        if (!new Analyzer().createCST(params[0], params[2], params[3])) {
            System.err.println("Error: it was not possible to create the CST!");
            return;
        }
        if (!new Analyzer().createShorterCST(params[3] + File.separator + "temp" + File.separator + "CST.xml",
                params[3] + File.separator + "temp")) {
            System.err.println("Warning: it was not possible to create the shorter CST!");
        }
        Translator translator = new Translator();
        if (!translator.createeCST(params[3] + File.separator + "temp" + File.separator + "CST.xml", params[4],
                "\"ruleinitial\"", params[3])) {
            System.err.println("Error: it was not possible to create the eCST!");
            return;
        }
        String auxTmapDir = Translator.inferAuxTmapsDir(params[4], params[0], params[1]);
        if (!translator.adapteCST(params[3] + File.separator + "eCST.xml", auxTmapDir, params[0], params[1])) {
            System.err.println("Error: it was not possible to adapt the eCST!");
            return;
        }
        String auxWriteTmapDir = Translator.inferAuxWriteTmapsDir(params[4], params[1]);
        if (!translator.gerateCode(params[3] + File.separator + "eCSTadapted.xml", auxWriteTmapDir, params[1])) {
            System.err.println("Error: it was not possible to create the output program!");
            return;
        }
        // new XMLDOTConverter().convertFromDir(
        // "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\temp\\licca",
        // "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output"
        // );
    }

}
