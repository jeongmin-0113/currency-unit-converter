package com.example.data.repository

import com.example.data.local.ConversionResultDao
import com.example.data.local.CurrencyDao
import com.example.data.local.CurrencyEntity
import com.example.data.remote.ExchangeRateApi
import com.example.data.remote.ExchangeRateDto
import com.example.domain.model.Currency
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.math.exp

class CurrencyRepositoryImplTest {
    private lateinit var currencyDao: CurrencyDao
    private lateinit var conversionResultDao: ConversionResultDao
    private lateinit var exchangeRateApi: ExchangeRateApi
    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @BeforeEach
    fun setUp() {
        currencyDao = mockk<CurrencyDao>()
        conversionResultDao = mockk<ConversionResultDao>()
        exchangeRateApi = mockk<ExchangeRateApi>()
        currencyRepositoryImpl = CurrencyRepositoryImpl(currencyDao, conversionResultDao, exchangeRateApi)
    }

    @Test
    fun updateCurrencySuccess() = runTest {

        coEvery { exchangeRateApi.getExchangeRate(any()) } returns ExchangeRateDto(
            base = "EUR",
            rates = mapOf(
                "KRW" to 1723.48,
                "USD" to 1.18,
            )
        )

        coEvery { currencyDao.saveCurrency(any()) } just Runs

        val result = currencyRepositoryImpl.updateCurrency()
        val expect = Result.success(listOf<Currency>(
            Currency(
                code = "KRW",
                name = "KRW",
                rate = 1723.48
            ),
            Currency(
                code = "USD",
                name = "USD",
                rate = 1.18
            )
        ))

        assertEquals(expect, result)
        coVerify { currencyDao.saveCurrency(match { entities ->
            entities.find { it.code == "KRW" }?.rate == 1723.48
                    && entities.find { it.code == "USD" }?.rate == 1.18
        }) }
    }

    @Test
    fun updateCurrencyFailure() = runTest {
        val exception = HttpException(Response.error<Any>(404, "{\"error\": \"not found\"}".toResponseBody(null)))
        coEvery { exchangeRateApi.getExchangeRate(any()) } throws exception
        coEvery { currencyDao.saveCurrency(any()) } just Runs

        val result = currencyRepositoryImpl.updateCurrency()
        val expect = Result.failure<HttpException>(exception)

        assertEquals(expect, result)
    }

    @Test
    fun getCurrencySuccess() = runTest {
        val code = "KRW"
        coEvery { currencyDao.getCurrency(code) } returns CurrencyEntity(code, code, 1450.0)

        val result = currencyRepositoryImpl.getCurrency(code)
        val expect = Currency(code, code, 1450.0)

        assertEquals(expect, result)
    }

    @Test
    fun getCurrencyFailure() = runTest {
        val code = "KRW"
        coEvery { currencyDao.getCurrency(code) } returns null

        val result = currencyRepositoryImpl.getCurrency(code)

        assertNull(result)
    }
}