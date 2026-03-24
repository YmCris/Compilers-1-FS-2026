package ymcris.pkmforms.domain.model.forms.style

enum class FontFamily {

    MONO,
    SANS_SERIF,
    CURSIVE;
    companion object {
        fun fromString(value: String): FontFamily {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: MONO
        }
    }

}