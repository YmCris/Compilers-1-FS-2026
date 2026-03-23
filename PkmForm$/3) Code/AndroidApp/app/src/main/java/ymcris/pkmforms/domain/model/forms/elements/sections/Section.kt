package ymcris.pkmforms.domain.model.forms.elements.sections

import ymcris.pkmforms.domain.backend.core.interfaces.Positionable
import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.style.Style

data class Section(
    override val width: Double,
    override val height: Double,
    override val style : Style,
    override val position: Point,
    val orientation: OrientationType,
    val elements: List<Element> = emptyList(),
) : Element(width, height, style), Positionable