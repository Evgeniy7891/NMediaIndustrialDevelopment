package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    authorAvatar = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val old = _data.value?.posts.orEmpty()

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })
        println("THREAD - " + Thread.currentThread().id)
    }

    fun save() {
            edited.value?.let {
                repository.save(it, object : PostRepository.PostCallback {
                    override fun onSuccess(posts: Post) {
                        _postCreated.value = Unit
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(_data.value?.copy(posts = old))
                        e.message?.let {
                            val Info = when (it) {
                                "500" -> "Internal Server Error"
                                "404" -> "Not Found "
                                else -> "Error like"
                            }
                            println(Info)

                        }
                    }
                })
            }
            edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) : String {
        val code = repository.likeById(id, object : PostRepository.PostCallback {
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })
        println("!!!! vm like" + code)
        return code
    }

    fun deleteLikeById(id: Long) : String {
       val code = repository.deleteLikeById(id, object : PostRepository.PostCallback {
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                e.message?.let {
                    val Info = when (it) {
                        "500" -> "Internal Server Error"
                        "404" -> "Not Found "
                        else -> "Error like"
                    }
                }
            }

            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })
        println("!!!!!! vm delete" + code)
        return code
    }

    fun removeById(id: Long) : String {
        val code = repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                e.message?.let {
                    val Info = when (it) {
                        "500" -> "Internal Server Error"
                        "404" -> "Not Found "
                        else -> "Error"
                    }
                    println(Info)
                }
            }
        })
        return code
    }
}
