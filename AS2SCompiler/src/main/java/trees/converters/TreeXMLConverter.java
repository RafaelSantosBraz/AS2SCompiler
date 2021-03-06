/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.converters;

import trees.*;
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

/**
 * converts a XML file containing a tree (CST or eCST) into a simple tree
 * structure in memory
 *
 * @author Rafael Braz
 */
public class TreeXMLConverter {

    /**
     * Simple tree converted from XML.
     */
    private final Tree<TokenAttributes> tree;

    public TreeXMLConverter() {
        tree = new Tree<>();
    }

    /**
     * starts the convertion from XML.
     *
     * @param inputPath
     * @return
     */
    public boolean convertFromFile(String inputPath) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(inputPath));
            if (document.hasChildNodes() && document.getFirstChild().getNodeName().equals("childElement")) {
                convertion(document.getFirstChild().getChildNodes(), tree.getRoot());
            }
            trees.Node<TokenAttributes> newRoot = tree.getRoot().getChildren().get(0);
            newRoot.setParent(null);
            tree.setRoot(newRoot);
            return true;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            return false;
        }
    }

    /**
     * recursively converts the XML elements
     *
     * @param children children nodes of the current node.
     * @param parent the current node.
     */
    private void convertion(NodeList children, trees.Node<TokenAttributes> parent) {
        for (int c = 0; c < children.getLength(); c++) {
            org.w3c.dom.Node tempXMLNode = children.item(c);
            if (tempXMLNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempXMLNode.getNodeName().equals("token") && tempXMLNode.hasAttributes()) {
                    trees.Node<TokenAttributes> currentTreeNode = tree.createNode(parent);
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

    /**
     * normalize all the XML forbiden tokens.
     *
     * @param text
     * @return
     */
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
