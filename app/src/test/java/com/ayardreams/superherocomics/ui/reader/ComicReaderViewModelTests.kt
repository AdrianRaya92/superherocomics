package com.ayardreams.superherocomics.ui.reader

import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.testshared.sampleReaderComics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicReaderViewModelTests {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `onUiReady updates state with provided comic`() = runTest {
        val viewModel = ComicReaderViewModel()
        val testComic = sampleReaderComics

        val job = launch {
            viewModel.state.collect { state ->
                Assert.assertEquals(testComic, state.readerComics)
            }
        }

        viewModel.onUiReady(testComic)

        delay(100)

        job.cancel()
    }
}
