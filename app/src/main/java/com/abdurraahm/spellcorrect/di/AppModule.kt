package com.abdurraahm.spellcorrect.di

import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataSource
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.repository.WordEntryRepository
import com.abdurraahm.spellcorrect.data.repository.WordEntryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideMainRepositoryImpl(
        wordEntryDataSource: WordEntryDataSource,
        wordEntryDataStore: WordEntryDataStore
    ): WordEntryRepository {
        return WordEntryRepositoryImpl(wordEntryDataSource, wordEntryDataStore)
    }
}