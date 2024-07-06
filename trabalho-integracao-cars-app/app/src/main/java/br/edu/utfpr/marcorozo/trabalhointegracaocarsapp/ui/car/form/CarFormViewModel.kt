package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.form

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.R
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.Car
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.network.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

data class FormField(
    val value: String = "",
    @StringRes
    val errorMessageCode: Int? = null
)

data class FormState(
    val name: FormField = FormField(),
    val maximumPower: FormField = FormField(),
    val year: FormField = FormField(),
    val price: FormField = FormField(),
) {
    val isValid get(): Boolean = name.errorMessageCode == null &&
            maximumPower.errorMessageCode == null &&
            year.errorMessageCode == null &&
            price.errorMessageCode == null
}

data class CarFormUiState(
    val carId: Int = 0,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val formState: FormState = FormState(),
    val isSaving: Boolean = false,
    val hasErrorSaving: Boolean = false,
    val carSaved: Boolean = false,
    val apiValidationError: String = ""
) {
    val isNewCar get(): Boolean = carId <= 0
    val isSuccessLoading get(): Boolean = !isLoading && !hasErrorLoading
}

class CarFormViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "CarFormViewModel"
    private val carId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

    var uiState: CarFormUiState by mutableStateOf(CarFormUiState())

    init {
        if (carId > 0) {
            loadCar()
        }
    }

    fun loadCar() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
            carId = carId
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                val car = ApiService.cars.findById(carId)
                uiState.copy(
                    isLoading = false,
                    formState = FormState(
                        name = FormField(car.name),
                        year = FormField(car.year),
                        maximumPower = FormField(car.maximumPower),
                        price = FormField(car.price),
                    )
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar os dados do carro com código $carId", ex)
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onNameChanged(value: String) {
        if (uiState.formState.name.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    name = uiState.formState.name.copy(
                        value = value,
                        errorMessageCode = validateName(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateName(nome: String): Int? = if (nome.isBlank()) {
        R.string.nome_obrigatorio
    } else {
        null
    }

    fun onMaximumPowerChanged(value: String) {
        if (uiState.formState.maximumPower.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    maximumPower = uiState.formState.maximumPower.copy(
                        value = value,
                        errorMessageCode = validateMaximumPower(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateMaximumPower(maximumPower: String): Int? = if (maximumPower.isBlank()) {
        R.string.potencia_obrigatorio
    } else {
        null
    }

    fun onYearChanged(value: String){
        if (uiState.formState.year.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    year = uiState.formState.year.copy(
                        value = value,
                        errorMessageCode = validateYear(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateYear(year: String): Int? = if (year.isBlank()) {
        R.string.ano_obrigatorio
    } else if (year.toInt() < 1900) {
        R.string.ano_invalido
    } else {
        null
    }

    fun onPriceChanged(value: String) {
        if (uiState.formState.price.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    price = uiState.formState.price.copy(
                        value = value,
                        errorMessageCode = validatePrice(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validatePrice(price: String): Int? = if (price.isBlank()) {
        R.string.valor_obrigatorio
    } else if (price.toDouble() < 1) {
        R.string.valor_invalido
    } else {
        null
    }

    fun save() {
        if (!isValidForm()) {
            return
        }
        uiState = uiState.copy(
            isSaving = true,
            hasErrorSaving = false
        )
        viewModelScope.launch {
            delay(2000)
            val car = Car(
                id = carId,
                name = uiState.formState.name.value,
                maximumPower = uiState.formState.maximumPower.value,
                price  = uiState.formState.price.value,
                year = uiState.formState.year.value,
            )
            uiState = try {
                val response = ApiService.cars.save(car)
                if (response.isSuccessful) {
                    uiState.copy(
                        isSaving = false,
                        carSaved = true
                    )
                } else if (response.code() == 400) {
                    val error = Json.parseToJsonElement(response.errorBody()!!.string())
                    val jsonObject = error.jsonObject
                    val apiValidationError =
                        jsonObject.keys.joinToString("\n") {
                            jsonObject[it].toString().replace("\"", "")
                        }
                    uiState.copy(
                        isSaving = false,
                        apiValidationError = apiValidationError
                    )
                } else {
                    uiState.copy(
                        isSaving = false,
                        hasErrorSaving = true
                    )
                }
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao salvar carro com código $carId", ex)
                uiState.copy(
                    isSaving = false,
                    hasErrorSaving = true
                )
            }
        }
    }

    private fun isValidForm(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                name = uiState.formState.name.copy(
                    errorMessageCode = validateName(uiState.formState.name.value)
                ),
                price = uiState.formState.price.copy(
                    errorMessageCode = validatePrice(uiState.formState.price.value)
                ),
                year = uiState.formState.year.copy(
                    errorMessageCode = validateYear(uiState.formState.year.value)
                ),
                maximumPower = uiState.formState.maximumPower.copy(
                    errorMessageCode = validateMaximumPower(uiState.formState.maximumPower.value)
                ),
            )
        )
        return uiState.formState.isValid
    }

    fun dismissInformationDialog() {
        uiState = uiState.copy(
            apiValidationError = ""
        )
    }
}