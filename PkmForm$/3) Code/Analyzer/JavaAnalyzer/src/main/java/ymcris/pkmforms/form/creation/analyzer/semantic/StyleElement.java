package ymcris.pkmforms.form.creation.analyzer.semantic;

/**
 * The StyleElement class is the class responsible for 
 *
 * @author YmCris
 * @since Mar 21, 2026
 */
public class StyleElement {

    // REFERENCE VARIABLES -----------------------------------------------------
    public String color;
    public String backgroundColor;
    public String fontFamily;
    public Object textSize;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    public StyleElement() {
    }

    public StyleElement(StyleElement parent) {
        this.color = parent.color;
        this.backgroundColor = parent.backgroundColor;
        this.fontFamily = parent.fontFamily;
        this.textSize = parent.textSize;
    }

    // SPECIFIC METHODS --------------------------------------------------------
    public void merge(StyleElement child) {
        if (child.color != null) {
            this.color = child.color;
        }
        if (child.backgroundColor != null) {
            this.backgroundColor = child.backgroundColor;
        }
        if (child.fontFamily != null) {
            this.fontFamily = child.fontFamily;
        }
        if (child.textSize != null) {
            this.textSize = child.textSize;
        }
    }
}
