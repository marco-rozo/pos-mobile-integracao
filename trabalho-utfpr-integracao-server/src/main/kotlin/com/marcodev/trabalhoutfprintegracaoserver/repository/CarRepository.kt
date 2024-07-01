package com.marcodev.trabalhoutfprintegracaoserver.repository

import com.marcodev.trabalhoutfprintegracaoserver.entity.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CarRepository : JpaRepository<Car, Int> {
    fun findByNameContainsIgnoreCase(name: String): List<Car>
}
