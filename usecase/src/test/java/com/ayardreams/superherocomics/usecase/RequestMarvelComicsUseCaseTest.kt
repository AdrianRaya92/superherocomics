package com.ayardreams.superherocomics.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.domain.Error
import com.ayardreams.usecase.RequestMarvelComicsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RequestMarvelComicsUseCaseTest {

    private val currentDateMocked = "19/01/2024"
    private val rangeDateMocked = "10/01/2024-19/01/2024"
    private val offsetMocked = 100

    @Test
    fun `Invoke calls comics repository`(): Unit = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val requestMarvelComicsUseCase = RequestMarvelComicsUseCase(comicsRepository)

        requestMarvelComicsUseCase(currentDateMocked, rangeDateMocked, offsetMocked)

        verify(comicsRepository).requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)
    }

    @Test
    fun `Invoke returns correct error response`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val errorResponse = Error.Server(500)
        whenever(
            comicsRepository.requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)
        ).thenReturn(errorResponse)

        val requestMarvelComicsUseCase = RequestMarvelComicsUseCase(comicsRepository)
        val result = requestMarvelComicsUseCase(currentDateMocked, rangeDateMocked, offsetMocked)

        assertEquals(errorResponse, result)
    }

    @Test
    fun `Invoke handles exceptions from repository`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val exception = RuntimeException("Error fetching comics")
        val currentDateMocked = "19/01/2024"
        val rangeDateMocked = "10/01/2024-19/01/2024"
        val offsetMocked = 100
        whenever(
            comicsRepository.requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)
        ).thenThrow(exception)

        val requestMarvelComicsUseCase = RequestMarvelComicsUseCase(comicsRepository)

        try {
            requestMarvelComicsUseCase(currentDateMocked, rangeDateMocked, offsetMocked)
            fail("Exception was expected but not thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
