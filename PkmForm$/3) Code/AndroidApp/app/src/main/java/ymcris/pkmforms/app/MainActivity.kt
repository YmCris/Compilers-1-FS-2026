package ymcris.pkmforms.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import ymcris.pkmforms.app.ui.FormScreen
import ymcris.pkmforms.app.ui.state.FormViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btnLoad)
        val editText = findViewById<EditText>(R.id.editTextTextMultiLine)
        button.setOnClickListener {
            val code = editText.text.toString()
            
            if (code.isBlank()){
                editText.error = "Field can't be empty"
                return@setOnClickListener
            }
            
            showComposeScreen(code)
        }
    }
    
    private fun showComposeScreen(code: String) {
        setContent {
            val vm: FormViewModel = viewModel()
            
            LaunchedEffect(Unit) {
                vm.loadForm(code)
            }
            
            vm.form?.let {
                FormScreen(it, vm)
            }
        }
    }
}