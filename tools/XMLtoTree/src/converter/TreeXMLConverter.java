/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import cstecst.*;
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
            if (document.hasChildNodes() && document.getFirstChild().getNodeName().equals("childElement")) {
                convertion(document.getFirstChild().getChildNodes(), tree.getRoot());
            }
            simpletree.Node<TokenAttributes> newRoot = tree.getRoot().getChildren().get(0);
            newRoot.setParent(null);
            tree.setRoot(newRoot);
            return true;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            return false;
        }
    }

    private void convertion(NodeList children, simpletree.Node<TokenAttributes> parent) {
        for (int c = 0; c < children.getLength(); c++) {
            org.w3c.dom.Node tempXMLNode = children.item(c);
            if (tempXMLNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempXMLNode.getNodeName().equals("token") && tempXMLNode.hasAttributes()) {
                    simpletree.Node<TokenAttributes> currentTreeNode = tree.createNode(parent);
                    Element elementNode = (Element) tempXMLNode;
                    try {
                        int column = Integer.parseInt(elementNode.getAttribute("column"));
                        int index = Integer.parseInt(elementNode.getAttribute("index"));
                        int line = Integer.parseInt(elementNode.getAttribute("line"));
                        String text = elementNode.getAttribute("text");
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

    public Tree<TokenAttributes> getTree() {
        return tree;
    }

}
