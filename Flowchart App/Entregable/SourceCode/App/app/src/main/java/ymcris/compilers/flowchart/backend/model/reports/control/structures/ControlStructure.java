package ymcris.compilers.flowchart.backend.model.reports.control.structures;

/**
 * The ControlStructure class is the class responsible for
 *
 * @author YmCris
 * @since Feb 25, 2026
 */
public class ControlStructure {

    // REFERENCE VARIABLES -----------------------------------------------------
    private String occurrence;
    private ControlStructureType type;

    // PRIMITIVE VARIABLES -----------------------------------------------------
    private int row;
    private int column;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public ControlStructure(String occurrence, ControlStructureType type,
            int row, int column) {

        this.occurrence = occurrence;
        this.type = type;
        this.row = row;
        this.column = column;
    }

    // GETTERS -----------------------------------------------------------------
    public String getOccurrence() {
        return occurrence;
    }

    public ControlStructureType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
