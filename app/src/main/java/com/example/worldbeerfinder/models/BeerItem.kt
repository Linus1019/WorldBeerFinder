package com.example.worldbeerfinder.models

data class BeerItem(
    val imageUrl: String?,
    val name: String,
    val firstBrewed: String,
    val tagLine: String,
    val description: String
)