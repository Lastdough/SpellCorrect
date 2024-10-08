package com.abdurraahm.spellcorrect.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.room.Room
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.dao.WordEntryDao
import com.abdurraahm.spellcorrect.data.local.source.SpellCheckDatabase
import com.abdurraahm.spellcorrect.data.local.source.WordEntryLocalDataSource
import com.abdurraahm.spellcorrect.data.local.store.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.store.ProgressDataStore
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.data.repository.MainRepositoryImpl
import com.abdurraahm.spellcorrect.data.service.SeedGenerator
import com.abdurraahm.spellcorrect.data.service.SpeechToTextManager
import com.abdurraahm.spellcorrect.data.service.TextToSpeechService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
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

    @Singleton
    @Provides
    fun provideSpellCheckDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        SpellCheckDatabase::class.java,
        "spell_check_db")
        .createFromAsset("database/section.db")
        .build()

    @Provides
    @Singleton
    fun provideSectionDataDao(db: SpellCheckDatabase) = db.sectionDataDao()

    @Provides
    @Singleton
    fun provideWordEntryDao(db: SpellCheckDatabase) = db.wordEntryDao()

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
    fun provideWordEntryLocalDataStore(
        @ApplicationContext context: Context
    ): WordEntryLocalDataSource = WordEntryLocalDataSource(context)

    @Provides
    @ApplicationContext
    fun provideApplication(application: Application): Application {
        return application
    }

    @Provides
    @ActivityContext
    fun provideActivity(activity: Activity): Activity {
        return activity
    }

    @Provides
    @Singleton
    @ApplicationContext
    fun provideSpeechToTextManager(
        @ApplicationContext application: Application
    ): SpeechToTextManager {
        return SpeechToTextManager(application)
    }

    @Provides
    @Singleton
    fun provideMainRepositoryImpl(
        @ApplicationContext context: Context,
        wordEntryLocalDataSource: WordEntryLocalDataSource,
        navigationDataStore: NavigationDataStore,
        progressDataStore: ProgressDataStore,
        ttsService: TextToSpeechService,
        speechToTextManager: SpeechToTextManager,
        sectionDataDao: SectionDataDao,
        wordEntryDao: WordEntryDao,
        seedGenerator: SeedGenerator
    ): MainRepository {
        return MainRepositoryImpl(
            context = context,
            wordEntryLocalDataSource = wordEntryLocalDataSource,
            navigationDataStore = navigationDataStore,
            ttsService = ttsService,
            sectionDataDao = sectionDataDao,
            seedGenerator = seedGenerator,
            progressDataStore = progressDataStore,
            speechToTextManager = speechToTextManager,
            wordEntryDao = wordEntryDao
        )
    }
}