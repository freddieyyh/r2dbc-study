package io.freddie.r2dbcstudy.repository

import io.freddie.r2dbcstudy.person.model.Person
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PersonRepository: ReactiveCrudRepository<Person, Long> {
    fun findByLastname(lastname: String): Mono<Person>
}