package com.ayardreams.apptestshared

import com.ayardreams.superherocomics.data.database.ComicsDao
import com.ayardreams.superherocomics.data.server.MarvelService
import com.ayardreams.superherocomics.data.server.RemoteComicsData
import com.ayardreams.superherocomics.data.server.RemoteComicsResult
import com.ayardreams.superherocomics.data.server.RemoteResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.ayardreams.superherocomics.data.database.MarvelComics as DatabaseComics

class FakeComicsDao(comics: List<DatabaseComics> = emptyList()) : ComicsDao {

    private val inMemoryComics = MutableStateFlow(comics)
    private lateinit var findComicsFlow: MutableStateFlow<DatabaseComics>

    override fun getAll(): Flow<List<DatabaseComics>> = inMemoryComics

    override fun findById(id: Int): Flow<DatabaseComics> {
        findComicsFlow = MutableStateFlow(inMemoryComics.value.first { it.id == id })
        return findComicsFlow
    }

    override suspend fun getFirstCurrentDate(): String = "19/01/2024"

    override suspend fun marvelComicsCount(): Int = inMemoryComics.value.size

    override suspend fun insertMarvelComics(marvelComics: List<DatabaseComics>) {
        inMemoryComics.value = marvelComics

        if (::findComicsFlow.isInitialized) {
            marvelComics.firstOrNull { it.id == findComicsFlow.value.id }
                ?.let { findComicsFlow.value = it }
        }
    }

    override suspend fun deleteAll() {
        inMemoryComics.value = emptyList()

        val emptyComic = DatabaseComics(
            id = -1,
            title = "",
            resume = null,
            modified = "",
            issueNumber = "",
            price = "",
            pageCount = "",
            thumbnail = "",
            currentDate = ""
        )

        if (::findComicsFlow.isInitialized) {
            findComicsFlow.value = emptyComic
        }
    }
}

class FakeRemoteService(private val comics: List<RemoteComicsResult> = emptyList()) : MarvelService {

    override suspend fun listMarvelComics(
        apiKey: String,
        ts: String,
        hash: String,
        dateRange: String,
        limit: Int,
        offset: Int,
    ) = RemoteResult(
        1,
        "status",
        "copyright",
        "attributionText",
        "staattributionHTML",
        RemoteComicsData(0, 100, comics.size, comics.size, comics),
        "etag"
    )
}
