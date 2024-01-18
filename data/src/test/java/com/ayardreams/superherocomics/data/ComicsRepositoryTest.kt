package com.ayardreams.superherocomics.data

import arrow.core.left
import arrow.core.right
import com.ayardreams.data.ComicsRepository
import com.ayardreams.data.datasource.ComicsLocalDataSource
import com.ayardreams.data.datasource.ComicsRemoteDataSource
import com.ayardreams.domain.Error
import com.ayardreams.testshared.sampleComics
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ComicsRepositoryTest {

    @Mock
    lateinit var localDataSource: ComicsLocalDataSource

    @Mock
    lateinit var remoteDataSource: ComicsRemoteDataSource

    private lateinit var comicsRepository: ComicsRepository

    private val localComics = flowOf(listOf(sampleComics.copy(1)))

    @Before
    fun setUp() {
        whenever(localDataSource.comics).thenReturn(localComics)
        comicsRepository = ComicsRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Comics are taken from local data source if available`(): Unit = runBlocking {
        val result = comicsRepository.comicsMarvel

        assertEquals(localComics, result)
    }

    @Test
    fun `Comics are saved to local data source when it's empty`(): Unit = runBlocking {
        val currentDateMocked = "19/01/2024"
        val rangeDateMocked = "10/01/2024-19/01/2024"
        val offsetMocked = 100
        val remoteComics = listOf(sampleComics.copy(2))
        whenever(localDataSource.isEmpty()).thenReturn(true)
        whenever(remoteDataSource.findMarvelComics(any(), any())).thenReturn(remoteComics.right())

        comicsRepository.requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)

        verify(localDataSource).save(remoteComics)
    }

    @Test
    fun `Finding a comic by id is done in local data source`(): Unit = runBlocking {
        val comic = flowOf(sampleComics.copy(id = 5))
        whenever(localDataSource.findById(5)).thenReturn(comic)

        val result = comicsRepository.findById(5)

        assertEquals(comic, result)
    }

    @Test
    fun `ComicsTotal returns correct total number of comics`(): Unit = runBlocking {
        val totalComics = 42
        whenever(localDataSource.getComicsTotal()).thenReturn(totalComics)

        val result = comicsRepository.comicsTotal()

        assertEquals(totalComics, result)
    }

    @Test
    fun `RequestMarvelComics handles errors from remote data source`(): Unit = runBlocking {
        val errorResponse = Error.Server(500)
        whenever(localDataSource.isEmpty()).thenReturn(true)
        whenever(remoteDataSource.findMarvelComics(any(), any())).thenReturn(errorResponse.left())

        val result = comicsRepository.requestMarvelComics("19/01/2024", "10/01/2024-19/01/2024", 100)

        assertEquals(errorResponse, result)
    }

    @Test
    fun `RequestMarvelComics does not call remote data source when not needed`(): Unit = runBlocking {
        val currentDateMocked = "19/01/2024"
        val rangeDateMocked = "10/01/2024-19/01/2024"
        val offsetMocked = 101

        whenever(localDataSource.isEmpty()).thenReturn(false)
        whenever(localDataSource.getFirstCurrentDate()).thenReturn(currentDateMocked)

        comicsRepository.requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)

        verify(remoteDataSource, never()).findMarvelComics(rangeDateMocked, offsetMocked)
    }

    @Test
    fun `RequestMarvelComics deletes all comics from local data source when needed`(): Unit = runBlocking {
        val currentDateMocked = "02/01/2024"
        val rangeDateMocked = "10/01/2024-19/01/2024"
        val offsetMocked = 101

        whenever(localDataSource.isEmpty()).thenReturn(false)
        whenever(localDataSource.getFirstCurrentDate()).thenReturn("01/01/2024")
        whenever(
            remoteDataSource.findMarvelComics(rangeDateMocked, offsetMocked)
        ).thenReturn(listOf(sampleComics.copy(3)).right())

        comicsRepository.requestMarvelComics(currentDateMocked, rangeDateMocked, offsetMocked)

        verify(localDataSource).deleteAllComics()
    }
}
