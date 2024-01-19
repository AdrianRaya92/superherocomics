package com.ayardreams.superherocomics.ui.comics

import app.cash.turbine.test
import com.ayardreams.apptestshared.buildDatabaseComics
import com.ayardreams.apptestshared.buildRemoteComics
import com.ayardreams.apptestshared.buildRepositoryWith
import com.ayardreams.superherocomics.data.server.RemoteComicsResult
import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.superherocomics.ui.comics.ComicsViewModel.UiState
import com.ayardreams.usecase.GetComicsTotalUseCase
import com.ayardreams.usecase.GetComicsUseCase
import com.ayardreams.usecase.RequestMarvelComicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.ayardreams.superherocomics.data.database.MarvelComics as DatabaseComics

@ExperimentalCoroutinesApi
class ComicsIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `data is loaded from server when local source is empty`() = runTest {
        val remoteData = buildRemoteComics(2, 3, 4)
        val vm = buildViewModelWith(
            localData = emptyList(),
            remoteData = remoteData
        )

        vm.onUiReady()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(marvelComics = emptyList()), awaitItem())
            assertEquals(
                UiState(
                    marvelComics = emptyList(),
                    loading = true,
                    dateComics = "12/01/2024 - 19/01/2024"
                ),
                awaitItem()
            )
            assertEquals(
                UiState(
                    marvelComics = emptyList(),
                    loading = false,
                    dateComics = "12/01/2024 - 19/01/2024",
                    totalComics = "3"
                ),
                awaitItem()
            )

            val comics = awaitItem().marvelComics!!
            assertEquals("Title 2", comics[0].title)
            assertEquals("Title 3", comics[1].title)
            assertEquals("Title 4", comics[2].title)

            cancel()
        }
    }

    @Test
    fun `data is loaded from local source when available`() = runTest {
        val localData = buildDatabaseComics(1, 2, 3)
        val remoteData = buildRemoteComics(4, 5, 6)
        val vm = buildViewModelWith(
            localData = localData,
            remoteData = remoteData
        )

        vm.state.test {
            assertEquals(UiState(), awaitItem())

            val comics = awaitItem().marvelComics!!
            assertEquals("Title 1", comics[0].title)
            assertEquals("Title 2", comics[1].title)
            assertEquals("Title 3", comics[2].title)

            cancel()
        }
    }

    private fun buildViewModelWith(
        localData: List<DatabaseComics> = emptyList(),
        remoteData: List<RemoteComicsResult> = emptyList()
    ): ComicsViewModel {
        val comicRepository = buildRepositoryWith(localData, remoteData)
        val getComicsUseCase = GetComicsUseCase(comicRepository)
        val getComicsTotalUseCase = GetComicsTotalUseCase(comicRepository)
        val requestMarvelComicsUseCase = RequestMarvelComicsUseCase(comicRepository)
        return ComicsViewModel(getComicsUseCase, requestMarvelComicsUseCase, getComicsTotalUseCase)
    }
}
