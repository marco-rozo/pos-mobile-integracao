package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.utils.composables.DefaultLoading
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.R
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.theme.TrabalhoIntegracaoCarsAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CarFormScreen(
    modifier: Modifier = Modifier,
                  viewModel: CarFormViewModel = viewModel(),
                  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
                  onBackPressed: () -> Unit,
                  onCarSaved: () -> Unit
) {

    LaunchedEffect(viewModel.uiState.carSaved) {
        if (viewModel.uiState.carSaved) {
            onCarSaved()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorSaving) {
        if (viewModel.uiState.hasErrorSaving) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.erro_ao_salvar_carro)
            )
        }
    }

    if (viewModel.uiState.apiValidationError.isNotBlank()) {
        InformationDialog(
            text = viewModel.uiState.apiValidationError,
            onDismiss = viewModel::dismissInformationDialog
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            FormTopAppBar(
                isNewCar = viewModel.uiState.isNewCar,
                onBackPressed = onBackPressed,
                onSavePressed = viewModel::save,
                showActions = viewModel.uiState.isSuccessLoading,
                isSaving = viewModel.uiState.isSaving
            )
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoading) {
            DefaultLoading(text = stringResource(R.string.carregando))
        } else if (viewModel.uiState.hasErrorLoading) {
            DefaultErrorLoading(
                text = stringResource(R.string.erro_ao_carregar),
                onTryAgainPressed = viewModel::loadCar
            )
        } else {
            FormContent(
                modifier = Modifier.padding(innerPadding),
                carId = viewModel.uiState.carId,
                formState = viewModel.uiState.formState,
                onNameChanged = viewModel::onNameChanged,
                onPriceChanged = viewModel::onPriceChanged,
                onYearChanged = viewModel::onYearChanged,
                onMaximumPowerChanged = viewModel::onMaximumPowerChanged,
            )
        }
    }
}


@Composable
private fun CarId(
    modifier: Modifier = Modifier,
    id: Int
) {
    if (id > 0) {
        FormTitle(text = "${stringResource(R.string.codigo)} - $id")
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormTitle(text = "${stringResource(R.string.codigo)} - ")
            Text(
                text = stringResource(R.string.a_definir),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTopAppBar(
    modifier: Modifier = Modifier,
    isNewCar: Boolean,
    showActions: Boolean,
    isSaving: Boolean,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(if (isNewCar) {
                stringResource(R.string.novo_carro)
            } else {
                stringResource(R.string.editar_carro)
            })
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.voltar)
                )
            }
        },
        actions = {
            if (showActions) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(all = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onSavePressed) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.salvar)
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun FormTopAppBarPeview() {
    TrabalhoIntegracaoCarsAppTheme {
        FormTopAppBar(
            isNewCar = true,
            showActions = true,
            isSaving = false,
            onBackPressed = {},
            onSavePressed = {},
        )
    }
}

@Composable
private fun FormTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    carId: Int,
    formState: FormState,
    onNameChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    onMaximumPowerChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CarId(id = carId)
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        FormTextField(
            label = stringResource(R.string.nome),
            value = formState.name.value,
            onValueChanged = onNameChanged,
            errorMessageCode = formState.name.errorMessageCode,
            keyboardCapitalization = KeyboardCapitalization.Words
        )
        FormTextField(
            label = stringResource(R.string.valor),
            value = formState.price.value,
            onValueChanged = onPriceChanged,
            errorMessageCode = formState.price.errorMessageCode,
            keyboardType = KeyboardType.Number,
        )
        FormTextField(
            label = stringResource(R.string.ano_fabricacao),
            value = formState.year.value,
            onValueChanged = onYearChanged,
            errorMessageCode = formState.year.errorMessageCode,
            keyboardType = KeyboardType.Number,
        )
        FormTextField(
            label = stringResource(R.string.torque),
            value = formState.maximumPower.value,
            onValueChanged = onMaximumPowerChanged,
            errorMessageCode = formState.name.errorMessageCode,
            keyboardCapitalization = KeyboardCapitalization.Words
        )
    }
}

@Composable
private fun FormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    @StringRes
    errorMessageCode: Int? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onValueChange = onValueChanged,
        label = { Text(label) },
        maxLines = 1,
        enabled = true,
        isError = errorMessageCode != null,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = keyboardCapitalization,
            imeAction = keyboardImeAction,
            keyboardType = keyboardType
        ),
    )
    errorMessageCode?.let {
        Text(
            text = stringResource(errorMessageCode),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormContentPreview() {
    TrabalhoIntegracaoCarsAppTheme {
        FormContent(
            carId = 1,
            formState = FormState(),
            onNameChanged = {},
            onPriceChanged ={},
            onYearChanged = {},
            onMaximumPowerChanged = {},
        )
    }
}

@Composable
private fun InformationDialog(
    modifier: Modifier = Modifier,
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(stringResource(R.string.a_seguinte_mensagem_retornou_do_server))
        },
        text = {
            Text(text)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

