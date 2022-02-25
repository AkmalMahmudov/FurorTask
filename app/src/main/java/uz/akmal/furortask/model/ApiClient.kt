package uz.akmal.furortask.model

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiClient {
    private val logging =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logging)
        .build()
    var gson = GsonBuilder()
        .setLenient()
        .create()!!
    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl("http://94.158.54.194:9092/")
//            .addConverterFactory(
//            ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
}