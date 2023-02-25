package org.rbleuse.inbox.service

import org.rbleuse.inbox.domain.Folder
import org.rbleuse.inbox.repository.FolderRepository
import org.springframework.stereotype.Service

@Service
class FolderService
constructor(
    private val folderRepository: FolderRepository
) {

    fun fetchDefaultFolders(userId: String): List<Folder> {
        return listOf(
            Folder(userId, "Inbox", "blue"),
            Folder(userId, "Sent Items", "green"),
            Folder(userId, "Important", "red")
        )
    }

    fun fetchUserFolders(userId: String): List<Folder> {
        return folderRepository.findAllByUserId(userId)
    }

    fun insert(folder: Folder) {
        folderRepository.insert(folder)
    }
}
