package com.example.worldbeerfinder.apiService

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {
    @GET("beers")
    fun findBeerList(
        @Query("beer_name") beerName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ) : Call<List<BeerApiResponse>>

    companion object {
        private const val baseUrl = "https://api.punkapi.com/v2/"

        private val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        val client: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}