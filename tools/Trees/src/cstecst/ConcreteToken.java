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
public class ConcreteToken extends TokenAttributes {

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
