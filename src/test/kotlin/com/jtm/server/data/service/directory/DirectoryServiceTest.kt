package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.exceptions.DirectoryNotFound
import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.model.directory.DirectoryInfo
import com.jtm.server.core.usecase.repository.DirectoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class DirectoryServiceTest {

    private val directoryRepository: DirectoryRepository = mock()
    private val directoryService = DirectoryService(directoryRepository)
    private val dir = Directory(UUID.randomUUID(), "test", info = DirectoryInfo(10, 10))

    @Test
    fun addDirectory() {
        `when`(directoryRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())
        `when`(directoryRepository.save(anyOrNull())).thenReturn(Mono.just(dir))

        val returned = directoryService.addDirectory(dir)

        verify(directoryRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(dir.serverId)
                    assertThat(it.name).isEqualTo("test")
                }
                .verifyComplete()
    }

    @Test
    fun getDirectory_thenNotFound() {
        `when`(directoryRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = directoryService.getDirectory(UUID.randomUUID())

        verify(directoryRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .expectError(DirectoryNotFound::class.java)
                .verify()
    }

    @Test
    fun getDirectory() {
        `when`(directoryRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(dir))

        val returned = directoryService.getDirectory(UUID.randomUUID())

        verify(directoryRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(dir.serverId)
                    assertThat(it.name).isEqualTo("test")
                }
                .verifyComplete()
    }

    @Test
    fun getDirectories() {
        `when`(directoryRepository.findAll()).thenReturn(Flux.just(dir))

        val returned = directoryService.getDirectories()

        verify(directoryRepository, times(1)).findAll()
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(dir.serverId)
                    assertThat(it.name).isEqualTo("test")
                }
                .verifyComplete()
    }

    @Test
    fun removeDirectory_thenNotFound() {
        `when`(directoryRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = directoryService.removeDirectory(UUID.randomUUID())

        verify(directoryRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .expectError(DirectoryNotFound::class.java)
                .verify()
    }

    @Test
    fun removeDirectory() {
        `when`(directoryRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(dir))
        `when`(directoryRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = directoryService.removeDirectory(UUID.randomUUID())

        verify(directoryRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(directoryRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(dir.serverId)
                    assertThat(it.name).isEqualTo("test")
                }
                .verifyComplete()
    }
}