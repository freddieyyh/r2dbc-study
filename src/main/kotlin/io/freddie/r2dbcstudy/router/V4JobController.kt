package io.freddie.r2dbcstudy.router

import io.freddie.r2dbcstudy.job.dto.JobRequest
import io.freddie.r2dbcstudy.job.model.Job
import io.freddie.r2dbcstudy.person.dto.PersonRequest
import io.freddie.r2dbcstudy.person.model.Person
import io.freddie.r2dbcstudy.repository.JobRepository
import io.freddie.r2dbcstudy.repository.PersonRepository
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v4/jobs")
class V4JobController(val jobRepository: JobRepository) {

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long) = jobRepository.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun create(@RequestBody jobRequest: JobRequest) = jobRepository.save(
        Job(name = jobRequest.name)
    )

    @PutMapping("{id}")
    @Transactional
    fun update(@PathVariable("id") id: Long, @RequestBody jobRequest: JobRequest) = jobRepository.findById(id)
        .flatMap { job ->
            jobRepository.save(job.copy(
                name = jobRequest.name
            ))
        }
}