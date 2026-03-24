package ymcris.pkmforms.app.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ymcris.pkmforms.domain.backend.form.creation.FormCreator
import ymcris.pkmforms.domain.model.forms.Form

class FormViewModel : ViewModel() {
    
    var form by mutableStateOf<Form?>(null)
        private set
    
    val answers = mutableStateMapOf<String, Any>()
    
    fun loadForm(code: String) {
        val formCreator = FormCreator()
        form = formCreator.createForm(code)
    }
    
    fun updateAnswer(key: String, value: Any) {
        answers[key] = value
    }
}