package com.abdurraahm.spellcorrect.di

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.room.Room
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.source.SpellCheckDatabase
import com.abdurraahm.spellcorrect.data.local.source.WordEntryLocalDataSource
import com.abdurraahm.spellcorrect.data.local.store.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.store.ProgressDataStore
import com.abdurraahm.spellcorrect.data.local.store.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.data.repository.MainRepositoryImpl
import com.abdurraahm.spellcorrect.data.service.SeedGenerator
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
    fun provideTextToSpeech(@ApplicationContext context: Context): TextToSpeech {
        return TextToSpeech(context, null)
    }

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideSpellCheckDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        SpellCheckDatabase::class.java,
        "spell_check_db")
        .createFromAsset("database/section.db")
        .build() // The reason we can construct a database for the repo

    @Provides
    @Singleton
    fun provideSectionDataDao(db: SpellCheckDatabase) = db.sectionDataDao()

    @Provides
    @Singleton
    fun provideSeedGenerator(): SeedGenerator = SeedGenerator()

    @Provides
    @Singleton
    fun provideProgressDataStore(
        @ApplicationContext context: Context
    ): ProgressDataStore = ProgressDataStore(context)


    @Provides
    @Singleton
    fun provideMainRepositoryImpl(
        @ApplicationContext context: Context,
        wordEntryLocalDataSource: WordEntryLocalDataSource,
        wordEntryDataStore: WordEntryDataStore,
        navigationDataStore: NavigationDataStore,
        progressDataStore: ProgressDataStore,
        ttsService: TextToSpeechService,
        sectionDataDao: SectionDataDao,
        seedGenerator: SeedGenerator
    ): MainRepository {
        return MainRepositoryImpl(
            context = context,
            wordEntryLocalDataSource = wordEntryLocalDataSource,
            wordEntryDataStore = wordEntryDataStore,
            navigationDataStore = navigationDataStore,
            ttsService = ttsService,
            sectionDataDao = sectionDataDao,
            seedGenerator = seedGenerator,
            progressDataStore = progressDataStore
        )
    }
}