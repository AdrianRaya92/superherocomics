package com.ayardreams.superherocomics.ui.detail

import app.cash.turbine.test
import com.ayardreams.apptestshared.buildDatabaseComics
import com.ayardreams.apptestshared.buildRepositoryWith
import com.ayardreams.superherocomics.data.server.RemoteComicsResult
import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.superherocomics.ui.detail.ComicDetailViewModel.UiState
import com.ayardreams.usecase.FindComicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import com.ayardreams.superherocomics.data.database.MarvelComics as DatabaseComics

@ExperimentalCoroutinesApi
class ComicDetailIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the comic on start`() = runTest {
        val vm = buildViewModelWith(
            id = 2,
            localData = buildDatabaseComics(1, 2, 3)
        )

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(2, awaitItem().comic!!.id)
            cancel()
        }
    }

    private fun buildViewModelWith(
        id: Int,
        localData: List<DatabaseComics> = emptyList(),
        remoteData: List<RemoteComicsResult> = emptyList()
    ): ComicDetailViewModel {
        val comicsRepository = buildRepositoryWith(localData, remoteData)
        val findComicUseCase = FindComicUseCase(comicsRepository)
        return ComicDetailViewModel(id, findComicUseCase)
    }
}
