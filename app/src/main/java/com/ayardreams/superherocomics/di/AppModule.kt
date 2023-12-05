package com.ayardreams.superherocomics.di

import android.app.Application
import androidx.room.Room
import com.ayardreams.data.datasource.ComicsLocalDataSource
import com.ayardreams.data.datasource.ComicsRemoteDataSource
import com.ayardreams.domain.MarvelApi
import com.ayardreams.superherocomics.data.database.ComicsDatabase
import com.ayardreams.superherocomics.data.database.ComicsRoomDataSource
import com.ayardreams.superherocomics.data.server.ComicsServerDataSource
import com.ayardreams.superherocomics.data.server.MarvelService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        ComicsDatabase::class.java,
        "marvels-comics-db"
    ).build()

    @Provides
    @Singleton
    fun provideCharacterDao(db: ComicsDatabase) = db.comicsDao()

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://gateway.marvel.com/"

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

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {
    @Binds
    abstract fun bindLocalDataSource(localDataSource: ComicsRoomDataSource): ComicsLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: ComicsServerDataSource): ComicsRemoteDataSource
}