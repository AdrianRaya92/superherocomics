package com.ayardreams.superherocomics.ui.detail

import app.cash.turbine.test
import com.ayardreams.domain.Error
import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.superherocomics.ui.detail.ComicDetailViewModel.UiState
import com.ayardreams.testshared.sampleComics
import com.ayardreams.usecase.FindComicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ComicDetailViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var findComicUseCase: FindComicUseCase

    private lateinit var vm: ComicDetailViewModel

    private val comic = sampleComics.copy(id = 2)

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        vm = buildViewMoel()
        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(comic = comic), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Error is emitted when comic search fails`() = runTest {
        whenever(findComicUseCase(2)).thenReturn(flow { throw IOException("Test Error") })
        val vm = ComicDetailViewModel(2, findComicUseCase)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val state = awaitItem()
            assertTrue(state.error is Error.Connectivity)
            cancel()
        }
    }

    private fun buildViewMoel(): ComicDetailViewModel {
        whenever(findComicUseCase(2)).thenReturn(flowOf(comic))
        return ComicDetailViewModel(2, findComicUseCase)
    }
}
