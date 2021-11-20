package com.jtm.server.core.usecase

import com.jtm.server.core.domain.exceptions.FileNotFound
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Mono
import java.io.File
import java.util.*

class FileHandler {

    private val logger = LoggerFactory.getLogger(FileHandler::class.java)

    @Value("\${path.downloads:/downloads}")
    lateinit var downloadPath: String

    fun save(path: String, name: String, filePart: FilePart): Mono<Void> {
        val folder = File("$downloadPath/$path")
        if (!folder.exists() && folder.mkdirs()) logger.info("Created directories at: $path")
        val file = File("$downloadPath/$path", name)
        return filePart.transferTo(file)
    }

    fun fetch(path: String): Mono<File> {
        val file = File("$downloadPath/$path")
        if (!file.exists()) return Mono.error { FileNotFound() }
        return Mono.just(file)
    }

    fun clearDownloads(id: UUID): Mono<Void> {
        val folder = File("$downloadPath/$id")
        if (!folder.exists()) return Mono.error { FileNotFound() }
        FileUtils.deleteDirectory(folder)
        return Mono.empty()
    }
}