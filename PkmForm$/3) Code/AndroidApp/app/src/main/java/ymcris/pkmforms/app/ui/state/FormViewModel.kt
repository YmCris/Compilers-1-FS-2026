package ymcris.pkmforms.app.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ymcris.pkmforms.domain.model.forms.Form

class FormViewModel : ViewModel() {
    
    var form by mutableStateOf<Form?>(null)
        private set
    
    // Aquí guardas TODAS las respuestas
    val answers = mutableStateMapOf<String, Any>()
    
    fun loadForm() {
        form = fakeForm() // luego conectas tu Controller
    }
    
    fun updateAnswer(key: String, value: Any) {
        answers[key] = value
    }
}