package com.abdurraahm.spellcorrect.data.local.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abdurraahm.spellcorrect.data.local.converter.IntSetConverter
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.model.SectionData

@Database(
    entities = [SectionData::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(IntSetConverter::class)
abstract class SpellCheckDatabase : RoomDatabase() {
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
    }
}