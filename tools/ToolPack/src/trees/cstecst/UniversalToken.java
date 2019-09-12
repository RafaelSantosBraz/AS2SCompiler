/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees.cstecst;

/**
 * Represents the defined attributes of universal nodes on CSTs and eCSTs
 * @author Rafael Braz
 */
public class UniversalToken extends TokenAttributes {

    public UniversalToken(String text, int type) {
        super();
        index = ABSTRACT_NODE_COLUMN;
        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }
        this.type = type;
        line = ABSTRACT_NODE_LINE;
        column = ABSTRACT_NODE_COLUMN;
    }

}
