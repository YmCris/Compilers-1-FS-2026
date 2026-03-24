package ymcris.pkmforms.domain.backend.analyzer.symbols.table;

import java.util.Map;
import java.util.HashMap;
import ymcris.pkmforms.domain.backend.analyzer.symbols.Symbol;
import ymcris.pkmforms.domain.backend.analyzer.symbols.SymbolType;
import ymcris.pkmforms.domain.backend.analyzer.utilities.exceptions.VariableAlreadyExistsException;

/**
 * The SymbolTable class is the class responsible for be the semantic symbol
 * table
 *
 * @author YmCris
 * @since Mar 15, 2026
 */
public class SymbolTable {

    // REFERENCE VARIABLES -----------------------------------------------------
    private Map<String, Symbol> table;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public SymbolTable() {
        table = new HashMap<>();
    }

    // SPECIFIC METHODS --------------------------------------------------------
    public void addVariable(String variableName, SymbolType type, Object value,
            int row, int column) {

        if (table.containsKey(variableName)) {
        }

        Symbol symbol = new Symbol(variableName, type, value, row, column);

        table.put(variableName, symbol);

    }

    public Symbol getVariable(String variableName) {
        return table.get(variableName);
    }

    public void assignValueToVariable(String variableName, SymbolType type, Object newValue) {

        Symbol symbol = table.get(variableName);

        if (symbol == null) {
            throw new VariableAlreadyExistsException("The variable "
                    + variableName + " doesn't exist");
        }

        if (type != null) {
            symbol.setType(type);
        }

        symbol.setValue(newValue);
    }

    public void printTable() {
        for (Symbol symbol : table.values()) {
            System.out.println(
                    "Variable " + symbol.getIdentifier()
                    + " Type " + symbol.getType() + " = "
                    + " Value " + symbol.getValue()
            );
        }
    }

    // GETTERS -----------------------------------------------------------------
    public Map getTable() {
        return table;
    }

    public boolean exists(String name) {
        return table.containsKey(name);
    }

    public SymbolType getType(String name) {
        return table.get(name).getType();
    }

}
