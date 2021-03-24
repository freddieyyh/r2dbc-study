package io.freddie.r2dbcstudy.router

import io.freddie.r2dbcstudy.person.dto.PersonRequest
import io.freddie.r2dbcstudy.person.model.Person
import io.freddie.r2dbcstudy.repository.PersonRepository
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v4/persons")
class V4Controller(val personRepository: PersonRepository) {

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long) = personRepository.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun create(@RequestBody personRequest: PersonRequest) = personRepository.save(
        Person(
            lastname = personRequest.lastName,
            firstname = personRequest.firstName,
            age = personRequest.age,
            email = personRequest.email,
            updatedAt = LocalDateTime.now()
        )
    )

    @PutMapping("{id}")
    @Transactional
    fun update(@PathVariable("id") id: Long, @RequestBody personRequest: PersonRequest) = personRepository.findById(id)
        .flatMap { person ->
            val newPerson = person.copy(
                firstname = personRequest.firstName,
                lastname = personRequest.lastName,
                age = personRequest.age,
                email = personRequest.email,
                updatedAt = LocalDateTime.now()
            )
            personRepository.save(newPerson)
        }

    @PutMapping("{id}/change-lastname")
    @Transactional
    fun updateLastname(@PathVariable("id") id: Long, @RequestParam lastname: String) = personRepository.findById(id)
        .flatMap {
            personRepository.updateLastname(lastname = lastname, id = id)
        }
}