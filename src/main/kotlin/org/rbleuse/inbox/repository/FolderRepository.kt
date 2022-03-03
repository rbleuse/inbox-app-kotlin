package org.rbleuse.inbox.repository

import org.rbleuse.inbox.domain.Folder
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository : CassandraRepository<Folder, String> {

    fun findAllByUserId(userId: String): List<Folder>
}
