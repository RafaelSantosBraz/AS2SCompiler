/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import cstecst.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import simpletree.*;

/**
 *
 * @author Rafael Braz
 */
public class TreeXMLConverter {

    private final Tree<TokenAttributes> tree;

    public TreeXMLConverter() {
        tree = new Tree<>();
    }

    public boolean convertFromFile(String inputPath) {
        try {            
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(inputPath));
            NodeList childNodes = document.getChildNodes();
            for (int c = 0; c < childNodes.getLength(); c++){
                
            }
            
//            String usr = document.getElementsByTagName("user").item(0).getTextContent();
//            String pwd = document.getElementsByTagName("password").item(0).getTextContent();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void convertion(org.w3c.dom.Node domNode, simpletree.Node<TokenAttributes> parent){
        if (domNode.hasAttributes()){
            simpletree.Node<TokenAttributes> treeNode = tree.createNode(parent);
            NamedNodeMap attributes = domNode.getAttributes();
            //if (attributes.getNamedItem("column").getn)
            //treeNode.setNodeData(nodeData);
        }
    }
    
    public Tree<TokenAttributes> getTree() {
        return tree;
    }

}
