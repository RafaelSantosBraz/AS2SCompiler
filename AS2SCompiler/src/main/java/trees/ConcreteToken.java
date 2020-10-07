/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees;

/**
 * represents concrete tokens (terminal nodes) in eCSTs and CSTs
 *
 * @author Rafael Braz
 */
public class ConcreteToken extends TokenAttributes {

    /**
     * arguments follow the XML schema.
     *
     * @param index
     * @param text
     * @param type
     * @param line
     * @param column
     */
    public ConcreteToken(int index, String text, int type, int line, int column) {
        super();
        this.index = index;
        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
