package ymcris.pkmforms.analyzer.symbols;

/**
 * The Symbol class is the class responsible for a symbol for the symbol table
 *
 * @author YmCris
 * @since Mar 15, 2026
 */
public class Symbol {

    // REFERENCE VARIABLES -----------------------------------------------------
    private Object value;
    private SymbolType type;
    private String identifier;
    
    // PRIMITIVE VARIABLES -----------------------------------------------------
    private int row;
    private int column;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public Symbol(String identifier, SymbolType type, Object value, int row, int column) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
        this.row = row;
        this.column = column;
    }

    // SPECIFIC METHODS --------------------------------------------------------
    public boolean isNull() {
        return value == null;
    }

    // GETTERS -----------------------------------------------------------------
    public String getIdentifier() {
        return identifier;
    }

    public SymbolType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    // SETTERS -----------------------------------------------------------------
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
