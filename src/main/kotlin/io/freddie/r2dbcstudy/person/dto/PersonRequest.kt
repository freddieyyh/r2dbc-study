package io.freddie.r2dbcstudy.person.dto

data class PersonRequest(
    private val firstName: String,
    private val lastName: String,
    private val age: Int,
    private val email: String? = null
)
