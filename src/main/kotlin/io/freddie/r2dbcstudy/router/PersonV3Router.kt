package io.freddie.r2dbcstudy.router

import io.freddie.r2dbcstudy.person.dto.PersonRequest
import io.freddie.r2dbcstudy.person.model.Person
import io.freddie.r2dbcstudy.repository.PersonRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.LocalDateTime

@Configuration
class PersonV3Router {
    @Bean
    fun routePersonV3(personRepository: PersonRepository) = router {
        (accept(MediaType.APPLICATION_JSON) and "/v3").nest {
            GET("/persons") { request ->
                val lastname = request.queryParam("lastname").get()
                personRepository.findByLastname(lastname)
                    .flatMap {
                        ServerResponse.ok()
                            .bodyValue(it)
                    }
            }

            POST("/persons") { request ->
                request.bodyToMono(PersonRequest::class.java)
                    .map {
                        it.run {
                            Person(
                                firstname = firstName,
                                lastname = lastName,
                                age = age,
                                email = email,
                                updatedAt = LocalDateTime.now()
                            )
                        }
                    }
                    .flatMap(personRepository::save)
                    .flatMap {
                        ServerResponse.ok()
                            .bodyValue(it)
                    }
            }

            PUT("/persons/{id}") { request ->
                val id = request.pathVariable("id").toLong()
                request.bodyToMono(PersonRequest::class.java)
                    .zipWith(personRepository.findById(id))
                    .switchIfEmpty(Mono.error(IllegalArgumentException()))
                    .flatMap { (personRequest, person) ->
                        personRepository.save(personRequest.run {  Person(
                            id = person.id,
                            firstname = firstName,
                            lastname = lastName,
                            age = age,
                            email = email,
                            updatedAt = LocalDateTime.now()
                        )})
                    }
                    .flatMap { ServerResponse.ok().bodyValue(it) }
            }

            DELETE("/persons/{id}") { request ->
                val id = request.pathVariable("id").toLong()
                personRepository.deleteById(id)
                    .flatMap {
                        ServerResponse.ok().bodyValue(it)
                    }
            }
        }
    }
}