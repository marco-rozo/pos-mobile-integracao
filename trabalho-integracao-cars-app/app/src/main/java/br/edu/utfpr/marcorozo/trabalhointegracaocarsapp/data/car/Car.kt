package br.edu.utfpr.marcorozo.trabalhointegracaocarsapp.data.car

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val id: Int = 0,
    val name: String = "",
    val year: String = "",
    val price: String = "",
    val maximumPower: String = "",
)
