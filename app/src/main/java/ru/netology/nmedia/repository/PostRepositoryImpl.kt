package ru.netology.nmedia.repository

import android.widget.Toast
import androidx.annotation.MainThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.activity.FeedFragment
import ru.netology.nmedia.api.PostApiServiceHolder
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl() : PostRepository {
    var codeError = ""
    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiServiceHolder.service.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.PostCallback): String {
        PostApiServiceHolder.service.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    codeError = response.code().toString()
                    return
                }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                codeError = t.message.toString()
                callback.onError(t as Exception)
            }
        })
        return codeError
    }

    override fun deleteLikeById(id: Long, callback: PostRepository.PostCallback): String {
        PostApiServiceHolder.service.disklikedById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    codeError = response.code().toString()
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                codeError = t.message.toString()
                callback.onError(t as Exception)
            }
        })
        return codeError
    }

    override fun save(post: Post, callback: PostRepository.PostCallback): String {
        PostApiServiceHolder.service.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    codeError = response.code().toString()
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                codeError = t.message.toString()
                callback.onError(t as Exception)
            }
        })
        return codeError
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>): String {
        PostApiServiceHolder.service.delete(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    codeError = response.code().toString()
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                codeError = t.message.toString()
                callback.onError(t as Exception)
            }
        })
        return codeError
    }
}
