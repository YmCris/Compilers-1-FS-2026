package ymcris.pkmforms.app.ui.render

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.MultipleQuestion
import ymcris.pkmforms.domain.model.forms.elements.texts.Text

@Composable
fun RenderMultipleQuestion(q: MultipleQuestion, key: String, vm: FormViewModel) {
    @Suppress("UNCHECKED_CAST")
    val selected = vm.answers[key] as? Set<String> ?: emptySet()
    Column {
        Text(q.label)
        q.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked         = option in selected,
                    onCheckedChange = { checked ->
                        val updated = if (checked) selected + option else selected - option
                        vm.updateAnswer(key, updated)
                    }
                )
                Text(option)
            }
        }
    }
}