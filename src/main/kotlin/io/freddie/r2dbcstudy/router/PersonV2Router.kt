package io.freddie.r2dbcstudy.router

import io.freddie.r2dbcstudy.person.dto.PersonRequest
import io.freddie.r2dbcstudy.person.model.Person
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class PersonV2Router {
    @Bean
    fun routePersonV2(r2dbcEntityTemplate: R2dbcEntityTemplate) = router {
        (accept(MediaType.APPLICATION_JSON) and "/v2").nest {
            GET("/persons") { request ->
                val lastname = request.queryParam("lastname").get()
                r2dbcEntityTemplate.selectOne(query(where("lastname").`is`(lastname)), Person::class.java)
                    .flatMap {
                        ServerResponse.ok()
                            .bodyValue(it)
                    }
            }

            POST("/persons") { request ->
                request.bodyToMono(PersonRequest::class.java)
                    .flatMap(r2dbcEntityTemplate::insert)
                    .flatMap {
                        ServerResponse.ok()
                            .bodyValue(it)
                    }
            }

            PUT("/persons/{id}") { request ->
                val id = request.pathVariable("id").toLong()
                r2dbcEntityTemplate.selectOne(query(where("id").`is`(id)), Person::class.java)
                    .switchIfEmpty(Mono.error(IllegalArgumentException()))
                    .flatMap { r2dbcEntityTemplate.update(query(where("id").`is`(id)), Update.update("lastname", it.lastName)
                        .set("firstname", it.firstName)
                        .set("age", it.age), Person::class.java)
                    }
                    .flatMap { ServerResponse.ok().bodyValue(it) }
            }

            DELETE("/persons/{id}") { request ->
                val id = request.pathVariable("id").toLong()
                r2dbcEntityTemplate.delete(Person::class.java)
                    .matching(query(where("id").`is`(id)))
                    .all()
                    .flatMap {
                        ServerResponse.ok().bodyValue(it)
                    }
            }
        }
    }
}