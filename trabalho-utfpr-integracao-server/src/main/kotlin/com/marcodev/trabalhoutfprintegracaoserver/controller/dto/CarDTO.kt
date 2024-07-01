package com.marcodev.trabalhoutfprintegracaoserver.controller.dto

import com.marcodev.trabalhoutfprintegracaoserver.entity.Car
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class CarDTO(
    val id: Int = 0,
    @field:NotBlank(message = "{name.notblank}")
    @field:Size(max = 100, message = "{name.size}")
    val name: String = "",
    val year: String = "",
    @field:Positive(message = "{price.positive}")
    val price: String = "",
    val maximumPower: String = "",
) {
    fun toEntity(): Car = Car(
        id = this.id,
        name = this.name,
        year = this.year,
        price = BigDecimal.valueOf(this.price.toDouble()) ,
        maximumPower = this.maximumPower,
    )

    companion object {
        fun fromEntity(car: Car): CarDTO = CarDTO(
            id = car.id,
            name = car.name,
            price = car.price.toString(),
            year = car.year,
            maximumPower = car.maximumPower,
        )
    }
}
