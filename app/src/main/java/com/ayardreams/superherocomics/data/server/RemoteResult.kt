package com.ayardreams.superherocomics.data.server

data class RemoteResult(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val staattributionHTMLtus: String,
    val data: RemoteComicsData,
    val etag: String
)

data class RemoteComicsData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<RemoteComicsResult>
)

data class RemoteComicsResult(
    val id: Int,
    val digitalId: String,
    val title: String,
    val issueNumber: Double,
    val variantDescription: String,
    val description: String,
    val modified: String,
    val isbn: String,
    val upc: String,
    val diamondCode: String,
    val ean: String,
    val issn: String,
    val format: String,
    val pageCount: Int,
    val textObject: RemoteComicsTextObjects,
    val resourceURI: String,
    val urls: List<RemoteComicsUrls>,
    val series: RemoteComicsSeries,
    val variants: List<RemoteComicsVariants>,
    val collections: List<RemoteComicsCollections>,
    val collectedIssues: List<RemoteComicsCollectedIssues>,
    val dates: List<RemoteComicsDates>,
    val prices: List<RemoteComicsPrices>,
    val thumbnail: RemoteComicsThumbnail,
    val images: List<RemoteComicsImages>,
    val creators: RemoteComicsCreators,
    val characters: RemoteComicsCharacter,
    val stories: RemoteComicsStories,
    val events: RemoteComicsEvents
)

data class RemoteComicsTextObjects(
    val type: String,
    val language: String,
    val text: String
)

data class RemoteComicsUrls(
    val type: String,
    val url: String
)

data class RemoteComicsSeries(
    val resourceURI: String,
    val name: String
)

data class RemoteComicsVariants(
    val resourceURI: String,
    val name: String
)

data class RemoteComicsCollections(
    val resourceURI: String,
    val name: String
)

data class RemoteComicsCollectedIssues(
    val resourceURI: String,
    val name: String
)

data class RemoteComicsDates(
    val type: String,
    val date: String
)

data class RemoteComicsPrices(
    val type: String,
    val price: Float
)

data class RemoteComicsThumbnail(
    val path: String,
    val extension: String
)

data class RemoteComicsImages(
    val path: String,
    val extension: String
)

data class RemoteComicsCreators(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsCreators>
)

data class RemoteItemsCreators(
    val resourceURI: String,
    val name: String,
    val role: String
)

data class RemoteComicsCharacter(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsCharacters>
)

data class RemoteItemsCharacters(
    val resourceURI: String,
    val name: String,
    val role: String
)

data class RemoteComicsStories(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsStories>
)

data class RemoteItemsStories(
    val resourceURI: String,
    val name: String,
    val type: String
)

data class RemoteComicsEvents(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsEvents>
)

data class RemoteItemsEvents(
    val resourceURI: String,
    val name: String
)