package org.rbleuse.inbox.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File

@ConfigurationProperties("datastax.astra")
data class DataStaxAstraProperties(
    val secureConnectBundle: File
)
