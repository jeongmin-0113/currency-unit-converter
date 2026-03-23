package com.example.domain.usecase

import com.example.domain.model.ConversionResult
import com.example.domain.model.Currency
import com.example.domain.repository.CurrencyRepository
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun convert(fromCode: String, amount: Double, toCode: String): Result<ConversionResult> {
        val fromCurrency: Currency = currencyRepository.getCurrency(fromCode) ?: return Result.failure(
            IllegalArgumentException("유효한 통화가 아닙니다."))
        val toCurrency: Currency = currencyRepository.getCurrency(toCode) ?: return Result.failure(
            IllegalArgumentException("유효한 통화가 아닙니다."))

        return runCatching {
            ConversionResult(
                fromCode = fromCurrency.code,
                fromName = fromCurrency.name,
                fromAmount = amount,
                toCode = toCurrency.code,
                toName = toCurrency.name,
                toAmount = amount / fromCurrency.rate * toCurrency.rate
            ).also {
                currencyRepository.saveConversionResult(it)
            }
        }
    }
}