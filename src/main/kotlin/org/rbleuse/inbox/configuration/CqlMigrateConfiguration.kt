package org.rbleuse.inbox.configuration

import com.datastax.oss.driver.api.core.ConsistencyLevel
import com.datastax.oss.driver.api.core.CqlSession
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.sky.cqlmigrate.CassandraLockConfig
import uk.sky.cqlmigrate.CqlMigrator
import uk.sky.cqlmigrate.CqlMigratorFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

@Configuration
class CqlMigrateConfiguration(
    private val properties: CassandraProperties,
    private val session: CqlSession,
    private val migrationProperties: CassandraMigrationProperties
) {

    @Bean(initMethod = "migrate")
    fun initMigration(): CqlMigrate {
        val lockConfig = CassandraLockConfig.builder()
            .withTimeout(Duration.ofSeconds(3))
            .withPollingInterval(Duration.ofMillis(500))
            .withConsistencyLevel(ConsistencyLevel.ALL)
            .withLockKeyspace(properties.keyspaceName)
            .build()

        val migrator = CqlMigratorFactory.create(lockConfig)
        val directories = migrationProperties.locations.map {
            Paths.get(ClassLoader.getSystemResource(it).toURI())
        }

        return CqlMigrate(migrator, session, properties.keyspaceName, directories)
    }
}

data class CqlMigrate(
    val migrator: CqlMigrator,
    val session: CqlSession,
    val keyspace: String,
    val directories: Collection<Path>
) {
    fun migrate() {
        migrator.migrate(session, keyspace, directories, true)
    }
}
