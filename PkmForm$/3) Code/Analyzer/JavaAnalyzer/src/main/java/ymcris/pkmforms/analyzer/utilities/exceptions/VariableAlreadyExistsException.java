package ymcris.pkmforms.analyzer.utilities.exceptions;

/**
 * The VariableAlreadyExistsException class is the class responsible for
 *
 * @author YmCris
 * @since Mar 15, 2026
 */
public class VariableAlreadyExistsException extends RuntimeException {

    // REFERENCE VARIABLES -----------------------------------------------------
    private final String operation;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public VariableAlreadyExistsException(String operation, Throwable cause) {
        super(operation, cause);
        this.operation = operation;
    }
    
    public VariableAlreadyExistsException(String operation) {
        super(operation);
        this.operation = operation;
    }

    // GETTERS -----------------------------------------------------------------
    public String getOperation() {
        return operation;
    }

}
