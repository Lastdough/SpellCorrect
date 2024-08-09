package com.abdurraahm.spellcorrect.di

import android.content.Context
import android.speech.tts.TextToSpeech
import com.abdurraahm.spellcorrect.data.local.store.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.source.WordEntryLocalDataSource
import com.abdurraahm.spellcorrect.data.local.store.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.data.repository.MainRepositoryImpl
import com.abdurraahm.spellcorrect.data.service.TextToSpeechService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMainRepositoryImpl(
        @ApplicationContext context: Context,
        wordEntryLocalDataSource: WordEntryLocalDataSource,
        wordEntryDataStore: WordEntryDataStore,
        navigationDataStore: NavigationDataStore,
        ttsService: TextToSpeechService
    ): MainRepository {
        return MainRepositoryImpl(
            context = context,
            wordEntryLocalDataSource = wordEntryLocalDataSource,
            wordEntryDataStore = wordEntryDataStore,
            navigationDataStore = navigationDataStore,
            ttsService = ttsService
        )
    }

    @Provides
    @Singleton
    fun provideTextToSpeech(@ApplicationContext context: Context): TextToSpeech {
        return TextToSpeech(context, null)
    }

}