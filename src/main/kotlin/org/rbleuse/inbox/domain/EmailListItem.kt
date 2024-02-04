package org.rbleuse.inbox.domain

import org.rbleuse.inbox.domain.pk.EmailListItemKey
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.CassandraType.Name
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("messages_by_user_folder")
data class EmailListItem(
    @PrimaryKey
    val key: EmailListItemKey,
    @CassandraType(type = Name.TEXT)
    val from: String,
    @CassandraType(type = Name.LIST, typeArguments = [Name.TEXT])
    val to: List<String>,
    @CassandraType(type = Name.TEXT)
    val subject: String,
    @CassandraType(type = Name.BOOLEAN)
    val isRead: Boolean,
) {
    @field:Transient
    lateinit var agoTimeString: String
}
