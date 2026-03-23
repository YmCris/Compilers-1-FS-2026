package ymcris.pkmforms.app.ui.render

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import androidx.compose.material3.Text

@Composable
fun RenderQuestion(q: Question, vm: FormViewModel) {
    
    // clave única (puedes mejorar esto luego)
    val key = System.identityHashCode(q).toString()
    
    val value = vm.answers[key] as? String ?: ""
    
    Column {
        
        Text(q.label)
        
        TextField(
            value = value,
            onValueChange = {
                vm.updateAnswer(key, it)
            }
        )
    }
}