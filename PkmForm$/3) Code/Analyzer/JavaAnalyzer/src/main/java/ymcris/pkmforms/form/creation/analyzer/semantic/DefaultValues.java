package ymcris.pkmforms.form.creation.analyzer.semantic;

/**
 * The enum DefaultValues is the enum responsible for representing
 *
 * @author YmCris
 * @since Mar 21, 2026
 */
public enum DefaultValues {

    // ENUMS -------------------------------------------------------------------
    BACKGROUND_COLOR("WHITE"),
    LETER("BLACK");

    // PRIMITIVE VARIABLES -----------------------------------------------------
    private String color;

    // CONSTRUCTOR METHOD ------------------------------------------------------
    private DefaultValues(String color) {
        this.color = color;
    }

}
