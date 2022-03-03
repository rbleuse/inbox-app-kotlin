package org.rbleuse.inbox.repository

import org.rbleuse.inbox.domain.Email
import org.springframework.data.cassandra.repository.CassandraRepository
import java.util.UUID

interface EmailRepository : CassandraRepository<Email, UUID>
