package uz.akmal.furortask.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val logging =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logging)
        .build()

    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl("http://94.158.54.194:9092/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
}