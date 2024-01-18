package com.ayardreams.superherocomics.di

import android.app.Application
import androidx.room.Room
import com.ayardreams.domain.MarvelApi
import com.ayardreams.superherocomics.data.database.ComicsDatabase
import com.ayardreams.superherocomics.data.server.MarvelService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = MarvelApi.publicKey

    @Provides
    @Singleton
    @ApiTs
    fun provideTs(): String = MarvelApi.ts

    @Provides
    @Singleton
    @ApiHash
    fun provideHash(): String = MarvelApi.hash

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.inMemoryDatabaseBuilder(
        app,
        ComicsDatabase::class.java
    ).build()

    @Provides
    @Singleton
    fun provideComicsDao(db: ComicsDatabase) = db.comicsDao()

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "http://localhost:8080"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideRemoteService(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): MarvelService {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}