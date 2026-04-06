package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversionResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val fromCode: String,   // 대상 통화 코드
    val fromName: String,   // 대상 통화 이름
    val fromAmount: Double,  // 환전 이전 금액
    val toCode: String,     // 목표 통화 코드
    val toName: String,     // 목표 통화 이름
    val toAmount: Double     // 환전 이후 금액
)
