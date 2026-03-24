package ymcris.pkmforms.app.ui.render

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.OpenQuestion

@Composable
fun RenderOpenQuestion(q: OpenQuestion, key: String, vm: FormViewModel) {
    val value = vm.answers[key] as? String ?: ""
    Column {
        Text(q.label)
        TextField(
            value = value,
            onValueChange = { vm.updateAnswer(key, it) },
            placeholder = { Text("Escribe tu respuesta...") }
        )
    }
}