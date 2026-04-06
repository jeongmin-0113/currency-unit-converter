package com.example.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest.json")
    suspend fun getExchangeRate(@Query("app_id") appId: String): ExchangeRateDto
}