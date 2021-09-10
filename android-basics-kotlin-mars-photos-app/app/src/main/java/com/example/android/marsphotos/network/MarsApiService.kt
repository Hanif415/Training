package com.example.android.marsphotos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// base url
private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com/"

// create a moshi object
private val moshi = Moshi.Builder()
    // create a converter
    .add(KotlinJsonAdapterFactory())
    // build the object
    .build()

// build a retrofit
private val retrofit = Retrofit.Builder()
    // add converter by moshi
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    // add base url
    .baseUrl(BASE_URL)
    // build
    .build()

// this interface is representative how retrofit talks to web server
interface MarsApiService {
    // get mars photo
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

// create a singleton objects
object MarsApi {
    // create a retrofit instance
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
