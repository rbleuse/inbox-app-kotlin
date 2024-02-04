package org.rbleuse.inbox.domain

import org.springframework.data.annotation.Id
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.CassandraType.Name
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID

@Table("messages")
data class Email(
    @Id
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    val id: UUID,
    @CassandraType(type = Name.TEXT)
    val from: String,
    @CassandraType(type = Name.LIST, typeArguments = [Name.TEXT])
    val to: List<String>,
    @CassandraType(type = Name.TEXT)
    val subject: String,
    @CassandraType(type = Name.TEXT)
    val body: String,
)
