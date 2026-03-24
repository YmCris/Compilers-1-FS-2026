package ymcris.pkmforms.domain.backend.analyzer.ast;

/**
 * The AST class is the class responsible for the AST parser
 *
 * @see Node
 * @author YmCris
 * @since Mar 17, 2026
 */
public class AST {

    // REFERENCE VARIABLES -----------------------------------------------------
    private Node root;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public AST(Node root) {
        this.root = root;
    }
    
    // SPECIFIC METHODS --------------------------------------------------------
    public void print(){
        if (root!=null) {
            root.print("");
        }
    }

    // GETTERS -----------------------------------------------------------------
    public Node getRoot() {
        return root;
    }

}
