package ymcris.pkmforms.domain.model.forms.elements.tables

import ymcris.pkmforms.domain.backend.core.interfaces.Positionable
import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.elements.sections.Point
import ymcris.pkmforms.domain.model.forms.style.Style

data class Table (
    override val width: Double,
    override val height: Double,
    override val style : Style,
    override val position: Point,
    val rows: Int,
    val columns: Int,
    val elements: Grid<Element>
): Element(width, height, style) , Positionable