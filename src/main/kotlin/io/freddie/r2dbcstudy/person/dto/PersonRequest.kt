package io.freddie.r2dbcstudy.person.dto

data class PersonRequest(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String? = null
)
