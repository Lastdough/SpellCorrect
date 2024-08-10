package com.abdurraahm.spellcorrect.data.local.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.model.SectionData

@Database(
    entities = [SectionData::class],
    version = 1
)
abstract class SpellCheckDatabase : RoomDatabase() {
    abstract fun sectionDataDao(): SectionDataDao
}