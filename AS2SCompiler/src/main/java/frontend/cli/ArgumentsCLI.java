/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.cli;

import configuration.Configuration;
import java.io.File;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 *
 * @author Rafael Braz
 */
@Command(name = "AS2SCompiler",
        usageHelpAutoWidth = true,
        sortOptions = false,
        requiredOptionMarker = '*',
        abbreviateSynopsis = true,
        mixinStandardHelpOptions = true,
        version = Configuration.VERSION,
        description = "Translate {C to Java} or {Java to C}.")
public class ArgumentsCLI implements Callable<Integer> {

    @Option(names = {"-iL", "--input-language", "--inp-lang"},
            description = "source language (Languages: ${COMPLETION-CANDIDATES}).",
            required = true,
            completionCandidates = LanguageCondidates.class)
    private String inputLanguage;

    @Option(names = {"-oL", "--output-language", "--out-lang"},
            description = "Target language (Languages: ${COMPLETION-CANDIDATES}).",
            required = true,
            completionCandidates = LanguageCondidates.class)
    private String outputLanguage;

    @Parameters(
            index = "0",
            description = "Source (input) directory."
    )
    private File inputDirectory;

    @Parameters(
            index = "1",
            description = "Target (output) directory."
    )
    private File outputDirectory;

    @Override
    public Integer call() throws Exception {
        if (!Configuration.LANGUAGES.contains(inputLanguage.toLowerCase())) {
            System.err.println(String.format("'%s' is not a vality language!",
                    inputLanguage));
            return 1;
        }
        if (!Configuration.LANGUAGES.contains(outputLanguage.toLowerCase())) {
            System.err.println(String.format("'%s' is not a vality language!",
                    outputLanguage));
            return 1;
        }
        if (!inputDirectory.exists()) {
            System.err.println(String.format("'%s' source directory does not exist!",
                    inputDirectory.getPath()));
            return 1;
        }
        Configuration.INPUT_LANGUAGE = inputLanguage;
        Configuration.OUTPUT_LANGUAGE = outputLanguage;
        Configuration.OUTPUT_DIR = new File(outputDirectory.getPath());
        Configuration.INPUT_DIR = new File(inputDirectory.getPath());
        Configuration.setOutputDirs();
        return 0;
    }

    /**
     * start parsing the arguments. Errors stop the program.
     *
     * @param args
     */
    public void start(String args[]) {
        if (new CommandLine(new ArgumentsCLI()).execute(args) != 0) {
            System.exit(1);
        }        
    }

}

/**
 * all languages candidates.
 *
 * @author Rafael Braz
 */
class LanguageCondidates extends ArrayList<String> {

    LanguageCondidates() {
        super(Configuration.LANGUAGES);
    }
}
