package ymcris.pkmforms.app.ui.render

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.DropQuestion

@Composable
fun RenderDropQuestion(q: DropQuestion, key: String, vm: FormViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val selected = vm.answers[key] as? String ?: ""
    Column {
        Text(q.label)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(if (selected.isEmpty()) "Selecciona..." else selected)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                q.options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            vm.updateAnswer(key, option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}