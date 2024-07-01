package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.list.CarsListScreen
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.theme.TrabalhoIntegracaoCarsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrabalhoIntegracaoCarsAppTheme {
                CarsListScreen(
                    onCarPressed = {},
                )
            }
        }
    }
}