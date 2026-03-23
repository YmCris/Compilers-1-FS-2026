package ymcris.pkmforms.domain.model.forms.elements

import ymcris.pkmforms.domain.model.forms.style.Style

abstract class Element(
    open val width: Double,
    open val height: Double,
    open val style : Style
)