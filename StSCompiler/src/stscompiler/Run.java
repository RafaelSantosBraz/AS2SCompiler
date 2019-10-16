/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stscompiler;

import analyzer.Analyzer;
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
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\input",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\Tmaps\\C_CST_eCST.tmap"
        };       
        if (!new Analyzer().createCST(params[0], params[1], params[2])){
            System.out.println("Error: it was not possible to create the CST!");
            return;
        }    
        //new Translator().createeCST(params[1] + File.separator + "temp" + File.separator + "CST.xml", params[3], "\"ruleinitial\"", params[2]);
    }

}
