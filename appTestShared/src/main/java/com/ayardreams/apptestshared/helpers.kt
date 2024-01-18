package com.ayardreams.apptestshared

import com.ayardreams.data.ComicsRepository
import com.ayardreams.superherocomics.data.database.ComicsRoomDataSource
import com.ayardreams.superherocomics.data.server.ComicsServerDataSource
import com.ayardreams.superherocomics.data.server.RemoteComicsCharacter
import com.ayardreams.superherocomics.data.server.RemoteComicsCollectedIssues
import com.ayardreams.superherocomics.data.server.RemoteComicsCollections
import com.ayardreams.superherocomics.data.server.RemoteComicsCreators
import com.ayardreams.superherocomics.data.server.RemoteComicsDates
import com.ayardreams.superherocomics.data.server.RemoteComicsEvents
import com.ayardreams.superherocomics.data.server.RemoteComicsImages
import com.ayardreams.superherocomics.data.server.RemoteComicsPrices
import com.ayardreams.superherocomics.data.server.RemoteComicsResult
import com.ayardreams.superherocomics.data.server.RemoteComicsSeries
import com.ayardreams.superherocomics.data.server.RemoteComicsStories
import com.ayardreams.superherocomics.data.server.RemoteComicsTextObjects
import com.ayardreams.superherocomics.data.server.RemoteComicsThumbnail
import com.ayardreams.superherocomics.data.server.RemoteComicsUrls
import com.ayardreams.superherocomics.data.server.RemoteComicsVariants
import com.ayardreams.superherocomics.data.server.RemoteItemsCharacters
import com.ayardreams.superherocomics.data.server.RemoteItemsCreators
import com.ayardreams.superherocomics.data.server.RemoteItemsEvents
import com.ayardreams.superherocomics.data.server.RemoteItemsStories
import com.ayardreams.superherocomics.data.database.MarvelComics as DatabaseComics

fun buildRepositoryWith(
    localData: List<DatabaseComics>,
    remoteData: List<RemoteComicsResult>
): ComicsRepository {
    val localDataSource = ComicsRoomDataSource(FakeComicsDao(localData))
    val remoteDataSource =
        ComicsServerDataSource("1234", "1234", "1234", FakeRemoteService(remoteData))
    return ComicsRepository(localDataSource, remoteDataSource)
}

fun buildDatabaseComics(vararg id: Int) = id.map {
    DatabaseComics(
        id = it,
        title = "Title $it",
        resume = "Resume $it",
        modified = "Modified $it",
        issueNumber = "IssueNumber $it",
        price = "Price $it",
        pageCount = "PageCount $it",
        thumbnail = "Thumbnail $it",
        currentDate = "CurrentDate $it",
    )
}

fun buildRemoteComics(vararg id: Int) = id.map {
    RemoteComicsResult(
        id = it,
        digitalId = "digitalId $it",
        title = "Title $it",
        issueNumber = 2.0,
        variantDescription = "VariantDescription $it",
        description = "Description $it",
        modified = "Modified $it",
        isbn = "Isbn $it",
        upc = "Upc $it",
        diamondCode = "DiamondCode $it",
        ean = "Ean $it",
        issn = "Issn $it",
        format = "Format $it",
        pageCount = 100,
        textObject = RemoteComicsTextObjects("Type $it", "Language $it", "Text $it"),
        resourceURI = "ResourceURI $it",
        urls = listOf(RemoteComicsUrls("type $it", "url $it")),
        series = RemoteComicsSeries("ResourceUri $it", "Name $it"),
        variants = listOf(RemoteComicsVariants("ResourceUri $it", "Name $it")),
        collections = listOf(RemoteComicsCollections("ResourceUri $it", "Name $it")),
        collectedIssues = listOf(RemoteComicsCollectedIssues("ResourceUri $it", "Name $it")),
        dates = listOf(RemoteComicsDates("Type $it", "Date $it")),
        prices = listOf(RemoteComicsPrices("Type $it", 9.99F)),
        thumbnail = RemoteComicsThumbnail("Path $it", "Extension $it"),
        images = listOf(RemoteComicsImages("Path $it", "Extension $it")),
        creators = RemoteComicsCreators(
            it,
            it,
            "CollectionUri $it",
            listOf(RemoteItemsCreators("ResourceUri $it", "Name $it", "Role $it"))
        ),
        characters = RemoteComicsCharacter(
            it,
            it,
            "CollectionUri $it",
            listOf(RemoteItemsCharacters("ResourceUri $it", "Name $it", "Role $it"))
        ),
        stories = RemoteComicsStories(
            it,
            it,
            "CollectionUri $it",
            listOf(RemoteItemsStories("ResourceUri $it", "Name $it", "Role $it"))
        ),
        events = RemoteComicsEvents(
            it,
            it,
            "CollectionUri $it",
            listOf(RemoteItemsEvents("ResourceUri $it", "Name $it"))
        )
    )
}
