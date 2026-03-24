package ymcris.pkmforms.domain.backend.analyzer.ast;

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
    CELL,
    TEXT,
    CONTENT,
    //
    OPTION_LIST,
    VALUE_LIST,
    WHO_IS_THAT_POKEMON,
    BLOCK,
    IF,
    ELSE,
    ELSE_IF,
    FOR,
    WHILE,
    DO_WHILE,
    INIT,
    UPDATE,
    FOR_RANGE,
    //QUESTION
    LABEL,
    DROP_QUESTION,
    MULTIPLE_QUESTION,
    SELECT_QUESTION,
    OPTIONS,
    OPTION,
    CORRECT_LIST,
    CORRECT,
    PROGRAM,
    STMT_LIST, 
    SECTION_BODY,
    COLOR_STYLE,
    COLOR_VALUE, 
    BACKGROUND_COLOR_STYLE, 
    BACKGROUND_COLOR, 
    TEXT_SIZE_STYLE, 
    TEXT_SIZE, 
    FONT_FAMILY_STYLE, 
    FONT_FAMILY, 
    BORDER, 
    BORDER_ATTR, 
    LINE_BORDER, 
    DOTTED_BORDER,
    DOUBLE_BORDER, TEXT_BODY, INTEGER

}
