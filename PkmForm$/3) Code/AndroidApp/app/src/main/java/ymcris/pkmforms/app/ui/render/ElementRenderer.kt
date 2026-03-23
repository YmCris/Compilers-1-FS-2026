package ymcris.pkmforms.app.ui.render

import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import ymcris.pkmforms.domain.model.forms.elements.sections.Section
import ymcris.pkmforms.domain.model.forms.elements.tables.Table

@Composable
fun RenderElement(element: Element, vm: FormViewModel) {
    
    when (element) {
        
        is Section -> RenderSection(element, vm)
        
        is Question -> RenderQuestion(element, vm)
        
        is Table -> RenderTable(element, vm)
        
        else -> {}
    }
}