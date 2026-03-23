package ymcris.pkmforms.app.ui.render

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.sections.OrientationType
import ymcris.pkmforms.domain.model.forms.elements.sections.Section

@Composable
fun RenderSection(section: Section, vm: FormViewModel) {
    
    when (section.orientation) {
        
        OrientationType.VERTICAL -> {
            Column {
                section.elements.forEach {
                    RenderElement(it, vm)
                }
            }
        }
        
        OrientationType.HORIZONTAL -> {
            Row {
                section.elements.forEach {
                    RenderElement(it, vm)
                }
            }
        }
    }
}