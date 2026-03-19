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
    
    // VAR TYPES / RESOURCES
    ID,
    NUMBER,
    STRING,
    TYPE_NUMBER,
    TYPE_STRING;

}
