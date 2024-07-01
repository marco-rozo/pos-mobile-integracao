package com.marcodev.trabalhoutfprintegracaoserver.controller

import com.marcodev.trabalhoutfprintegracaoserver.controller.dto.CarDTO
import com.marcodev.trabalhoutfprintegracaoserver.repository.CarRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/carros")
class ClienteController(
    private val carRepository: CarRepository
) {
    @GetMapping
    fun list(@RequestParam name: String?): List<CarDTO> {
        val cars =  name?.let {
            carRepository.findByNameContainsIgnoreCase(it)
        } ?: carRepository.findAll()
        return cars.map { CarDTO.fromEntity(it) }
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