package io.freddie.r2dbcstudy.person.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

data class Person(
    @Id val id: Long? = null,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val email: String?,
    @Column("updatedAt")
    val updatedAt: LocalDateTime
)
