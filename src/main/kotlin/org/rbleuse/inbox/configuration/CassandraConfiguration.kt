package org.rbleuse.inbox.configuration

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.CqlSessionBuilder
import org.springframework.boot.cassandra.autoconfigure.CassandraProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.core.cql.generator.CreateKeyspaceCqlGenerator
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification

@Configuration
class CassandraConfiguration {
    @Bean
    fun cqlSession(
        builder: CqlSessionBuilder,
        properties: CassandraProperties,
    ): CqlSession {
        // This creates the keyspace on startup
        builder.withKeyspace("system").build().use { session ->
            session.execute(
                CreateKeyspaceCqlGenerator
                    .toCql(CreateKeyspaceSpecification.createKeyspace(properties.keyspaceName!!).ifNotExists()),
            )
        }

        return builder.withKeyspace(properties.keyspaceName).build()
    }
}
