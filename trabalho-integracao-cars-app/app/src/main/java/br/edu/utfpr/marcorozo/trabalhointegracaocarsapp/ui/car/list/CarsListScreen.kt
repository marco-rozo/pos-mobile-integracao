package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.R
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.Car
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.theme.TrabalhoIntegracaoCarsAppTheme

@Composable
fun CarsListScreen(
    modifier: Modifier = Modifier,
    viewModel: CarsListViewModel = viewModel(),
    onAddPressed: () -> Unit,
    onCarPressed: (Car) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CarsTopBar(
                onRefresh = viewModel::load,
                showRefreashAction = viewModel.uiState.success
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.success) {
                FloatingActionButton(onClick = onAddPressed) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.adicionar)
                    )
                }
            }
        },
    ) { paddingValues ->
        if (viewModel.uiState.loading) {
            LoadingCars(modifier = Modifier.padding(paddingValues))
        } else if (viewModel.uiState.hasError) {
            ErrorLoadingCars(
                modifier = Modifier.padding(paddingValues),
                onTryAgainPressed = viewModel::load
            )
        } else {
            CarsList(
                modifier = Modifier.padding(paddingValues),
                cars = viewModel.uiState.cars,
                onCarPressed = onCarPressed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarsTopBar(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    showRefreashAction: Boolean
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = "Cars") },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.atualizar)
                )
            }
        }
    )
}

@Composable
private fun LoadingCars(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(40.dp)
        )
        Text(
            modifier = Modifier.padding(top = 9.dp),
            text = "Carregando...",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun LoadingCarsPreview(modifier: Modifier = Modifier) {
    TrabalhoIntegracaoCarsAppTheme {
        LoadingCars()
    }
}

@Preview(showBackground = true)
@Composable
private fun CarsTopBarPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        CarsTopBar(
            onRefresh = {},
            showRefreashAction = true
        )
    }
}

@Composable
private fun ErrorLoadingCars(
    modifier: Modifier = Modifier,
    onTryAgainPressed: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = stringResource(R.string.erro_ao_carregar),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = stringResource(R.string.aguarde_um_momento_e_tente_novamente),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp, start = 9.dp, end = 8.dp)
        )
        ElevatedButton(
            onClick = onTryAgainPressed,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.tentar_novamente))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorLoadingCarsPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        ErrorLoadingCars(
            onTryAgainPressed = {}
        )
    }
}

@Composable
private fun CarsList(
    modifier: Modifier = Modifier,
    cars: List<Car> = listOf(),
    onCarPressed: (Car) -> Unit

) {
    if (cars.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(
            modifier = modifier,
            cars = cars,
            onCarPressed = onCarPressed
        )
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhum carro encontrado",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Adicione algum item pressionando o \"+\"",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun EmptyListPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        EmptyList()
    }
}


@Composable
private fun FilledList(
    modifier: Modifier = Modifier,
    cars: List<Car>,
    onCarPressed: (Car) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        itemsIndexed(cars) { index, car ->
            ListItem(
                modifier = modifier
                    .padding(8.dp)
                    .clickable {
                        onCarPressed(car)
                    },
                headlineContent = {
                    Text(
                        text = "${car.id} - ${car.name}",
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                supportingContent = {
                    Text(
                        text = "${car.maximumPower}, ${car.price}, ${car.year}",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Selecionar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            if (index < cars.lastIndex) {
                HorizontalDivider()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun FilledListPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        FilledList(
            cars = carsFake,
            onCarPressed = {}
        )
    }
}

