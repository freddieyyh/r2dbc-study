package io.freddie.r2dbcstudy.router

import io.freddie.r2dbcstudy.person.model.Person
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.time.LocalDateTime

@Configuration
class PersonV1Router {

    @Bean
    fun routePersonV1(databaseClient: DatabaseClient) = router {
        (accept(MediaType.APPLICATION_JSON) and "/v1").nest {
            GET("/persons") { request ->
                databaseClient.sql("SELECT * FROM person WHERE lastname = :lastname")
                    .bind("lastname", request.queryParam("lastname").get())
                    .map { row ->
                        Person(
                            id = row.get("id", Long::class.java),
                            firstname = row.get("firstname", String::class.java)!!,
                            lastname = row.get("lastname", String::class.java)!!,
                            age = row.get("age", Int::class.java)!!,
                            email = row.get("email", String::class.java),
                            updatedAt = row.get("updatedAt", LocalDateTime::class.java)!!
                        )
                    }
                    .all()
                    .collectList()
                    .flatMap {
                        ServerResponse.ok()
                            .bodyValue(it)
                    }
            }
        }
    }
}