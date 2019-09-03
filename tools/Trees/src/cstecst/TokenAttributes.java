/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cstecst;

/**
 *
 * @author Rafael Braz
 */
public abstract class TokenAttributes {

    public static final int ABSTRACT_NODE_LINE = -1;
    public static final int ABSTRACT_NODE_COLUMN = 0;

    protected int index;
    protected String text;
    protected int type;
    protected int line;
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
