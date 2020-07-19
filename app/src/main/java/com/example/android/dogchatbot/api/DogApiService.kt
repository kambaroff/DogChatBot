package com.example.android.dogchatbot.api

import com.example.android.dogchatbot.model.DogApiResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("api/breed/{breed}/{sub_breed}/images/random")
     fun getRandomDogImageWithSB(
        @Path("breed") breed: String,
        @Path("sub_breed") sub_breed: String
    ): Call<DogApiResponse>

    @GET("api/breed/{breed}/images/random")
     fun getRandomDogImage(
        @Path("breed") breed: String
    ): Call<DogApiResponse>

    companion object {
    private const val ENDPOINT = "https://dog.ceo/"

    fun create() : DogApiService{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DogApiService::class.java)
    }
    }
}