package org.rbleuse.inbox.service

import com.datastax.oss.driver.api.core.uuid.Uuids
import org.rbleuse.inbox.domain.Email
import org.rbleuse.inbox.domain.EmailListItem
import org.rbleuse.inbox.domain.pk.EmailListItemKey
import org.rbleuse.inbox.repository.EmailListItemRepository
import org.rbleuse.inbox.repository.EmailRepository
import org.rbleuse.inbox.repository.UnreadEmailStatsRepository
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailRepository: EmailRepository,
    private val emailListItemRepository: EmailListItemRepository,
    private val unreadEmailStatsRepository: UnreadEmailStatsRepository
) {

    fun sendEmail(from: String, to: List<String>, subject: String, body: String) {
        val email = Email(Uuids.timeBased(), from, to, subject, body)

        emailRepository.save(email)

        to.forEach { toId ->
            val emailListItem = EmailListItem(
                EmailListItemKey(toId, "Inbox", email.id),
                from,
                to,
                subject,
                false
            )
            emailListItemRepository.save(emailListItem)
            unreadEmailStatsRepository.incrementCounter(toId, "Inbox")
        }

        val emailListItem = EmailListItem(
            EmailListItemKey(from, "Sent Items", email.id),
            from,
            to,
            subject,
            true
        )
        emailListItemRepository.save(emailListItem)
    }
}
