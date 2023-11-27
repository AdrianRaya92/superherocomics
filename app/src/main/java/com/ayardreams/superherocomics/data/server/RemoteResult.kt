package com.ayardreams.superherocomics.data.server

data class RemoteResult(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val staattributionHTMLtus: String,
    val data: RemoteCharacterData,
    val etag: String
)

data class RemoteCharacterData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<RemoteCharacterResult>
)

data class RemoteCharacterResult(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val resourceURI: String,
    val urls: List<RemoteCharacterUrls>,
    val thumbnail: RemoteCharacterThumbnail,
    val comics: RemoteCharacterComics,
    val stories: RemoteCharacterStories,
    val events: RemoteCharacterEvents,
    val series: RemoteCharacterSeries
)

data class RemoteCharacterUrls(
    val type: String,
    val url: String
)

data class RemoteCharacterThumbnail(
    val path: String,
    val extension: String
)

data class RemoteCharacterComics(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsComics>
)

data class RemoteItemsComics(
    val resourceURI: String,
    val name: String
)

data class RemoteCharacterStories(
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

data class RemoteCharacterEvents(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsEvents>
)

data class RemoteItemsEvents(
    val resourceURI: String,
    val name: String
)

data class RemoteCharacterSeries(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<RemoteItemsSeries>
)

data class RemoteItemsSeries(
    val resourceURI: String,
    val name: String
)
