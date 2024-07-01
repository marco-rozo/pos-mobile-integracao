package com.marcodev.trabalhoutfprintegracaoserver.repository

import com.marcodev.trabalhoutfprintegracaoserver.entity.Car
import org.springframework.data.jpa.repository.JpaRepository

interface CarRepository : JpaRepository<Car, Int>
