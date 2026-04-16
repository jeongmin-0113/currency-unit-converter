package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyEntity::class, ConversionResultEntity::class],
    version = 1
)
abstract class ConverterDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun conversionResultDao(): ConversionResultDao
}