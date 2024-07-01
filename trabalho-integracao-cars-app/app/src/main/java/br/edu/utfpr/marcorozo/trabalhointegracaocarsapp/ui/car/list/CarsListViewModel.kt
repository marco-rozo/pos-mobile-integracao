package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.Car
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.network.ApiService
import kotlinx.coroutines.launch
import java.lang.Exception
import java.math.BigDecimal


data class CarsListUiState(
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val cars: List<Car> = listOf()
) {
    val success get(): Boolean = !loading && !hasError
}

class CarsListViewModel : ViewModel() {
    private val tag: String = "CarsListViewModel"
    var uiState: CarsListUiState by mutableStateOf(CarsListUiState())


    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(
            loading = true,
            hasError = false
        )

        viewModelScope.launch {
            uiState = try {
                val carsResult = ApiService.cars.findAll()
                uiState.copy(
                    cars = carsResult,
                    loading = false
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar lista de carros", ex)
                uiState.copy(
                    hasError = true,
                    loading = false
                )
            }
        }
    }
}

val carsFake: List<Car> = listOf(
    Car(
        id = 1,
        maximumPower = "400 cv",
        name = "Ferrari Spider",
        price = "2000000",
        year = "2008"
    ),
    Car(
        id = 2,
        maximumPower = "162 cv",
        name = "Corsa turbo",
        price = "20000",
        year = "2005"
    )
)
