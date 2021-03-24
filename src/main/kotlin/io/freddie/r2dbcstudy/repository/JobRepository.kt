package io.freddie.r2dbcstudy.repository

import io.freddie.r2dbcstudy.job.model.Job
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JobRepository : ReactiveCrudRepository<Job, Long> {
}