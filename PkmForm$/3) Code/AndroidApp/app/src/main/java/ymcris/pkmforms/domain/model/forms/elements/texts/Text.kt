package ymcris.pkmforms.domain.model.forms.elements.texts

import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.style.Style

data class Text (
    override val width: Double,
    override val height: Double,
    override val style : Style,
    val content: String
): Element(width, height, style)
