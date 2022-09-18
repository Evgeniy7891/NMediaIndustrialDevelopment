package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long, callback: PostCallback)
    fun save(post: Post, callback: PostCallback)
    fun removeById(id: Long, callback: IdCallback)
    fun deleteLikeById(id: Long, callback: PostCallback )

    fun getAllAsync(callback: Callback<List<Post>>)

    interface Callback<T> {
        fun onSuccess(posts: List<Post>) {}
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
