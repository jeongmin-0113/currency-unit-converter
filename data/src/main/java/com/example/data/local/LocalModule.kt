package com.example.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ConverterDatabase {
        return Room.databaseBuilder(
            context = context,
            ConverterDatabase::class.java,
            "ConverterDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(db: ConverterDatabase): CurrencyDao {
        return db.currencyDao()
    }

    @Provides
    @Singleton
    fun provideConversionResultDao(db: ConverterDatabase): ConversionResultDao {
        return db.conversionResultDao()
    }
}