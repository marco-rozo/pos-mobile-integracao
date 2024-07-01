package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.network

import br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car.Car
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiCarsService {
    @GET("carros")
    suspend fun findAll(): List<Car>

    @GET("carros/{id}")
    suspend fun findById(@Path("id") id: Int): Car

    @DELETE("carros/{id}")
    suspend fun delete(@Path("id") id: Int)

    @POST("carros")
    suspend fun save(@Body car: Car): Response<Car>
}
