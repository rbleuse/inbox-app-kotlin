package org.rbleuse.inbox.repository

import org.rbleuse.inbox.domain.EmailListItem
import org.rbleuse.inbox.domain.pk.EmailListItemKey
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailListItemRepository : CassandraRepository<EmailListItem, EmailListItemKey> {
    fun findAllByKeyUserIdAndKeyLabel(
        userId: String,
        label: String,
    ): List<EmailListItem>
}
