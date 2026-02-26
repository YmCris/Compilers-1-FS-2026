package ymcris.compilers.flowchart.backend.operators;

/**
 * The Operator class is the class responsible for
 *
 * @author YmCris
 * @since Feb 25, 2026
 */
public class Operator {

    // REFERENCE VARIABLES -----------------------------------------------------
    private String occurrence;
    private OperatorType type;

    // PRIMITIVE VARIABLES -----------------------------------------------------
    private int row;
    private int column;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public Operator(String occurrence, OperatorType type, int row, int column) {
        this.occurrence = occurrence;
        this.type = type;
        this.row = row;
        this.column = column;
    }

    // GETTERS -----------------------------------------------------------------
    public String getOccurrence() {
        return occurrence;
    }

    public OperatorType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
