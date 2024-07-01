import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.Car
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.network.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CarDetailsUiState(
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val car: Car = Car(),
    val isDeleting: Boolean = false,
    val hasErrorDeleting: Boolean = false,
    val carDeleted: Boolean = false,
    val showConfirmationDialog: Boolean = false
) {
    val isSuccessLoading get(): Boolean = !isLoading && !hasErrorLoading
}

class CarDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "CarDetailsViewModel"
    private val carId: Int = savedStateHandle.get<Int>("id")!!

    var uiState: CarDetailsUiState by mutableStateOf(CarDetailsUiState())

    init {
        loadCar()
    }

    fun loadCar() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                uiState.copy(
                    isLoading = false,
                    car = ApiService.cars.findById(carId)
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar dados do carro com id $carId", ex)
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun showConfirmationDialog() {
        uiState = uiState.copy(
            showConfirmationDialog = true
        )
    }

    fun dismissConfirmationDialog() {
        uiState = uiState.copy(
            showConfirmationDialog = false
        )
    }

    fun delete() {
        uiState = uiState.copy(
            isDeleting = true,
            hasErrorDeleting = false,
            showConfirmationDialog = false
        )
        viewModelScope.launch {
            uiState = try {
                delay(2000)
                ApiService.cars.delete(carId)
                uiState.copy(
                    isDeleting = false,
                    carDeleted = true
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao excluir carro com id $carId", ex)
                uiState.copy(
                    isDeleting = false,
                    hasErrorDeleting = true
                )
            }
        }
    }
}












