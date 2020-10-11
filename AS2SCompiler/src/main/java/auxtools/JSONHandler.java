/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

import files.FileAux;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

/**
 * Operations tools for converting to and from JSON.
 *
 * @author Rafael Braz
 */
public class JSONHandler {

    /**
     * transforms an ANTLR tree into a JSON object.
     *
     * @param ctx head of the ANTLR tree.
     * @return JSON object.
     */
    public static JSONObject antlrToJson(ParserRuleContext ctx) {
        var jsonStr = antlrToStringJson(ctx);
        return new JSONObject(new JSONTokener(jsonStr.toString()));
    }

    /**
     * transforms an ANTLR tree into a JSON String representation.
     *
     * @param ctx head of the ANTLR tree.
     * @return StringBuilder for the JSON string.
     */
    public static StringBuilder antlrToStringJson(ParserRuleContext ctx) {
        var builder = new StringBuilder("{");
        antlrToStringJsonRecursive(ctx, builder);
        builder.append("}");
        return builder;
    }

    private static void antlrToStringJsonRecursive(ParseTree ctx, StringBuilder builder) {
        builder.append("\"token\": {");
        var payload = ctx.getPayload();
        String text;
        int type;
        if (payload instanceof RuleContext) {
            var rule = (RuleContext) payload;
            type = rule.getRuleIndex();
            text = rule.getClass().getSimpleName();
        } else {
            var token = (Token) payload;
            text = token.getText();
            if (text.contains("\"")) {
                text = text.replaceAll("\"", "\\\\\"");
            }
            type = token.getType();
        }
        builder.append("\"type\":");
        builder.append(type);
        builder.append(",\"text\":\"");
        builder.append(text);
        builder.append("\"},\"childElement\": [");
        for (int c = 0; c < ctx.getChildCount(); c++) {
            builder.append("{");
            antlrToStringJsonRecursive(ctx.getChild(c), builder);
            builder.append("},");
        }
        builder.append("]");
    }

    /**
     * receives a JSON object and writes the String version to a file.
     *
     * @param dir file directory.
     * @param fileName file name + extension.
     * @param json JSON object.
     */
    public static void writeToFileJSON(File dir, String fileName, JSONObject json) {
        try (var writer = new FileWriter(
                FileAux.pathConverter(
                        String.format("%s/%s", dir.getAbsolutePath(), fileName)
                )
        )) {
            writer.write(json.toString());
        } catch (IOException ex) {
            System.err.println("It was not possible to write JSON file!" + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * receives a JSON object and writes the XML version to a file.
     *
     * @param dir file directory.
     * @param fileName file name + extension.
     * @param json JSON object.
     */
    public static void writeToFileXML(File dir, String fileName, JSONObject json) {
        try (var writer = new FileWriter(
                FileAux.pathConverter(
                        String.format("%s/%s", dir.getAbsolutePath(), fileName)
                )
        )) {
            writer.write(XML.toString(json));
        } catch (IOException ex) {
            System.err.println("It was not possible to write XML file!" + ex.getMessage());
            System.exit(1);
        }
    }

}
