/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

/**
 * keeps information about the eCST nodes.
 *
 * @author Rafael Braz
 */
public class ECST {

    /**
     * represent the integer version of the eCST node.
     */
    public static final int LEAF_TYPE = -1,
            COMPILATION_UNIT_TYPE = 0,
            PACKAGE_DECL_TYPE = 1,
            CONCRETE_UNIT_DECL_TYPE = 2,
            VAR_DECL_TYPE = 3,
            TYPE_TYPE = 4;

    /**
     * represent the textual version of the eCST node.
     */
    public static final String COMPILATION_UNIT_LABEL = "COMPILATION_UNIT",
            PACKAGE_DECL_LABEL = "PACKAGE_DECL",
            CONCRETE_UNIT_DECL_LABEL = "CONCRETE_UNIT_DECL",
            VAR_DECL_LABEL = "VAR_DECL",
            TYPE_LABEL = "TYPE";
}
