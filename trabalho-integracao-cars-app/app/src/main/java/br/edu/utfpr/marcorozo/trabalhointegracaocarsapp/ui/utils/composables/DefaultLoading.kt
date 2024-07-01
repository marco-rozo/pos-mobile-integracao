package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.utils.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.R
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.theme.TrabalhoIntegracaoCarsAppTheme

@Composable
fun DefaultLoading(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(60.dp)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun LoadingCarsPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        DefaultLoading(
            text = stringResource(R.string.carregando)
        )
    }
}











