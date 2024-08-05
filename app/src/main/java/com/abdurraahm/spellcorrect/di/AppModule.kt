package com.abdurraahm.spellcorrect.di

import com.abdurraahm.spellcorrect.data.local.source.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataSource
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideMainRepositoryImpl(
        wordEntryDataSource: WordEntryDataSource,
        wordEntryDataStore: WordEntryDataStore,
        navigationDataStore: NavigationDataStore
    ): MainRepository {
        return MainRepositoryImpl(wordEntryDataSource, wordEntryDataStore, navigationDataStore)
    }

}