/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.cstecst;

/**
 * Represents the attributes of universal nodes of CSTs and eCSTs
 *
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
