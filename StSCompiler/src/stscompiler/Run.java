/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package stscompiler;

import controller.ProcessControl;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String[] params = new String[]{
//            "java",
//            "c",
//            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\input",
//            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output",
//            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\Tmaps\\Java_CST_eCST.tmap"
//        };
//        ProcessControl.execute(params);        
        ProcessControl.execute(args);
    }

}
