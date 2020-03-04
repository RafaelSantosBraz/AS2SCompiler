/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.cstecst;

/**
 * Abstract class to handle the defined tokens' attributes. It is necessary to
 * use the Concrete or Universal classes
 *
 * @author Rafael Braz
 */
public abstract class TokenAttributes {

    public static final int ABSTRACT_NODE_LINE = 0;
    public static final int ABSTRACT_NODE_COLUMN = -1;

    protected int index; // The position of the node in the ordered tree
    protected String text; // The real value of concrete tokens or the rule name of Universal nodes
    protected int type; // the grammar classification of the token - parser rule number or token number
    protected int line; // The line number of the token
    protected int column; // The column number of the token

    public TokenAttributes() {

    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setText(String text) {
        if (text == null) {
            this.text = "";
            return;
        }
        this.text = text;
    }

    public void setType(int type) {
        this.type = type;
    }

}
