package org.rbleuse.inbox.controller

import com.datastax.oss.driver.api.core.uuid.Uuids
import org.ocpsoft.prettytime.PrettyTime
import org.rbleuse.inbox.domain.Folder
import org.rbleuse.inbox.repository.EmailListItemRepository
import org.rbleuse.inbox.service.FolderService
import org.rbleuse.inbox.service.UnreadEmailStatsService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@Controller
class InboxController(
    private val folderService: FolderService,
    private val emailListItemRepository: EmailListItemRepository,
    private val unreadEmailStatsService: UnreadEmailStatsService,
) {
    @GetMapping("/")
    fun homePage(
        @RequestParam folder: String = "Inbox",
        @AuthenticationPrincipal principal: OAuth2User?,
        model: Model,
    ): String {
        if (null == principal || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index"
        }

        val userId: String = principal.getAttribute("login")!!

        val userFolders = folderService.fetchUserFolders(userId)

        if (userFolders.isEmpty()) {
            folderService.insert(Folder(userId, "Inbox", "blue"))
            folderService.insert(Folder(userId, "Sent", "green"))
            folderService.insert(Folder(userId, "Important", "red"))
        }

        model.addAttribute("userFolders", userFolders)
        model.addAttribute("defaultFolders", folderService.fetchDefaultFolders(userId))
        model.addAttribute("folderToUnreadCounts", unreadEmailStatsService.mapFolderToUnreadCount(userId))

        val prettyTime = PrettyTime(Locale.FRENCH)

        val emailList = emailListItemRepository.findAllByKeyUserIdAndKeyLabel(userId, folder)
        emailList.forEach {
            val emailDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Uuids.unixTimestamp(it.key.timeUuid)), ZoneId.systemDefault())
            it.agoTimeString = prettyTime.format(emailDateTime)
        }

        model.addAttribute("folderEmails", emailList)
        model.addAttribute("currentFolder", folder)

        return "inbox-page"
    }
}
