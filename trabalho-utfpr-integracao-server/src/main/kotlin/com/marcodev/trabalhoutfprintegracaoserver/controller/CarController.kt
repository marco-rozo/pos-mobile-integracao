package com.marcodev.trabalhoutfprintegracaoserver.controller

import com.marcodev.trabalhoutfprintegracaoserver.controller.dto.CarDTO
import com.marcodev.trabalhoutfprintegracaoserver.repository.CarRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/carros")
class ClienteController(
    private val carRepository: CarRepository
) {
    @GetMapping
    fun list(): List<CarDTO> {
        return carRepository.findAll().map {
            CarDTO.fromEntity(it)
        }
    }

    @PostMapping
    fun save(@Valid @RequestBody car: CarDTO): CarDTO {
        val carUpdated = carRepository.save(car.toEntity())
        return CarDTO.fromEntity(carUpdated)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<CarDTO> {
        val car = carRepository.findById(id).getOrNull()
            ?: return ResponseEntity
                .notFound()
                .build()
        return ResponseEntity.ok(CarDTO.fromEntity(car))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        carRepository.deleteById(id)
        return ResponseEntity
            .noContent()
            .build()
    }

}