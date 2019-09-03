/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import simpletree.Tree;

/**
 *
 * @author Rafael Braz
 * @param <T>
 */
public class XMLConverter<T> {
    
    private Tree<T> tree;
    
    public XMLConverter(Tree<T> tree){
        this.tree = tree;
    }
    
    public boolean convertToFile(String outputPath){
        
        return true;
    }
    
}
