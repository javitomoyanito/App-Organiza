package com.example.proyectobase.ApiRegionComuna

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ApiDuocRegionComuna {

    /**
     * ambiente de ejecucion, podria ser desarrollo, certificacion o
     * produccion
     */
    private const val BASE_URL = "https://uxdynamic.cl/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val http = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: ApiRegionComunaServicio by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiRegionComunaServicio::class.java)
    }
}