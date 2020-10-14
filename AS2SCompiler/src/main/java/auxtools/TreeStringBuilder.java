/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 *
 * @author Rafael Braz
 */
public class TreeStringBuilder {

    private final StringBuilder builder;

    public TreeStringBuilder(int initialCapacity) {
        this.builder = new StringBuilder(initialCapacity);
    }

    public TreeStringBuilder append(String str) {
        builder.append(str);
        return this;
    }

    public TreeStringBuilder append(int i) {
        builder.append(i);
        return this;
    }

    public TreeStringBuilder appendComplete(String text, int type, String... children) {
        builder
                .append("{\"token\":{")
                .append("\"text\":\"")
                .append(text)
                .append("\",\"type\":")
                .append(type)
                .append("},\"childElement\":[");
        for (var child : children) {
            builder
                    .append(child)
                    .append(",");
        }
        builder.append("]}");
        return this;
    }

    public TreeStringBuilder appendComplete(String text, int type, ParseTreeVisitor visitor, ParseTree... children) {
        builder
                .append("{\"token\":{")
                .append("\"text\":\"")
                .append(text)
                .append("\",\"type\":")
                .append(type)
                .append("},\"childElement\":[");
        for (var child : children) {
            visitor.visit(child);
            builder.append(",");
        }
        builder.append("]}");
        return this;
    }

    public TreeStringBuilder appendComplete(String text, int type) {
        builder
                .append("{\"token\":{")
                .append("\"text\":\"")
                .append(text)
                .append("\",\"type\":")
                .append(type)
                .append("},\"childElement\":[]}");               
        return this;
    }
    
    public TreeStringBuilder appendTokenOpenChildren(String text, int type) {
        builder
                .append("{\"token\":{")
                .append("\"text\":\"")
                .append(text)
                .append("\",\"type\":")
                .append(type)
                .append("},\"childElement\":[");
        return this;
    }

    public TreeStringBuilder appendCloseTokenChildren() {
        builder.append("]}");        
        return this;

    }

    @Override
    public String toString() {
        return builder.toString();
    }
    
    
}
