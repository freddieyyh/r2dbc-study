package io.freddie.r2dbcstudy.person.model

import java.time.LocalDateTime

data class Person(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String?,
    val updatedAt: LocalDateTime
)
