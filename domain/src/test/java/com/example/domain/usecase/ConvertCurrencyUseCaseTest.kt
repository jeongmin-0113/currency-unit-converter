package com.example.domain.usecase

import com.example.domain.model.ConversionResult
import com.example.domain.model.Currency
import com.example.domain.repository.CurrencyRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConvertCurrencyUseCaseTest {
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase

    @BeforeEach
    fun setUp() {
        currencyRepository = mockk<CurrencyRepository>()
        convertCurrencyUseCase = ConvertCurrencyUseCase(currencyRepository)

        coEvery { currencyRepository.getCurrency("KRW") } returns Currency("KRW", "한국 원", 1512.28)
        coEvery { currencyRepository.getCurrency("JPY") } returns Currency("JPY", "일본 엔", 159.50)
        coEvery { currencyRepository.getCurrency("123") } returns null
        coEvery { currencyRepository.saveConversionResult(any()) } just Runs
    }

    @Test
    fun convertSuccess() = runTest {
        val result = convertCurrencyUseCase.convert("KRW", 2000.0, "JPY")
        val expect = ConversionResult(
            fromCode = "KRW",
            fromName = "한국 원",
            fromAmount = 2000.0,
            toCode = "JPY",
            toName = "일본 엔",
            toAmount = 210.9397730578993
        )
        assertEquals(expect.toAmount, result.getOrNull()!!.toAmount, 1e-6)
        coVerify { currencyRepository.saveConversionResult(any()) }
    }

    @Test
    fun convertFailureNoFromCode() = runTest {
        val result = convertCurrencyUseCase.convert("123", 2000.0, "JPY")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun convertFailureNoToCode() = runTest {
        val result = convertCurrencyUseCase.convert("KRW", 2000.0, "123")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!! is IllegalArgumentException)
    }
}