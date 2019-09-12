/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import trees.cstecst.TokenAttributes;

/**
 *
 * @author Rafael Braz
 */
public class XMLDOTConverter {

    public boolean convertFromFile(String inputPath, String outputPath) {
        TreeXMLConverter convXML = new TreeXMLConverter();
        if (!convXML.convertFromFile(inputPath)) {
            return false;
        }
        DOTConverter<TokenAttributes> convDOT = new DOTConverter<>(convXML.getTree());
        return convDOT.convertToFile(outputPath);
    }
}
