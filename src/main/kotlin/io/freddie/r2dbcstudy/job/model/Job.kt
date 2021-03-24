package io.freddie.r2dbcstudy.job.model

import org.springframework.data.annotation.Id
import java.util.UUID

data class Job(
    @Id val id: String? = null,
    val name: String
)