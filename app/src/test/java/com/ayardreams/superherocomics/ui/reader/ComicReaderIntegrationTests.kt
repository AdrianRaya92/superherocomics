package com.ayardreams.superherocomics.ui.reader

import app.cash.turbine.test
import com.ayardreams.superherocomics.testrules.CoroutinesTestRule
import com.ayardreams.testshared.sampleReaderComics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicReaderIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the reader comic on start`() = runTest {
        val testComic = sampleReaderComics

        val vm = ComicReaderViewModel().apply {
            onUiReady(testComic)
        }

        vm.state.test {
            val initialState = awaitItem()
            assertNotNull(initialState.readerComics)
            Assert.assertEquals(testComic, initialState.readerComics)

            cancel()
        }
    }
}
