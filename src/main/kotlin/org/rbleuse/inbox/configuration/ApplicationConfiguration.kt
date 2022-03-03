package org.rbleuse.inbox.configuration

import com.datastax.oss.driver.api.core.CqlSessionBuilder
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Path

@Configuration
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
}
