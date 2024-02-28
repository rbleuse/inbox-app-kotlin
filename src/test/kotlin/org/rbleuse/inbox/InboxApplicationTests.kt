package org.rbleuse.inbox

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class InboxApplicationTests {
    companion object {
        @Container
        @ServiceConnection
        val cassandraContainer: CassandraContainer<*> = CassandraContainer("cassandra:5.0-beta1")
    }

    @Test
    fun contextLoads() {
    }
}
