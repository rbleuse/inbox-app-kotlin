package org.rbleuse.inbox.service

import org.rbleuse.inbox.repository.UnreadEmailStatsRepository
import org.springframework.stereotype.Service

@Service
class UnreadEmailStatsService
constructor(
    private val unreadEmailStatsRepository: UnreadEmailStatsRepository
) {

    fun mapFolderToUnreadCount(userId: String): Map<String, Int> {
        val stats = unreadEmailStatsRepository.findAllByUserId(userId)
        return stats.associate { it.label to it.unreadCount }
    }
}
