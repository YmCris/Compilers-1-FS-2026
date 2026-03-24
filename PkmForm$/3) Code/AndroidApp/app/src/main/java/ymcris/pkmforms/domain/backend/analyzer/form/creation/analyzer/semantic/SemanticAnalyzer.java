/**
 * To understand the semantic look the restrictions in:
 * Base Repository -> 4) Test -> rules.txt
 */
package ymcris.pkmforms.domain.backend.analyzer.form.creation.analyzer.semantic;

import ymcris.pkmforms.domain.backend.analyzer.ast.Node;
import ymcris.pkmforms.domain.backend.analyzer.ast.NodeType;
import ymcris.pkmforms.domain.backend.analyzer.symbols.SymbolType;
import ymcris.pkmforms.domain.backend.analyzer.tokens.errors.ErrorType;
import ymcris.pkmforms.domain.backend.analyzer.tokens.errors.ErrorToken;
import ymcris.pkmforms.domain.backend.analyzer.symbols.table.SymbolTable;

import java.util.List;

/**
 * The SemanticAnalyzer class is the class responsible for make the semantic
 * analisis
 *
 * @author YmCris
 * @see Node
 * @see SymbolTable
 * @since Mar 21, 2026
 */
public class SemanticAnalyzer {

    // REFERENCE VARIABLES -----------------------------------------------------
    private SymbolTable table;
    private List<ErrorToken> errors;
    private boolean insideContainer = false;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public SemanticAnalyzer(SymbolTable table, List<ErrorToken> errors) {
        this.table = table;
        this.errors = errors;
    }

    // SPECIFIC METHODS --------------------------------------------------------
    public void analyze(Node root) {
        visitNode(root, "BLACK", "WHITE");
    }

    private void visitNode(Node node, String inheritedColor, String inheritedBg) {
        if (node == null) {
            return;
        }
        switch (node.getType()) {
            case PROGRAM, STMT_LIST, BLOCK -> {
                for (Node child : node.getChildren()) {
                    visitNode(child, inheritedColor, inheritedBg);
                }
            }
            case VAR_DECLARATION ->
                visitVarDeclaration(node);
            case VAR_INITIALIZATION ->
                visitVarInitialization(node);
            case VAR_ASSIGNMENT ->
                visitVarAssignment(node);
            case SPECIAL_INIT ->
                visitSpecialInit(node);
            case DRAW_CALL ->
                visitDrawCall(node);
            case SECTION ->
                visitSection(node, inheritedColor, inheritedBg);
            case TABLE ->
                visitTable(node, inheritedColor, inheritedBg);
            case TEXT ->
                visitText(node, inheritedColor, inheritedBg);
            case OPEN_QUESTION, DROP_QUESTION, SELECT_QUESTION, MULTIPLE_QUESTION ->
                visitQuestion(node, inheritedColor, inheritedBg);
            case IF ->
                visitIf(node, inheritedColor, inheritedBg);
            case ELSE_IF ->
                visitElseIf(node, inheritedColor, inheritedBg);
            case ELSE ->
                visitNode(node.getChildren().get(0), inheritedColor, inheritedBg);
            case WHILE ->
                visitWhile(node, inheritedColor, inheritedBg);
            case DO_WHILE ->
                visitDoWhile(node, inheritedColor, inheritedBg);
            case FOR ->
                visitFor(node, inheritedColor, inheritedBg);
            case FOR_RANGE ->
                visitForRange(node, inheritedColor, inheritedBg);
            default -> {
                for (Node child : node.getChildren()) {
                    visitNode(child, inheritedColor, inheritedBg);
                }
            }
        }
    }

    // VARIABLES RESTRICTIONS
    private void visitVarDeclaration(Node node) {
        Node nodeType = node.getChildren().get(0);
        Node nodeId = node.getChildren().get(1);
        String variableName = (String) nodeId.getValue();

        if (table.exists(variableName)) {
            addError("The variable: " + variableName + " already was declarated", node);
            return;
        }

        SymbolType variableType = nodeType.getType() == NodeType.TYPE_NUMBER
                ? SymbolType.NUMBER : SymbolType.STRING;
        Object defaultValue = variableType == SymbolType.NUMBER ? 0 : "";
        table.addVariable(variableName, variableType, defaultValue, 0, 0);
    }

    private void visitVarInitialization(Node node) {
        Node nodeType = node.getChildren().get(0);
        Node nodeId = node.getChildren().get(1);
        Node nodeValue = node.getChildren().get(3);
        String name = (String) nodeId.getValue();

        if (table.exists(name)) {
            addError("The variable: " + name + " already was declarated", node);
            return;
        }

        SymbolType declared = nodeType.getType() == NodeType.TYPE_NUMBER
                ? SymbolType.NUMBER : SymbolType.STRING;
        SymbolType assigned = inferType(nodeValue);

        if (assigned != null && assigned != declared) {
            addError("Incorrect variable type in " + name + " variable, excepted: "
                    + declared + " founded: " + assigned + ".", node);
            return;
        }
        table.addVariable(name, declared, nodeValue.getValue(), 0, 0);
    }

    private void visitVarAssignment(Node node) {
        Node nodeId = node.getChildren().get(0);
        Node nodeValue = node.getChildren().get(2);
        String name = (String) nodeId.getValue();

        if (!table.exists(name)) {
            addError("The variable " + name + " have not declarated", node);
            return;
        }
        if (table.getType(name) == SymbolType.SPECIAL) {
            addError("An special variable can't be reasigned " + name + ".", node);
            return;
        }

        SymbolType declared = table.getType(name);
        SymbolType assigned = inferType(nodeValue);
        if (assigned != null && assigned != declared) {
            addError("Incorrect type in " + name + " variable: type is " + declared
                    + " but was assigned: " + assigned + ".", node);
        }
    }

    private void visitSpecialInit(Node node) {
        Node nodeId = node.getChildren().get(0);
        Node questionNode = node.getChildren().get(1);
        String name = (String) nodeId.getValue();

        if (table.exists(name)) {
            addError("The special variable " + name + " already was declarated", node);
            return;
        }

        NodeType qN = questionNode.getType();
        if (qN != NodeType.OPEN_QUESTION && qN != NodeType.DROP_QUESTION
                && qN != NodeType.SELECT_QUESTION && qN != NodeType.MULTIPLE_QUESTION) {
            addError("The special variable " + name + " only can save questions", node);
            return;
        }

        int placeholders = countNodes(questionNode, NodeType.PLACEHOLDER);
        table.addVariable(name, SymbolType.SPECIAL, null, 0, 0);
        table.getVariable(name).setPlaceholderCount(placeholders);
    }

    // DRAW CALL RESTRICTIONS
    private void visitDrawCall(Node node) {
        Node nodeId = node.getChildren().get(0);
        Node argumentList = node.getChildren().get(1);
        String name = (String) nodeId.getValue();

        if (!table.exists(name)) {
            addError("The variable " + name + " have not been declarated", node);
            return;
        }
        if (table.getType(name) != SymbolType.SPECIAL) {
            addError("Only special variables can use .draw() function", node);
            return;
        }

        int expected = table.getVariable(name).getPlaceholderCount();
        int actual = argumentList.getChildren().size();
        if (actual != expected) {
            addError("The special variable " + name + ".draw() received" + actual
                    + " argumentos but it only can use "
                    + expected + " placeholders", node);
        }
    }

    // EXPRESSIONS RESTRICTIONS
    private SymbolType visitArithmeticExpression(Node node) {
        if (node == null) {
            return null;
        }
        switch (node.getType()) {
            case NUMBER -> {
                return SymbolType.NUMBER;
            }
            case STRING -> {
                return SymbolType.STRING;
            }
            case ID -> {
                String name = (String) node.getValue();
                if (!table.exists(name)) {
                    addError("Variable " + name + " has not been declarated", node);
                    return null;
                }
                if (table.getType(name) == SymbolType.SPECIAL) {
                    addError("Special variable " + name
                            + " can't be used in arithmetic expressions", node);
                    return null;
                }
                return table.getType(name);
            }
            case NEG, POS -> {
                SymbolType type = visitArithmeticExpression(node.getChildren().get(0));
                if (type == SymbolType.STRING) {
                    addError("Unarian operation can't be applied to string variables", node);
                }
                return SymbolType.NUMBER;
            }
            case ADD, SUB, MUL, DIV, POW, MOD -> {
                SymbolType left = visitArithmeticExpression(node.getChildren().get(0));
                SymbolType right = visitArithmeticExpression(node.getChildren().get(1));
                if (left == SymbolType.STRING || right == SymbolType.STRING) {
                    addError("The arithmetic operation can't have string variables", node);
                }
                return SymbolType.NUMBER;
            }
            default -> {
                for (Node child : node.getChildren()) {
                    visitArithmeticExpression(child);
                }
                return null;
            }
        }
    }

    private void visitLogic(Node node) {
        if (node == null) {
            return;
        }
        checkMixedOperators(node, null);
    }

    private void checkMixedOperators(Node node, NodeType dominant) {
        if (node == null) {
            return;
        }
        switch (node.getType()) {
            case AND, OR -> {
                if (dominant != null && dominant != node.getType()) {
                    addError("You can't mix && and || in the same expression", node);
                    return;
                }
                for (Node child : node.getChildren()) {
                    checkMixedOperators(child, node.getType());
                }
            }
            case NOT -> {
                for (Node child : node.getChildren()) {
                    checkMixedOperators(child, dominant);
                }
            }
            case GT, GE, LT, LE, EQ, NE -> {
                SymbolType left = visitArithmeticExpression(node.getChildren().get(0));
                SymbolType right = visitArithmeticExpression(node.getChildren().get(1));
                if (left == SymbolType.STRING) {
                    addError("Left Operator comparator can't be a string", node);
                }
                if (right == SymbolType.STRING) {
                    addError("Right Operator comparator can't be a string", node);
                }
            }
            default ->
                visitArithmeticExpression(node);
        }
    }

    // SECTION RESTRICTIONS
    private void visitSection(Node node, String color, String bg) {
        boolean wasContainer = insideContainer;
        insideContainer = true;

        Node body = getChild(node, NodeType.SECTION_BODY);
        if (body == null) {
            insideContainer = wasContainer;
            return;
        }

        Node styleBlock = getChild(body, NodeType.STYLE_BLOCK);
        String[] style = resolveStyle(styleBlock, color, bg);
        String myColor = style[0];
        String myBg = style[1];

        Node attrList = getChild(body, NodeType.ATTR_LIST);
        if (attrList != null) {
            validatePositiveAttrs(attrList);
        }

        Node elements = getChild(body, NodeType.ELEMENTS);
        if (elements != null) {
            Node list = elements.getChildren().isEmpty()
                    ? null : elements.getChildren().get(0);
            if (list != null && list.getChildren().isEmpty()) {
                addError("in SECTION elements can't be empty", node);
            } else if (list != null) {
                for (Node el : list.getChildren()) {
                    visitNode(el, myColor, myBg);
                }
            }
        }

        insideContainer = wasContainer;
    }

    // TABLE RESTRICTIONS
    private void visitTable(Node node, String color, String bg) {
        boolean wasContainer = insideContainer;
        insideContainer = true;

        Node styleBlock = getChild(node, NodeType.STYLE_BLOCK);
        String[] style = resolveStyle(styleBlock, color, bg);

        Node attrList = getChild(node, NodeType.ATTR_LIST);
        if (attrList != null) {
            validatePositiveAttrs(attrList);
        }

        Node tableElements = getChild(node, NodeType.TABLE_ELEMENTS);
        if (tableElements != null) {

            List<Node> rows = new java.util.ArrayList<>();
            collectRows(tableElements, rows);

            if (!rows.isEmpty()) {
                int expected = countCells(rows.get(0));
                for (int i = 1; i < rows.size(); i++) {
                    int actual = countCells(rows.get(i));
                    if (actual != expected) {
                        addError("TABLE: row " + (i + 1) + " have " + actual
                                + " columns, expected" + expected, rows.get(i));
                    }
                }

                for (Node row : rows) {
                    for (Node child : row.getChildren()) {
                        if (child.getType() == NodeType.CELL) {
                            for (Node el : child.getChildren()) {
                                visitNode(el, style[0], style[1]);
                            }
                        }
                    }
                }
            }
        }

        insideContainer = wasContainer;
    }

    // TEXT RESTRICTIONS
     private void visitText(Node node, String color, String bg) {
        if (!insideContainer) {
            addError("TEXT have to be inside of SECTION or TABLE.", node);
        }

        Node body = getChild(node, NodeType.TEXT_BODY);
        if (body == null || getChild(body, NodeType.CONTENT) == null) {
            addError("TEXT element have to have content attribute", node);
        }

    }

    // QUESTIONS RESTRICTIONS
    private void visitQuestion(Node node, String color, String bg) {
        if (!insideContainer) {
            addError(node.getType() + " have to be inside of SECTION or TABLE.", node);
            return;
        }

        if (getChild(node, NodeType.LABEL) == null) {
            addError(node.getType() + " label attribute is obligatory", node);
        }

        Node styleBlock = getChild(node, NodeType.STYLE_BLOCK);
        resolveStyle(styleBlock, color, bg);

        switch (node.getType()) {
            case OPEN_QUESTION -> {
            }
            case DROP_QUESTION ->
                validateDropQuestion(node);
            case SELECT_QUESTION ->
                validateSelectQuestion(node);
            case MULTIPLE_QUESTION ->
                validateMultipleQuestion(node);
            default -> {
            }
        }
    }

    private void validateDropQuestion(Node node) {
        Node options = getChild(node, NodeType.OPTIONS);
        if (options == null) {
            addError("DROP_QUESTION: options is obligatory", node);
            return;
        }
        validateOptions(options, node);

        Node correct = getChild(node, NodeType.CORRECT);
        if (correct != null) {
            validateCorrectIndex(options, correct, "DROP_QUESTION");
        }
    }

    private void validateSelectQuestion(Node node) {
        Node options = getChild(node, NodeType.OPTIONS);
        if (options == null) {
            addError("SELECT_QUESTION: options is obligatory", node);
            return;
        }
        validateOptions(options, node);

        int count = countOptions(options);
        if (count > 5 && count != Integer.MAX_VALUE) {
            warn("SELECT_QUESTION have " + count + " options", node);
        }

        Node correct = getChild(node, NodeType.CORRECT);
        if (correct != null) {
            validateCorrectIndex(options, correct, "SELECT_QUESTION");
        }
    }

    private void validateMultipleQuestion(Node node) {
        Node options = getChild(node, NodeType.OPTIONS);
        if (options == null) {
            addError("MULTIPLE_QUESTION: options is obligatory", node);
            return;
        }
        validateOptions(options, node);

        int total = countOptions(options);
        Node correctList = getChild(node, NodeType.CORRECT_LIST);

        if (correctList != null && total != Integer.MAX_VALUE) {

            Node valueList = correctList.getChildren().isEmpty()
                    ? null : correctList.getChildren().get(0);

            if (valueList != null) {

                for (Node val : valueList.getChildren()) {

                    double index = convertToDoube(val);
                    if (index < 0 || index != Math.floor(index)) {
                        addError("MULTIPLE_QUESTION: index in correct attribute have to be a positive number", val);
                    } else if ((int) index >= total) {
                        addError("MULTIPLE_QUESTION: index " + (int) index
                                + " out of range (0-" + (total - 1) + ").", val);
                    }
                }
            }
        }
    }

    // CODE BLOCKS RESTRICTIONS
    private void visitIf(Node node, String color, String bg) {
        visitLogic(node.getChildren().get(0));
        for (Node child : node.getChildren()) {
            visitNode(child, color, bg);
        }
    }

    private void visitElseIf(Node node, String color, String bg) {
        visitLogic(node.getChildren().get(0));
        visitNode(node.getChildren().get(1), color, bg);
    }

    private void visitWhile(Node node, String color, String bg) {
        visitLogic(node.getChildren().get(0));
        visitNode(node.getChildren().get(1), color, bg);
    }

    private void visitDoWhile(Node node, String color, String bg) {
        visitNode(node.getChildren().get(0), color, bg);
        visitLogic(node.getChildren().get(1));
    }

    private void visitFor(Node node, String color, String bg) {
        Node initNode = node.getChildren().get(0);
        Node nodeId = initNode.getChildren().get(0);
        String name = (String) nodeId.getValue();

        if (table.exists(name)) {
            if (table.getType(name) != SymbolType.NUMBER) {
                addError("FOR: " + name + " alredy exists and it isn't a number", nodeId);
            }
        } else {
            table.addVariable(name, SymbolType.NUMBER, 0, 0, 0);
        }

        visitLogic(node.getChildren().get(1));
        visitNode(node.getChildren().get(3), color, bg);
    }

    private void visitForRange(Node node, String color, String bg) {
        Node nodeId = node.getChildren().get(0);
        String name = (String) nodeId.getValue();

        if (table.exists(name)) {
            if (table.getType(name) != SymbolType.NUMBER) {
                addError("FOR IN: " + name + " alredy exists and it isn't a number", nodeId);
            }
        } else {
            table.addVariable(name, SymbolType.NUMBER, 0, 0, 0);
        }

        visitArithmeticExpression(node.getChildren().get(1));
        visitArithmeticExpression(node.getChildren().get(2));
        visitNode(node.getChildren().get(3), color, bg);
    }

    // HELPERS METHODS
    private String[] resolveStyle(Node styleBlock, String parentColor, String parentBg) {
        String color = parentColor;
        String bg = parentBg;
        if (styleBlock == null) {
            return new String[]{color, bg};
        }

        for (Node style : flattenStyleBlock(styleBlock)) {
            switch (style.getType()) {
                case COLOR_STYLE -> {
                    if (!style.getChildren().isEmpty()) {
                        color = getString(style.getChildren().get(0));
                    }
                }
                case BACKGROUND_COLOR_STYLE -> {
                    if (!style.getChildren().isEmpty()) {
                        bg = getString(style.getChildren().get(0));
                    }
                }
                case TEXT_SIZE_STYLE -> {
                    double v = convertToDoube(style.getChildren().isEmpty()
                            ? null : style.getChildren().get(0));
                    if (v <= 0) {
                        addError("text size have to be positive", style);
                    }
                }
                case BORDER -> {
                    if (!style.getChildren().isEmpty()) {
                        Node attr = style.getChildren().get(0);
                        if (!attr.getChildren().isEmpty()) {
                            double v = convertToDoube(attr.getChildren().get(0));
                            if (v <= 0) {
                                addError("The 'border' thickness must be positive", style);
                            }
                        }
                    }
                }
                default -> {
                }
            }
        }
        return new String[]{color, bg};
    }

    private List<Node> flattenStyleBlock(Node node) {
        List<Node> result = new java.util.ArrayList<>();
        for (Node child : node.getChildren()) {
            if (child.getType() == NodeType.STYLE_BLOCK) {
                result.addAll(flattenStyleBlock(child));
            } else {
                result.add(child);
            }
        }
        return result;
    }

    private void validateOptions(Node optionsNode, Node parent) {
        if (optionsNode.getChildren().isEmpty()) {
            addError("options can't be empty", parent);
            return;
        }
        Node first = optionsNode.getChildren().get(0);
        if (first.getType() == NodeType.WHO_IS_THAT_POKEMON) {
            List<Node> args = first.getChildren();
            if (args.size() >= 2) {
                double n = convertToDoube(args.get(0));
                double m = convertToDoube(args.get(1));
                if (n <= 0 || m <= 0) {
                    addError("who_is_that_pokemon: ranges must be positive", first);
                }
                if (n > m) {
                    addError("who_is_that_pokemon: initial range (" + (int) n
                            + ") greater in the final (" + (int) m + ")", first);
                }
            }
        } else if (first.getType() == NodeType.OPTION_LIST
                && first.getChildren().isEmpty()) {
            addError("options can't be empty", parent);
        }
    }

    private void validateCorrectIndex(Node optionsNode, Node correctNode, String questionType) {
        int total = countOptions(optionsNode);
        if (total == Integer.MAX_VALUE) {
            return;
        }
        Node expression = correctNode.getChildren().isEmpty()
                ? null : correctNode.getChildren().get(0);
        if (expression == null) {
            return;
        }
        double val = convertToDoube(expression);
        if (val < 0 || val != Math.floor(val)) {
            addError(questionType + ": correct must be positive", correctNode);
            return;
        }
        if ((int) val >= total) {
            addError(questionType + ": correct = " + (int) val
                    + " out of range (0-" + (total - 1) + ").", correctNode);
        }
    }

    private int countOptions(Node optionsNode) {
        if (optionsNode.getChildren().isEmpty()) {
            return 0;
        }
        Node first = optionsNode.getChildren().get(0);
        if (first.getType() == NodeType.WHO_IS_THAT_POKEMON) {
            return Integer.MAX_VALUE;
        }
        if (first.getType() == NodeType.OPTION_LIST) {
            return first.getChildren().size();
        }
        return 0;
    }

    private void validatePositiveAttrs(Node attrList) {
        for (Node attr : attrList.getChildren()) {
            if (attr.getType() != NodeType.ATTR) {
                continue;
            }
            Node val = attr.getChildren().isEmpty() ? null : attr.getChildren().get(0);
            if (val != null && isLiteralNumber(val)) {
                double v = convertToDoube(val);
                if (v < 0) {
                    addError("Attribute " + attr.getValue() + " must be positive", attr);
                }
            }
        }
    }

    private boolean isLiteralNumber(Node node) {
        return node.getType() == NodeType.NUMBER;
    }

    private void collectRows(Node node, List<Node> rows) {
        for (Node child : node.getChildren()) {
            if (child.getType() == NodeType.ROW) {
                rows.add(child);
            } else {
                collectRows(child, rows);
            }
        }
    }

    private int countCells(Node row) {
        int count = 0;
        for (Node child : row.getChildren()) {
            if (child.getType() == NodeType.CELL) {
                count++;
            } else if (child.getType() == NodeType.ROW) {
                count += countCells(child);
            }
        }
        return count;
    }

    private int countNodes(Node node, NodeType type) {
        int count = node.getType() == type ? 1 : 0;
        for (Node child : node.getChildren()) {
            count += countNodes(child, type);
        }
        return count;
    }

    private SymbolType inferType(Node node) {
        if (node == null) {
            return null;
        }
        switch (node.getType()) {
            case NUMBER -> {
                return SymbolType.NUMBER;
            }
            case STRING -> {
                return SymbolType.STRING;
            }
            case ADD, SUB, MUL, DIV, POW, MOD, NEG, POS -> {
                return SymbolType.NUMBER;
            }
            case ID -> {
                String name = (String) node.getValue();
                return table.exists(name) ? table.getType(name) : null;
            }
            default -> {
                return null;
            }
        }
    }

    private Node getChild(Node node, NodeType type) {
        for (Node child : node.getChildren()) {
            if (child.getType() == type) {
                return child;
            }
            Node found = getChild(child, type);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private double convertToDoube(Node node) {
        if (node == null) {
            return 0;
        }
        if (node.getType() == NodeType.TEXT_SIZE && !node.getChildren().isEmpty()) {
            return convertToDoube(node.getChildren().get(0));
        }
        Object v = node.getValue();
        if (v instanceof Double aDouble) {
            return aDouble;
        }
        if (v instanceof Integer integer) {
            return integer.doubleValue();
        }
        if (v instanceof Number number) {
            return number.doubleValue();
        }
        return 0;
    }

    private String getString(Node node) {
        if (node == null || node.getValue() == null) {
            return null;
        }
        return node.getValue().toString();
    }

    private void addError(String message, Node node) {
        errors.add(new ErrorToken(
                node != null && node.getValue() != null ? node.getValue().toString() : "?",
                message, ErrorType.SEMANTIC, 0, 0
        ));
    }

    private void warn(String message, Node node) {
        errors.add(new ErrorToken(
                node != null && node.getValue() != null ? node.getValue().toString() : "?",
                message, ErrorType.SEMANTIC, 0, 0
        ));
    }
}
