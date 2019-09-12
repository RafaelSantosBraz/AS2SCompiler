/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exportcstecstxml;

import eCSTGenerator.eCSTGenerator;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.JavaParser;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaParser parser = new JavaParser();
        parser.startParsing(
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\input",
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output"
        );
        String[] aux = {
            "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\input",
            "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\temp\\output"
        };
        try {
            eCSTGenerator.main(aux);
        } catch (Exception ex) {
            System.out.println("erro");
        }
    }

}
