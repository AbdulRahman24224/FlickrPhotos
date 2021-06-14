package com.example.flickrphotos.domain


import com.example.flickrphotos.entities.PhotosResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private const val SERVER_BASE_URL = "https://www.flickr.com/services/rest/"
private const val SERVER_API_KEY = "d17378e37e555ebef55ab86c4180e8dc"

val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val retrofit: Retrofit by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    Retrofit.Builder()
        .baseUrl(SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
}

val photosService: PhotosService by lazy {
    retrofit.create(PhotosService::class.java)
}


interface PhotosService {

    @GET("?method=flickr.photos.search")
    fun retrievePhotosList(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 50,
        @Query("text") text: String = "Color",
        @Query("per_page") per_page: Int = 20,
        @Query("api_key") api_key: String = SERVER_API_KEY,
        @Query("page") page: Int = 1,
    ): Single<PhotosResponse>
}