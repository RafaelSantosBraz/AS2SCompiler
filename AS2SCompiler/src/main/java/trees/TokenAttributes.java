/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees;

/**
 * Abstract class to handle the defined tokens' attributes. It is necessary to
 * use the Concrete or Universal classes.
 *
 * @author Rafael Braz
 */
public abstract class TokenAttributes {

    /**
     * abstract node's line number - follow the XML schema.
     */
    public static final int ABSTRACT_NODE_LINE = 0;
    /**
     * abstract node's column number - follow the XML schema.
     */
    public static final int ABSTRACT_NODE_COLUMN = -1;

    /**
     * The position of the node in the ordered tree.
     */
    protected int index;
    /**
     * The real value of concrete tokens or the rule name of Universal nodes.
     */
    protected String text;
    /**
     * the grammar classification of the token - parser rule number or token
     * number.
     */
    protected int type;
    /**
     * The line number of the token.
     */
    protected int line;
    /**
     * The column number of the token.
     */
    protected int column;

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
