package org.rbleuse.inbox.controller

import org.rbleuse.inbox.repository.EmailRepository
import org.rbleuse.inbox.service.EmailService
import org.rbleuse.inbox.service.FolderService
import org.rbleuse.inbox.service.UnreadEmailStatsService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.util.UUID

@Controller
class EmailController(
    private val folderService: FolderService,
    private val emailRepository: EmailRepository,
    private val emailService: EmailService,
    private val unreadEmailStatsService: UnreadEmailStatsService,
) {
    @GetMapping("/email/{id}")
    fun homePage(
        @PathVariable id: UUID,
        @RequestParam folder: String,
        @AuthenticationPrincipal principal: OAuth2User?,
        model: Model,
    ): String {
        if (null == principal || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index"
        }

        val userId: String = principal.getAttribute("login")!!
        model.addAttribute("userFolders", folderService.fetchUserFolders(userId))
        model.addAttribute("defaultFolders", folderService.fetchDefaultFolders(userId))

        val email = emailRepository.findByIdOrNull(id) ?: return "inbox-page"
        model.addAttribute("email", email)
        model.addAttribute("toIds", email.to.joinToString())
        model.addAttribute("folderToUnreadCounts", unreadEmailStatsService.mapFolderToUnreadCount(userId))

        return "email-page"
    }

    @GetMapping("/compose")
    fun getComposePage(
        @RequestParam(required = false) to: String?,
        @AuthenticationPrincipal principal: OAuth2User?,
        model: Model,
    ): String {
        if (null == principal || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index"
        }

        val userId: String = principal.getAttribute("login")!!
        model.addAttribute("userFolders", folderService.fetchUserFolders(userId))
        model.addAttribute("defaultFolders", folderService.fetchDefaultFolders(userId))
        model.addAttribute("folderToUnreadCounts", unreadEmailStatsService.mapFolderToUnreadCount(userId))

        to?.let {
            val uniqueIds = splitIds(it)
            model.addAttribute("toIds", uniqueIds)
        }

        return "compose-page"
    }

    private fun splitIds(string: String): List<String> {
        return string.split(",").stream().map { it.trim() }.filter { it.isNotEmpty() }.distinct().toList()
    }

    @PostMapping("/sendEmail")
    fun sendEmail(
        @RequestBody formData: MultiValueMap<String, String>,
        @AuthenticationPrincipal principal: OAuth2User?,
    ): ModelAndView {
        if (null != principal && StringUtils.hasText(principal.getAttribute("login"))) {
            val userId: String = principal.getAttribute("login")!!
            val toIds = splitIds(formData.getFirst("toUserIds")!!)
            val subject = formData.getFirst("subject")!!
            val body = formData.getFirst("body")!!

            emailService.sendEmail(userId, toIds, subject, body)
        }

        return ModelAndView("redirect:/")
    }
}
