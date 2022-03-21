package org.rbleuse.inbox.repository

import org.rbleuse.inbox.domain.UnreadEmailStats
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.cassandra.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UnreadEmailStatsRepository : CassandraRepository<UnreadEmailStats, String> {
    fun findAllByUserId(userId: String): List<UnreadEmailStats>

    @Query("update unread_email_stats set unread_count = unread_count + 1 where user_id = ?0 and label = ?1")
    fun incrementCounter(userId: String, label: String)

    @Query("update unread_email_stats set unread_count = unread_count - 1 where user_id = ?0 and label = ?1")
    fun decrementCounter(userId: String, label: String)
}
