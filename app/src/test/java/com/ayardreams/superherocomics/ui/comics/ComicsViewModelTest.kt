package com.ayardreams.superherocomics.ui.comics

import app.cash.turbine.test
import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.superherocomics.ui.comics.ComicsViewModel.UiState
import com.ayardreams.testshared.sampleComics
import com.ayardreams.usecase.GetComicsTotalUseCase
import com.ayardreams.usecase.GetComicsUseCase
import com.ayardreams.usecase.RequestMarvelComicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ComicsViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getComicsUseCase: GetComicsUseCase

    @Mock
    lateinit var getComicsTotalUseCase: GetComicsTotalUseCase

    @Mock
    lateinit var requestMarvelComicsUseCase: RequestMarvelComicsUseCase

    private lateinit var vm: ComicsViewModel

    private val comics = listOf(sampleComics.copy(id = 1))

    @Test
    fun `State is updated with current cached content immediately`() = runTest {
        vm = buildViewModel()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(marvelComics = comics), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting comics`() =
        runTest {
            vm = buildViewModel()
            vm.onUiReady()

            vm.state.test {
                assertEquals(UiState(), awaitItem())
                assertEquals(UiState(marvelComics = comics), awaitItem())
                assertEquals(
                    UiState(
                        marvelComics = comics,
                        loading = true,
                        totalComics = "0",
                        dateComics = "12/01/2024 - 19/01/2024"
                    ),
                    awaitItem()
                )
                assertEquals(
                    UiState(
                        marvelComics = comics,
                        loading = false,
                        totalComics = "4",
                        dateComics = "12/01/2024 - 19/01/2024"
                    ),
                    awaitItem()
                )
                cancel()
            }
        }

    @Test
    fun `Comics are requested when UI screen starts`() = runTest {
        val currentDateMocked = "2024-01-19"
        val rangeDateMocked = "2024-01-12,2024-01-19"
        val offsetMocked = 0
        vm = buildViewModel()
        vm.onUiReady()
        runCurrent()
        verify(requestMarvelComicsUseCase).invoke(currentDateMocked, rangeDateMocked, offsetMocked)
    }

    private fun buildViewModel(): ComicsViewModel {
        whenever(getComicsUseCase()).thenReturn(flowOf(comics))
        runBlocking {
            whenever(getComicsTotalUseCase.invoke()).thenReturn(4)
        }
        return ComicsViewModel(getComicsUseCase, requestMarvelComicsUseCase, getComicsTotalUseCase)
    }
}
