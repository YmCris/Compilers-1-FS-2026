package ymcris.pkmforms.domain.model.forms.elements.tables

import ymcris.pkmforms.domain.model.forms.elements.Element

class Grid(val rows: Int, val columns: Int) {
    
    private val data: List<MutableList<Element?>> =
        List(rows) { MutableList(columns) { null } }
    
    operator fun get(row: Int, col: Int): Element? = data[row][col]
    operator fun set(row: Int, col: Int, value: Element?) {
        data[row][col] = value
    }
}
