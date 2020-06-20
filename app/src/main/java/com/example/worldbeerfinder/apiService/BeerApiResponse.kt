package com.example.worldbeerfinder.apiService

import com.google.gson.annotations.SerializedName

class BeerApiResponse {
    @SerializedName("name")
    val beerName: String? = null

    @SerializedName("image_url")
    val imageUrl: String? = null

    @SerializedName("first_brewed")
    val firstBrewed: String? = null

    @SerializedName("tagline")
    val tagLine: String? = null

    @SerializedName("description")
    val description: String? = null
}