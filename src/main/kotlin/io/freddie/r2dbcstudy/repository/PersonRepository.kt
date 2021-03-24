package io.freddie.r2dbcstudy.repository

import io.freddie.r2dbcstudy.person.model.Person
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PersonRepository: ReactiveCrudRepository<Person, Long> {
    fun findByLastname(lastname: String): Mono<Person>

    @Modifying
    @Query("UPDATE person SET lastname = :lastname WHERE id = :id")
    fun updateLastname(lastname: String, id: Long): Mono<Int>
}