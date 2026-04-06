package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrency(currencies: List<CurrencyEntity>)

    @Query("select * from CurrencyEntity")
    suspend fun getAllCurrencies(): List<CurrencyEntity>

    @Query("select * from CurrencyEntity where code = :code")
    suspend fun getCurrency(code: String): CurrencyEntity?
}