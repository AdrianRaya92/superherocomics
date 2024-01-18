package com.ayardreams.superherocomics.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.testshared.sampleComics
import com.ayardreams.usecase.FindComicUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FindComicUseCaseTest {

    @Test
    fun `Invoke calls comics repository`(): Unit = runBlocking {
        val comic = flowOf(sampleComics.copy(id = 1))
        val findComicUseCase = FindComicUseCase(
            mock {
                on { findById(1) } doReturn (comic)
            }
        )

        val result = findComicUseCase(1)

        assertEquals(comic, result)
    }

    @Test
    fun `Invoke with non-existent comic id returns empty flow`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        whenever(comicsRepository.findById(anyInt())).thenReturn(flowOf())

        val findComicUseCase = FindComicUseCase(comicsRepository)
        val result = findComicUseCase(999).firstOrNull()

        assertNull(result)
    }

    @Test
    fun `Invoke handles exceptions from repository`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val exception = RuntimeException("Error fetching comic")
        whenever(comicsRepository.findById(anyInt())).thenThrow(exception)

        val findComicUseCase = FindComicUseCase(comicsRepository)

        try {
            findComicUseCase(1).collect { }
            Assert.fail("Exception was expected but not thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
