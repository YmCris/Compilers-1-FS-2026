package ymcris.compilers.flowchart.backend.model.reports.errors;

/**
 * The ErrorToken class is the class responsible for
 *
 * @author YmCris
 * @since Feb 25, 2026
 */
public class ErrorToken {

    // REFERENCE VARIABLES -----------------------------------------------------
    String lexeme;
    String message;
    ErrorType type;

    // PRIMITIVE VARIABLES -----------------------------------------------------
    int row;
    int column;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public ErrorToken(String lexeme, String message, ErrorType type, int row,
            int column) {
        this.lexeme = lexeme;
        this.message = message;
        this.type = type;
        this.row = row;
        this.column = column;
    }

    // GETTERS -----------------------------------------------------------------
    public String getLexeme() {
        return lexeme;
    }

    public String getMessage() {
        return message;
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
