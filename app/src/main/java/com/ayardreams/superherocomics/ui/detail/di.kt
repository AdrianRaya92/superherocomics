package com.ayardreams.superherocomics.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.ayardreams.superherocomics.di.ComicsId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @ComicsId
    fun provideComicId(savedStateHandle: SavedStateHandle) =
        ComicDetailFragmentArgs.fromSavedStateHandle(savedStateHandle).comicId
}