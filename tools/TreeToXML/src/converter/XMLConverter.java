/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import simpletree.*;

/**
 *
 * @author Rafael Braz
 * @param <T>
 */
public class XMLConverter<T> {

    private Tree<T> tree;

    public XMLConverter(Tree<T> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
        } else {
            this.tree = tree;
        }
    }

    public boolean convertToFile(String outputPath) {

        return true;
    }

    public String convertToString(String outputPath) {
        
        return null;
    }
    
    public Tree<T> getTree() {
        return tree;
    }

    public void setTree(Tree<T> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
            return;
        }
        this.tree = tree;
    }

}
