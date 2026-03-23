package ymcris.pkmforms.domain.model.forms.style

import ymcris.pkmforms.domain.model.forms.style.borders.Border

data class Style(
    val color: String,
    val backgroundColor: String,
    val fontFamily: FontFamily,
    val textSize: Double,
    val border: Border
)