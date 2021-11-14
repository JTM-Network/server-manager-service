package com.jtm.server.data.service

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.core.domain.exceptions.RuntimeNotFound
import com.jtm.server.core.usecase.repository.RuntimeStatsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
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
class RuntimeServiceTest {

    private val runtimeRepository: RuntimeStatsRepository = mock();
    private val runtimeService = RuntimeService(runtimeRepository)
    private val runtime = RuntimeStats()

    @Test
    fun getRuntime_thenNotFound() {
        `when`(runtimeRepository.findById(anyLong())).thenReturn(Mono.empty())

        val returned = runtimeService.getRuntime(System.currentTimeMillis())

        verify(runtimeRepository, times(1)).findById(anyLong())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .expectError(RuntimeNotFound::class.java)
                .verify()
    }

    @Test
    fun getRuntime() {
        `when`(runtimeRepository.findById(anyLong())).thenReturn(Mono.just(runtime))

        val returned = runtimeService.getRuntime(System.currentTimeMillis())

        verify(runtimeRepository, times(1)).findById(anyLong())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.time).isEqualTo(TimeType.FIVE)
                    assertThat(it.cpuUsage).isEqualTo(0.10)
                    assertThat(it.serverId).isEqualTo(runtime.serverId)
                }
                .verifyComplete()
    }

    @Test
    fun getRuntimesByServerId() {
        `when`(runtimeRepository.findByServerId(anyOrNull())).thenReturn(Flux.just(runtime))

        val returned = runtimeService.getRuntimesByServerId(UUID.randomUUID())

        verify(runtimeRepository, times(1)).findByServerId(anyOrNull())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.time).isEqualTo(TimeType.FIVE)
                    assertThat(it.cpuUsage).isEqualTo(0.10)
                    assertThat(it.serverId).isEqualTo(runtime.serverId)
                }
                .verifyComplete()
    }

    @Test
    fun getRuntimesByServerIdAndTime() {
        `when`(runtimeRepository.findByServerIdAndTime(anyOrNull(), anyOrNull())).thenReturn(Flux.just(runtime))

        val returned = runtimeService.getRuntimesByServerIdAndTime(UUID.randomUUID(), TimeType.FIVE)

        verify(runtimeRepository, times(1)).findByServerIdAndTime(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.time).isEqualTo(TimeType.FIVE)
                    assertThat(it.cpuUsage).isEqualTo(0.10)
                    assertThat(it.serverId).isEqualTo(runtime.serverId)
                }
                .verifyComplete()
    }

    @Test
    fun deleteRuntime_thenNotFound() {
        `when`(runtimeRepository.findById(anyLong())).thenReturn(Mono.empty())

        val returned = runtimeService.deleteRuntime(System.currentTimeMillis())

        verify(runtimeRepository, times(1)).findById(anyLong())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .expectError(RuntimeNotFound::class.java)
                .verify()
    }

    @Test
    fun deleteRuntime() {
        `when`(runtimeRepository.findById(anyLong())).thenReturn(Mono.just(runtime))
        `when`(runtimeRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = runtimeService.deleteRuntime(System.currentTimeMillis())

        verify(runtimeRepository, times(1)).findById(anyLong())
        verifyNoMoreInteractions(runtimeRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.time).isEqualTo(TimeType.FIVE)
                    assertThat(it.cpuUsage).isEqualTo(0.10)
                    assertThat(it.serverId).isEqualTo(runtime.serverId)
                }
                .verifyComplete()
    }
}