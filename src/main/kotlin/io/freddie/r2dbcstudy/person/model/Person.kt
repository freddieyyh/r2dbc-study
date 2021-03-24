package io.freddie.r2dbcstudy.person.model

import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime

data class Person(
    val id: Long? = null,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val email: String?,
    @Value("#root.updatedAt")
    val updatedAt: LocalDateTime
)
