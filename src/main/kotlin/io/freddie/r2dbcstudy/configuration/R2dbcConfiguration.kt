package io.freddie.r2dbcstudy.configuration

import io.freddie.r2dbcstudy.job.model.Job
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryMetadata
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.sql.SqlIdentifier
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import reactor.core.publisher.Mono
import java.util.UUID
import org.springframework.data.r2dbc.mapping.SettableValue

import org.springframework.data.r2dbc.mapping.OutboundRow

import io.freddie.r2dbcstudy.person.model.Person
import org.springframework.core.convert.converter.Converter

import org.springframework.data.convert.WritingConverter





@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
class R2dbcConfiguration : AbstractR2dbcConfiguration() {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()

        initializer.setConnectionFactory(connectionFactory)

        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("data.sql")))
        initializer.setDatabasePopulator(populator)

        return initializer
    }

    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get("r2dbc:mariadb://root:1234@localhost:33306/r2dbc_study?useUnicode=true&characterEncoding=utf8")
    }

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun idGeneratingCallback(databaseClient: DatabaseClient): BeforeConvertCallback<Job> {
        return BeforeConvertCallback<Job> { job: Job, sqlIdentifier: SqlIdentifier ->
            Mono.just(job.copy(id = UUID.randomUUID().toString()))
        }
    }
}