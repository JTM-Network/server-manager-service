package com.jtm.server.data.event

import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.data.security.AuthenticationManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
class EventDispatcherTest {

    private val eventAggregator: EventAggregator = mock()
    private val authenticationManager: AuthenticationManager = mock()
    private val eventDispatcher = EventDispatcher(eventAggregator, authenticationManager)

    private val handler: EventHandler<*> = mock()
    private val session: WebSocketSession = mock()
    private val event: IncomingEvent = mock()

    @Before
    fun setup() {
        `when`(event.name).thenReturn("name")
        `when`(event.token).thenReturn("token")
    }

    @Test
    fun dispatch_thenFailedAuthentication() {
        `when`(authenticationManager.authenticate(anyString())).thenReturn(false)
        `when`(session.close(anyOrNull())).thenReturn(Mono.empty())

        val returned = eventDispatcher.dispatch(session, event)

        verify(authenticationManager, times(1)).authenticate(anyString())
        verifyNoMoreInteractions(authenticationManager)

        verify(session, times(1)).close(anyOrNull())
        verifyNoMoreInteractions(session)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun dispatch_thenFailedEvent() {
        `when`(authenticationManager.authenticate(anyString())).thenReturn(true)
        `when`(eventAggregator.getHandler(anyString())).thenReturn(null)
        `when`(session.close(anyOrNull())).thenReturn(Mono.empty())

        val returned = eventDispatcher.dispatch(session, event)

        verify(authenticationManager, times(1)).authenticate(anyString())
        verifyNoMoreInteractions(authenticationManager)

        verify(eventAggregator, times(1)).getHandler(anyString())
        verifyNoMoreInteractions(eventAggregator)

        verify(session, times(1)).close(anyOrNull())
        verifyNoMoreInteractions(session)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun dispatchTest() {
        `when`(eventAggregator.getHandler(anyString())).thenReturn(handler)
        `when`(authenticationManager.authenticate(anyString())).thenReturn(true)
        `when`(handler.handleEvent(anyOrNull(), anyOrNull())).thenReturn(Mono.empty())

        val returned = eventDispatcher.dispatch(session, event)

        verify(authenticationManager, times(1)).authenticate(anyString())
        verifyNoMoreInteractions(authenticationManager)

        verify(eventAggregator, times(1)).getHandler(anyString())
        verifyNoMoreInteractions(eventAggregator)

        verify(handler, times(1)).handleEvent(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(handler)

        StepVerifier.create(returned)
            .verifyComplete()
    }
}