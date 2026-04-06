package com.example.data.remote

data class ExchangeRateDto(
    val base: String,
    val rates: Map<String, Double>
)