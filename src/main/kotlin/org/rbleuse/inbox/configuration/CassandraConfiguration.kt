package org.rbleuse.inbox.configuration

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.CqlSessionBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.core.cql.generator.CreateKeyspaceCqlGenerator
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification

@Configuration
class CassandraConfiguration {
    @Bean
    fun cqlSession(
        builder: CqlSessionBuilder,
        @Value("\${spring.cassandra.keyspace-name}") keyspaceName: String,
    ): CqlSession {
        // This creates the keyspace on startup
        builder.withKeyspace("system").build().use { session ->
            session.execute(
                CreateKeyspaceCqlGenerator
                    .toCql(CreateKeyspaceSpecification.createKeyspace(keyspaceName).ifNotExists()),
            )
        }

        return builder.withKeyspace(keyspaceName).build()
    }
}
