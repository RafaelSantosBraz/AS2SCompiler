package pack;

import org.antlr.v4.gui.TreeViewer;
import converter.XMLDOTConverter;
import parser.JavaParser;

public class A {    

    public static void main(String[] args) {
        somtthing();
        System.out.println("oi");
    }
   
    private double s(int a){
        if (a > 0){
            return s(a - 1);
        }
        return 0.0;
    }

    private double s(){
        return 1.0;
    }

}