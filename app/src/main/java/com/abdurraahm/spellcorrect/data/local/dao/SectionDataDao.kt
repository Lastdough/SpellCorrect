package com.abdurraahm.spellcorrect.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDataDao {
    @Query("SELECT count(id) FROM section_table")
    fun totalSectionInDB(): Int

    @Query("SELECT * FROM section_table")
    fun dataInDB(): Flow<List<SectionData>>

    @Query("SELECT * FROM section_table WHERE id = :id")
    fun getSectionDataById(id: Int): Flow<SectionData>

    @Update
    suspend fun updateSectionData(data: SectionData)
}