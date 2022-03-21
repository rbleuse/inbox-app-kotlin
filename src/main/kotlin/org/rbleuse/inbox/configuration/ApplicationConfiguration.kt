package org.rbleuse.inbox.configuration

import com.datastax.oss.driver.api.core.CqlSessionBuilder
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.cassandra.config.EnableCassandraAuditing
import org.springframework.data.domain.AuditorAware
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.Optional

@Configuration
@EnableCassandraAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "auditingDateTimeProvider")
class ApplicationConfiguration {

    /**
     * This is necessary to have the Spring Boot app use the Astra secure bundle
     * to connect to the database
     */
    @Bean
    fun sessionBuilderCustomizer(astraProperties: DataStaxAstraProperties): CqlSessionBuilderCustomizer {
        val bundle: Path = astraProperties.secureConnectBundle.toPath()
        return CqlSessionBuilderCustomizer { builder: CqlSessionBuilder -> builder.withCloudSecureConnectBundle(bundle) }
    }

    @Bean
    fun auditingDateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(OffsetDateTime.now()) }
    }

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("TEST") }
    }
}
