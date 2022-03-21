package org.rbleuse.inbox

import com.datastax.oss.driver.api.core.ConsistencyLevel
import com.datastax.oss.driver.api.core.CqlSession
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import uk.sky.cqlmigrate.CassandraLockConfig
import uk.sky.cqlmigrate.CqlMigratorFactory
import java.nio.file.Paths
import java.time.Duration

@SpringBootApplication
@ConfigurationPropertiesScan
class InboxApplication(
    private val properties: CassandraProperties,
    private val session: CqlSession
) {
    @EventListener(ApplicationReadyEvent::class)
    fun migrateCql() {
        // Configure locking for coordination of multiple nodes
        val lockConfig = CassandraLockConfig.builder()
            .withTimeout(Duration.ofSeconds(3))
            .withPollingInterval(Duration.ofMillis(500))
            .withConsistencyLevel(ConsistencyLevel.ALL)
            .withLockKeyspace(properties.keyspaceName)
            .build()

        // Create a migrator and run it
        val migrator = CqlMigratorFactory.create(lockConfig)
        val schemas = Paths.get(ClassLoader.getSystemResource("cql").toURI())
        migrator.migrate(session, properties.keyspaceName, listOf(schemas))
    }
}

fun main(args: Array<String>) {
    runApplication<InboxApplication>(*args)
}
