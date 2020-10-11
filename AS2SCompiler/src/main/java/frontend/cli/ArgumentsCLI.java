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
import java.util.Arrays;
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

    @Option(names = {"-il", "--input-language", "--inp-lang"},
            completionCandidates = LanguageCondidates.class,
            description = "Source language (Languages: ${COMPLETION-CANDIDATES}).",
            required = true)
    private String inputLanguage;

    @Option(names = {"-ol", "--output-language", "--out-lang"},
            completionCandidates = LanguageCondidates.class,
            description = "Target language (Languages: ${COMPLETION-CANDIDATES}).",
            required = true
    )
    private String outputLanguage;

    @Option(names = {"-r", "--recursive"},
            description = "Translates the source code of the input directory and its subdirectories. Default: 'false'",
            defaultValue = "false",
            negatable = true)
    private boolean recursive;

    @Option(names = {"--expose"},
            completionCandidates = IRTypes.class,
            description = "keeps all intermediate representation files. (types: ${COMPLETION-CANDIDATES}). More than 1 is welcome.",
            split = ",",
            paramLabel = "type",
            required = false)
    private String[] exposes = new String[]{};

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
        if (outputDirectory.exists() && inputDirectory.compareTo(outputDirectory) == 0) {
            System.err.println(String.format("input and output directories can not be the same!",
                    inputDirectory.getPath()));
            return 1;
        }
        var exposesList = Arrays.asList(exposes);
        exposesList.replaceAll((t) -> {
            return t.toLowerCase();
        });
        exposesList.forEach((t) -> {
            if (!Configuration.IR_TYPES.contains(t)) {
                System.err.println(String.format("--expose option '%s' is not valid!", t));
                System.exit(1);
            }
            switch (t) {
                case "json":
                    Configuration.EXPOSE_JSON = true;
                    break;
                case "xml":
                    Configuration.EXPOSE_XML = true;
                    break;
                case "dot":
                    Configuration.EXPOSE_DOT = true;
                    break;
            }
        });
        if (!exposesList.isEmpty()) {
            Configuration.EXPOSE_ANY = true;
        }
        Configuration.INPUT_LANGUAGE = inputLanguage;
        Configuration.OUTPUT_LANGUAGE = outputLanguage;
        Configuration.RECURSIVE = recursive;
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

/**
 * all IR types.
 *
 * @author Rafael Braz
 */
class IRTypes extends ArrayList<String> {

    IRTypes() {
        super(Configuration.IR_TYPES);
    }
}
