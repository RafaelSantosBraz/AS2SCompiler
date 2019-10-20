/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stscompiler;

import analyzer.Analyzer;
import converter.XMLDOTConverter;
import java.io.File;
import translator.Translator;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] params = new String[]{
            "c",
            "java",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\input",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\Tmaps\\C_CST_eCST.tmap"
        };       
        if (params.length != 5){
            System.out.println("StSCompiler : <in. lang.> <out. lang.> <in. dir.> <out. dir.> <tmap path>");
            return;
        }
        if (!new Analyzer().createCST(params[0], params[2], params[3])){
            System.err.println("Error: it was not possible to create the CST!");
            return;
        }   
        Translator translator = new Translator();
        if (!translator.createeCST(params[3] + File.separator + "temp" + File.separator + "CST.xml", params[4], "\"ruleinitial\"", params[3])){
            System.err.println("Error: it was not possible to create the eCST!");
            return;
        }
        if (!translator.adapteCST(params[3] + File.separator + "eCST.xml", params[0], params[1])){
            System.err.println("Error: it was not possible to adapt the eCST!");
            return;
        }
//        new XMLDOTConverter().convertFromDir(
//                "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\temp\\licca",
//                "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output"
//        );
    }

}
