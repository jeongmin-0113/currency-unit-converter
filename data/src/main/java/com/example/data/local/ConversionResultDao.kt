package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConversionResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConversionResult(result: ConversionResultEntity)

    @Query("select * from ConversionResultEntity order by id desc")
    suspend fun getAllConversionResults(): List<ConversionResultEntity>
}