package ymcris.compilers.flowchart.backend.core.colors

class Color {

    fun getRgbColor(r: Int, g: Int, b: Int): String {
        return "rgb($r, $g, $b)"
    }

    fun getHexadecimalColor(r: Int, g: Int, b: Int): String {
        return "#%02x%02x%02x".format(r, g, b)
    }

}