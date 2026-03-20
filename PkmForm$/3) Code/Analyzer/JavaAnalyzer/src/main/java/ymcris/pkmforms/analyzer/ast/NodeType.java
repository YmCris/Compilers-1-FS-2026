package ymcris.pkmforms.analyzer.ast;

/**
 * The enum NodeType is the enum responsible for representing
 *
 * @author YmCris
 * @since Mar 17, 2026
 */
public enum NodeType {

    // ENUMS -------------------------------------------------------------------
    //VARIABLES
    VAR_DECLARATION,
    VAR_INITIALIZATION,
    VAR_ASSIGNMENT,
    ASSIGN,
    // VAR TYPES / RESOURCES
    ID,
    NUMBER,
    STRING,
    TYPE_NUMBER,
    TYPE_STRING,
    // ARITHMETIC EXPRESIONS
    ADD, SUB, MUL, DIV, POW, MOD,
    NEG, POS,
    // RELATIONAL
    GT, GE, LT, LE, EQ, NE,
    // LOGICAL
    AND, OR,
    NOT,
    // SPECIAL QUESTIONS
    SPECIAL_INIT,
    OPEN_QUESTION,
    ATTR_LIST,
    ATTR,
    DRAW_CALL,
    PLACEHOLDER,
    // SECTIONS
    SECTION,
    ELEMENTS,
    ELEMENT_LIST,
    STYLE_BLOCK,
    STYLE,
    ORIENTATION,
    ARG_LIST,
    VERTICAL,
    HORIZONTAL,
    TABLE,
    TABLE_ELEMENTS,
    ROW,
    CELL

}
