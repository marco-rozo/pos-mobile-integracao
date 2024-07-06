package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui

import CarDetailsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.form.CarFormScreen
import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.ui.car.list.CarsListScreen

private object Screens {
    const val LIST_CARS = "listCars"
    const val CAR_DETAILS = "carDetails"
    const val CAR_FORM = "carForm"
}

object Arguments {
    const val ID = "id"
}

private object Routes {
    const val LIST_CARS = Screens.LIST_CARS
    const val CAR_DETAILS = "${Screens.CAR_DETAILS}/{${Arguments.ID}}"
    const val CAR_FORM = "${Screens.CAR_FORM}?${Arguments.ID}={${Arguments.ID}}"

}

@Composable
fun AppCars(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST_CARS,
        modifier = modifier,
    ) {
        composable(
            route = Routes.LIST_CARS,
        ) {
            CarsListScreen(
                onCarPressed = { car ->
                    navController.navigate("${Screens.CAR_DETAILS}/${car.id}")
                },
                onAddPressed = {
                    navController.navigate(Screens.CAR_FORM)
                },
            )
        }
        composable(
            route = Routes.CAR_DETAILS,
            arguments = listOf(
                navArgument(name = Arguments.ID) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            CarDetailsScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onCarDeleted = {
                    navigateToListCars(navController)
                },
                onEditPressed = {
                    val carId = navBackStackEntry.arguments?.getInt(Arguments.ID) ?: 0
                    navController.navigate("${Screens.CAR_FORM}?${Arguments.ID}=$carId")
                }
            )
        }

        composable(
            route = Routes.CAR_FORM,
            arguments = listOf(
                navArgument(name = Arguments.ID) { type = NavType.StringType; nullable = true}
            )
        ) {
            CarFormScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onCarSaved = {
                    navigateToListCars(navController)
                }
            )
        }

        //TODO  criar e adicionar formulário para "Car"
        //formulário deev funcionar para edit e para salvar um novo "Car"
    }
}

private fun navigateToListCars(navController: NavHostController) {
    navController.navigate(Routes.LIST_CARS) {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}