package com.example.data.repository

import com.example.data.local.ConversionResultDao
import com.example.data.local.ConversionResultEntity
import com.example.data.local.CurrencyDao
import com.example.data.local.CurrencyEntity
import com.example.data.remote.ExchangeRateApi
import com.example.domain.model.ConversionResult
import com.example.domain.model.Currency
import com.example.domain.repository.CurrencyRepository
import javax.inject.Inject
import com.example.data.BuildConfig

class CurrencyRepositoryImpl @Inject constructor(
    val currencyDao: CurrencyDao,
    val conversionResultDao: ConversionResultDao,
    val api: ExchangeRateApi
) : CurrencyRepository {
    override suspend fun updateCurrency(): Result<List<Currency>> {
        val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY

        return runCatching {
            val response = api.getExchangeRate(apiKey)

            val entities = response.rates.map { (code, rate) ->
                CurrencyEntity(
                    code = code,
                    name = code,
                    rate = rate
                )
            }
            currencyDao.saveCurrency(entities)

            entities.map { entity ->
                Currency(
                    code = entity.code,
                    name = entity.name,
                    rate = entity.rate
                )
            }
        }
    }

    override suspend fun getCurrency(code: String): Currency? {
        return currencyDao.getCurrency(code)?.let {
            Currency(it.code, it.name, it.rate)
        }
    }

    override suspend fun saveConversionResult(result: ConversionResult) {
        val entity = ConversionResultEntity(
            fromCode = result.fromCode,
            fromName = result.fromName,
            fromAmount = result.fromAmount,
            toCode = result.toCode,
            toName = result.toName,
            toAmount = result.toAmount
        )

        conversionResultDao.saveConversionResult(entity)
    }

    override suspend fun getAllConversionResults(): List<ConversionResult> {
        return conversionResultDao.getAllConversionResults().map { entity ->
            ConversionResult(
                fromCode = entity.fromCode,
                fromName = entity.fromName,
                fromAmount = entity.fromAmount,
                toCode = entity.toCode,
                toName = entity.toName,
                toAmount = entity.toAmount
            )
        }
    }
}