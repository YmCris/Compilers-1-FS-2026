package ymcris.pkmforms.domain.model.forms.elements.questions;

import ymcris.pkmforms.domain.model.forms.style.Style;
import ymcris.pkmforms.domain.model.forms.elements.Element;

open class Question(
    width: Double,
    height: Double,
    style: Style,
    open val label: String
) : Element(width, height, style)