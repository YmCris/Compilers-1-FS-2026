package ymcris.pkmforms.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.render.RenderElement
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.Form

@Composable
fun FormScreen(form: Form, vm: FormViewModel) {
    
    Column {
        form.elements.forEach { element ->
            RenderElement(element, vm)
        }
    }
}