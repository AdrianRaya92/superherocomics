package com.ayardreams.superherocomics.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.usecase.GetComicsTotalUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetComicTotalUseCaseTest {

    @Test
    fun `Invoke calls comic repository`(): Unit = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val getComicsTotalUseCase = GetComicsTotalUseCase(comicsRepository)

        getComicsTotalUseCase()

        verify(comicsRepository).comicsTotal()
    }

    @Test
    fun `Invoke returns correct total number of comics`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val comicTotal = 55
        whenever(comicsRepository.comicsTotal()).thenReturn(comicTotal)

        val getComicsTotalUseCase = GetComicsTotalUseCase(comicsRepository)
        val result = getComicsTotalUseCase()

        assertEquals(comicTotal, result)
    }

    @Test
    fun `Invoke handles exceptions from repository`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val exception = RuntimeException("Error fetching comics total")
        whenever(comicsRepository.comicsTotal()).thenThrow(exception)

        val getComicsTotalUseCase = GetComicsTotalUseCase(comicsRepository)

        try {
            getComicsTotalUseCase()
            fail("Exception was expected but not thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
