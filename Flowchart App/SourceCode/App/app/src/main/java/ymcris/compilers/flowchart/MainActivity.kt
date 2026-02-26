package ymcris.compilers.flowchart
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.text.pdf.TextField
import ymcris.compilers.flowchart.backend.analyzer.Analyzer
import ymcris.compilers.flowchart.backend.application.engine.Engine
import ymcris.compilers.flowchart.backend.model.elements.TextFont

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // DEFAULT ---------------------------------------------------------------------------------
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // FRONTEND VARIABLES ----------------------------------------------------------------------
        val analyzeButton = findViewById<Button>(R.id.btnAnalyze)
        val operatorReportButton = findViewById<Button>(R.id.btnOperatorReport)
        val errorReportButton = findViewById<Button>(R.id.btnErrorReport)
        val structureReportButton = findViewById<Button>(R.id.btnStructureReport)
        val textPseudocodeField = findViewById<TextInputEditText>(R.id.editText)
        val console = findViewById<TextInputEditText>(R.id.txtConsole)
        var analyzed : Boolean = false;
        var errors: Boolean = false;
        var engine : Engine = Engine()

        // "ACTIONS LISTENERS" ---------------------------------------------------------------------
        analyzeButton.setOnClickListener {
            val pseudocode = textPseudocodeField.text.toString()

            if (pseudocode.isNotBlank()) {
                try {
                    console.setText("")

                    engine = Engine()
                    engine.createFlowchart(pseudocode)

                    console.setText("Successful")
                    analyzed = true;

                    if (engine.analyzer.par.errorList.size>0){
                        errors = true;
                    }else   {
                        errors = false;
                    }

                } catch (e: Exception) {
                    console.setText("Error: ${e.message}")
                    analyzed = false;
                }
            } else {
                textPseudocodeField.error = "Escribe algo para analizar:"
                analyzed = false;
            }
        }

        operatorReportButton.setOnClickListener{
            if (analyzed){
                if (errors){
                    console.setText("No puedes ver este reporte porque el pseudocodigo tiene errores")
                }else{
                    console.setText(engine.analyzer.getOperators())
                }
            }else{
                console.setText("Tienes que analizar un pseudocodigo primero")
            }
        }
        errorReportButton.setOnClickListener{
            if (analyzed){
                if (!errors){
                    console.setText("No puedes ver este reporte porque el pseudocodigo no tiene errores")
                }else{
                    console.setText(engine.analyzer.getErrors())
                }
            }else{
                console.setText("Tienes que analizar un pseudocodigo primero")
            }
        }
        structureReportButton.setOnClickListener{
            if (analyzed){
                if (errors){
                    console.setText("No puedes ver este reporte porque el pseudocodigo tiene errores")
                }else{
                    console.setText(engine.analyzer.getStructures())
                }
            }else{
                console.setText("Tienes que analizar un pseudocodigo primero")
            }
        }
    }
}