package ymcris.pkmforms.app.ui.render

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.SelectQuestion

@Composable
fun RenderSelectQuestion(q: SelectQuestion, key: String, vm: FormViewModel) {
    val selected = vm.answers[key] as? String ?: ""
    Column {
        Text(q.label)
        q.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selected == option,
                    onClick  = { vm.updateAnswer(key, option) }
                )
                Text(option)
            }
        }
    }
}