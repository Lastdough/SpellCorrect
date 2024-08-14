package com.abdurraahm.spellcorrect.data.local.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abdurraahm.spellcorrect.data.local.converter.IntSetConverter
import com.abdurraahm.spellcorrect.data.local.converter.StringListConverter
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.dao.WordEntryDao
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.model.SectionData

@Database(
    entities = [SectionData::class, WordEntry::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(StringListConverter::class, IntSetConverter::class)
abstract class SpellCheckDatabase : RoomDatabase() {
    abstract fun wordEntryDao(): WordEntryDao
    abstract fun sectionDataDao(): SectionDataDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE section_table 
                    ADD COLUMN last_updated INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
                )
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE new_word_entry (
                            word TEXT PRIMARY KEY NOT NULL,
                            definition TEXT NOT NULL,
                            type TEXT NOT NULL,
                            ipa TEXT NOT NULL,
                            section TEXT NOT NULL
                        )    
                    """.trimIndent()
                )
            }
        }

    }
}