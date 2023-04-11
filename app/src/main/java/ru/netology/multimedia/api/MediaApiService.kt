package ru.netology.multimedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.multimedia.MainActivity.Companion.BASE_URL
import ru.netology.multimedia.dto.Media

private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface MediaApiService {
    @GET("album.json")
    suspend fun getMedia(): Media

}

object MediaApi {
    val retrofitService: MediaApiService by lazy {
        retrofit.create(MediaApiService::class.java)
    }
}