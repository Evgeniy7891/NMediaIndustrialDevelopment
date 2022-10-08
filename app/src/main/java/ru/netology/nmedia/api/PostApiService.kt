package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

//private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"
private const val BASE_URL = "http://192.168.0.101:9999/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface PostApiService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("posts/{id}")
    fun getById(@Path("id") id: Long) : Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long) : Call<Post>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun delete(@Path("id") postId: Long): Call<Unit>

    @DELETE("posts/{id}/likes")
    fun disklikedById(@Path("id") id: Long) : Call<Post>
}

object PostApiServiceHolder {
    val service: PostApiService by lazy { retrofit.create(PostApiService::class.java) }
}