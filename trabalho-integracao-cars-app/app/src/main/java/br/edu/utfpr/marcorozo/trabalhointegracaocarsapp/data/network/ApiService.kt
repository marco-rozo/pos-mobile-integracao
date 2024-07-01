package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.network

import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.network.ApiCarsService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


private val json = Json { ignoreUnknownKeys = true }
private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

private const val API_CARS_BASE_URL = "http://10.0.2.2:8080"
private val apiCarsClient = Retrofit.Builder()
    .addConverterFactory(jsonConverterFactory)
    .baseUrl(API_CARS_BASE_URL)
    .build()

object ApiService {
    val cars: ApiCarsService by lazy {
        apiCarsClient.create(ApiCarsService::class.java)
    }
}