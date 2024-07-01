package com.marcodev.trabalhoutfprintegracaoserver.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class Car(
    @Id
    @GeneratedValue
    var id: Int = 0,
    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",
    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,
    @Column(name = "manufacture_year")
    var year: String = "",
    var maximumPower: String = "",
)