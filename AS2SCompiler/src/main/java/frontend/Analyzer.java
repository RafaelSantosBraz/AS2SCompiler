/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend;

import auxtools.FastStack;
import auxtools.JSONHandler;
import auxtools.SafeStack;
import configuration.Configuration;
import files.FileAux;
import frontend.parsers.AnyParser;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

/**
 * represents the first mechanism of the framework - encapsule the analysis
 * process
 *
 * @author Rafael Braz
 */
public class Analyzer {

    /**
     * start parsing all input files.
     *
     * @return a safe stack of all eCSTs.
     */
    public static SafeStack<JSONObject> start() {
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
        var fileStack = new SafeStack<File>(files.length);
        var eCSTStack = new SafeStack<JSONObject>(files.length);
        fileStack.push(files);
        int cores = Runtime.getRuntime().availableProcessors();
        var treads = new Thread[cores];
        for (int i = 0; i < cores; i++) {
            treads[i] = new Thread(() -> {
                // parsing file starts here              
                var localStack = new FastStack<JSONObject>(files.length / cores);
                File file;
                while ((file = fileStack.pop()) != null) {
                    var eCST = AnyParser.parseFile(file);
                    localStack.push(eCST);
                    if (Configuration.EXPOSE_ANY) {
                        if (Configuration.EXPOSE_JSON) {
                            JSONHandler.writeToFileJSON(
                                    Configuration.ECST_DIR,
                                    String.format("%s.json", FilenameUtils.removeExtension(file.getName())),
                                    eCST);
                        }
                        if (Configuration.EXPOSE_XML) {
                            JSONHandler.writeToFileXML(
                                    Configuration.ECST_DIR,
                                    String.format("%s.xml", FilenameUtils.removeExtension(file.getName())),
                                    eCST);
                        }
                        if (Configuration.EXPOSE_DOT) {
                            JSONHandler.writeToFileDOT(
                                    Configuration.ECST_DIR,
                                    String.format("%s.dot", FilenameUtils.removeExtension(file.getName())),
                                    eCST);
                        }
                    }
                }
                var items = new JSONObject[localStack.size()];
                localStack.asList().toArray(items);
                eCSTStack.push(items);
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
        return eCSTStack;
    }
}
