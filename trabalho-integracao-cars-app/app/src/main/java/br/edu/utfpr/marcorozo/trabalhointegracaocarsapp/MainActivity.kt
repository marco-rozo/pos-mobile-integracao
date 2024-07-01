package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.AppCars
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.theme.TrabalhoIntegracaoCarsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrabalhoIntegracaoCarsAppTheme {
                AppCars()
            }
        }
    }
}