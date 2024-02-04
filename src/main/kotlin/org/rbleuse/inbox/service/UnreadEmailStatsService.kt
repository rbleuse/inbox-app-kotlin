package org.rbleuse.inbox.service

import org.rbleuse.inbox.repository.UnreadEmailStatsRepository
import org.springframework.stereotype.Service

@Service
class UnreadEmailStatsService(
    private val unreadEmailStatsRepository: UnreadEmailStatsRepository,
) {
    fun mapFolderToUnreadCount(userId: String): Map<String, Int> {
        return unreadEmailStatsRepository.findAllByUserId(userId).associate { it.label to it.unreadCount }
    }

    fun decrementCounter(
        userId: String,
        label: String,
    ) {
        unreadEmailStatsRepository.decrementCounter(userId, label)
    }
}
