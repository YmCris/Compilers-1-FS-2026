package ymcris.pkmforms.app.ui.render

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.elements.tables.Table

@Composable
fun RenderTable(table: Table, vm : FormViewModel) {
    
    Column {
        repeat(table.rows) { row ->
            
            Row {
                repeat(table.columns) { col ->
                    
                    val element = table.elements[row, col]
                    
                    if (element != null) {
                        RenderElement(element, vm)
                    }
                }
            }
        }
    }
}