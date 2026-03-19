package ymcris.pkmforms.analyzer.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * The Node class is the class responsible for be an node of the three
 *
 * @author YmCris
 * @since Mar 17, 2026
 */
public class Node {

    // REFERENCE VARIABLES -----------------------------------------------------
    private Object value;
    private NodeType type;
    private List<Node> children;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public Node(NodeType type, Object value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    // SPECIFIC METHODS --------------------------------------------------------
    public void addChild(Node child) {
        this.children.add(child);
    }

    public void print(String indent) {
        System.out.println(indent + this);
        for (Node child : children) {
            child.print(indent + "  ");
        }
    }

    // GETTERS -----------------------------------------------------------------
    public NodeType getType() {
        return type;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Object getValue() {
        return value;
    }

    // SETTERS -----------------------------------------------------------------
    public void setValue(Object value) {
        this.value = value;
    }

}
