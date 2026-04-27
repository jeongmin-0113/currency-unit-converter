package com.example.domain.repository

import com.example.domain.model.ConversionResult
import com.example.domain.model.Currency

interface CurrencyRepository {
    suspend fun updateCurrency(): Result<List<Currency>>
    suspend fun getCurrency(code: String): Currency?
    suspend fun saveConversionResult(result: ConversionResult)
    suspend fun getAllConversionResults(): List<ConversionResult>
}