/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend;

import auxtools.SafeStack;
import configuration.Configuration;
import files.FileAux;
import frontend.parsers.AnyParser;
import java.io.File;

/**
 * represents the first mechanism of the framework - encapsule the analysis
 * process
 *
 * @author Rafael Braz
 */
public class Analyzer {

    /**
     * start parsing all input files.
     */
    public static void start() {
        var fileStack = new SafeStack<File>();
        String[] exts = null;
        switch (Configuration.INPUT_LANGUAGE) {
            case Configuration.C:
                exts = new String[]{"c", "h"};
                break;
            case Configuration.JAVA:
                exts = new String[]{"java"};
                break;
        }
        var files = FileAux.listFiles(Configuration.INPUT_DIR, exts, Configuration.RECURSIVE);
        fileStack.push(files);
        int cores = Runtime.getRuntime().availableProcessors();
        var treads = new Thread[cores];
        for (int i = 0; i < cores; i++) {
            treads[i] = new Thread(() -> {
                // parsing file starts here
                File file;
                while ((file = fileStack.pop()) != null) {
                    AnyParser.parseFile(file);
                }
            });
            treads[i].start();
        }
        for (int i = 0; i < cores; i++) {
            try {
                treads[i].join();
            } catch (InterruptedException ex) {
                System.err.println("Internal Error: " + ex.getMessage());
                System.exit(1);
            }
        }
    }
}
