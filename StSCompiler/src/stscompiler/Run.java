/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        String[] params = new String[]{
            "java",
            "c",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\input",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output",
            "D:\\GitHub\\StS-Compilation-Framework\\runtime\\Tmaps\\Java_CST_eCST.tmap"
        };
        ProcessControl.execute(params);        
    }

}
