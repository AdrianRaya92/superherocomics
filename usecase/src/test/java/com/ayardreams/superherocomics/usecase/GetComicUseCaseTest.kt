package com.ayardreams.superherocomics.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.testshared.sampleComics
import com.ayardreams.usecase.GetComicsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetComicUseCaseTest {

    @Test
    fun `Invoke calls comic repository`(): Unit = runBlocking {
        val comics = flowOf(listOf(sampleComics.copy(id = 1)))
        val getComicsUseCase = GetComicsUseCase(
            mock {
                on { comicsMarvel } doReturn comics
            }
        )

        val result = getComicsUseCase()

        assertEquals(comics, result)
    }

    @Test
    fun `Invoke returns correct comics`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val sampleComicsList = listOf(sampleComics.copy(id = 1))
        val comicsFlow = flowOf(sampleComicsList)
        whenever(comicsRepository.comicsMarvel).thenReturn(comicsFlow)

        val getComicsUseCase = GetComicsUseCase(comicsRepository)
        val result = getComicsUseCase().first()

        assertEquals(sampleComicsList, result)
    }

    @Test
    fun `Invoke handles exceptions from repository`() = runBlocking {
        val comicsRepository = mock<ComicsRepository>()
        val exception = RuntimeException("Error fetching comics")
        whenever(comicsRepository.comicsMarvel).thenThrow(exception)

        val getComicsUseCase = GetComicsUseCase(comicsRepository)

        try {
            getComicsUseCase().collect { }
            fail("Exception was expected but not thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
