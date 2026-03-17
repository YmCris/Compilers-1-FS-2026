package ymcris.pkmforms.analyzer.tokens.errors;

/**
 * The ErrorToken class is the class responsible for an error token
 *
 * @author YmCris
 * @see ErrorType
 * @since Mar 14, 2026
 */
public class ErrorToken {

    // REFERENCE VARIABLES -----------------------------------------------------
    String lexeme;
    String description;
    ErrorType type;

    // PRIMITIVE VARIABLES -----------------------------------------------------
    int row;
    int column;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public ErrorToken(String lexeme, String message, ErrorType type, int row,
            int column) {
        this.lexeme = lexeme;
        this.description = message;
        this.type = type;
        this.row = row;
        this.column = column;
    }

    // GETTERS -----------------------------------------------------------------
    public String getLexeme() {
        return lexeme;
    }

    public String getDescription() {
        return description;
    }

    public ErrorType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
