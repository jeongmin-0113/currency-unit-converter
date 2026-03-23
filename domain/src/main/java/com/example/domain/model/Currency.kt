package com.example.domain.model

// 통화 정보
data class Currency(
    val code: String,   // 통화 코드
    val name: String,   // 통화 이름
    val rate: Double,      // 환율
)
