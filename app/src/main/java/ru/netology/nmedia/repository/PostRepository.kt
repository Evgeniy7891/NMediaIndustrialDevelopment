package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    //fun getAll(): List<Post>
    fun likeById(id: Long, callback: PostCallback) : String
    fun save(post: Post, callback: PostCallback) : String
    fun removeById(id: Long, callback: Callback<Unit>) : String
    fun deleteLikeById(id: Long, callback: PostCallback ) : String

    fun getAllAsync(callback: Callback<List<Post>>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
    interface PostCallback {
        fun onSuccess(posts: Post) {}
        fun onError(e: Exception) {}
    }
    interface IdCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }
}
