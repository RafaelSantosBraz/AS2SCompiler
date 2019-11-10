/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import trees.cstecst.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import trees.simpletree.*;

/**
 * converts a XML file containing a tree (CST or eCST) into a simple tree
 * structure in memory
 *
 * @author Rafael Braz
 */
public class TreeXMLConverter {

    private final Tree<TokenAttributes> tree;

    public TreeXMLConverter() {
        tree = new Tree<>();
    }

    // starts the convertion
    public boolean convertFromFile(String inputPath) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(inputPath));
            if (document.hasChildNodes() && document.getFirstChild().getNodeName().equals("childElement")) {
                convertion(document.getFirstChild().getChildNodes(), tree.getRoot());
            }
            trees.simpletree.Node<TokenAttributes> newRoot = tree.getRoot().getChildren().get(0);
            newRoot.setParent(null);
            tree.setRoot(newRoot);
            return true;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            return false;
        }
    }

    // recursively converts the XML elements
    private void convertion(NodeList children, trees.simpletree.Node<TokenAttributes> parent) {
        for (int c = 0; c < children.getLength(); c++) {
            org.w3c.dom.Node tempXMLNode = children.item(c);
            if (tempXMLNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempXMLNode.getNodeName().equals("token") && tempXMLNode.hasAttributes()) {
                    trees.simpletree.Node<TokenAttributes> currentTreeNode = tree.createNode(parent);
                    Element elementNode = (Element) tempXMLNode;
                    try {
                        int column = Integer.parseInt(elementNode.getAttribute("column"));
                        int index = Integer.parseInt(elementNode.getAttribute("index"));
                        int line = Integer.parseInt(elementNode.getAttribute("line"));
                        String text = normalizeText(elementNode.getAttribute("text"));
                        int type = Integer.parseInt(elementNode.getAttribute("type"));
                        TokenAttributes tokenData;
                        if (column == TokenAttributes.ABSTRACT_NODE_COLUMN
                                && line == TokenAttributes.ABSTRACT_NODE_LINE
                                && index == TokenAttributes.ABSTRACT_NODE_COLUMN) {
                            tokenData = new UniversalToken(text, type);
                        } else {
                            tokenData = new ConcreteToken(index, text, type, line, column);
                        }
                        currentTreeNode.setNodeData(tokenData);
                        for (int i = c + 1; i < children.getLength(); i++) {
                            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                convertion(children.item(i).getChildNodes(), currentTreeNode);
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                return;
            }
        }
    }

    private String normalizeText(String text) {
        String res = text;
        res = res.replace("&#60;", "<");
        res = res.replace("&#62;", ">");
        res = res.replace("&#38;", "&");
        res = res.replace("&#39;", "'");
        res = res.replace("&#34;", "\"");
        return res;
    }

    public Tree<TokenAttributes> getTree() {
        return tree;
    }

}
