package com.github.forrestdp.healbeapp.ui.feed

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.util.FragmentToolbarable
import com.github.forrestdp.healbeapp.util.ShrinkedWallPost
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.launch
import tk.skeptick.vk.apiclient.VkApiClient
import tk.skeptick.vk.apiclient.VkApiUser
import tk.skeptick.vk.apiclient.domain.models.WallPost
import tk.skeptick.vk.apiclient.execute

class FeedViewModel : ViewModel(), FragmentToolbarable {

    override val title = "Лента"
    override lateinit var titleImage: Drawable

    private val _posts: MutableLiveData<List<ShrinkedWallPost>>
    val posts: LiveData<List<ShrinkedWallPost>>

    private val _navigateToSelectedPost = MutableLiveData<ShrinkedWallPost?>()
    val navigateToSelectedPost: LiveData<ShrinkedWallPost?> = _navigateToSelectedPost

    init {
        _posts = MutableLiveData<List<ShrinkedWallPost>>()
        posts = _posts
        getPosts()
    }

    fun displayPostDetails(wallPost: ShrinkedWallPost) {
        _navigateToSelectedPost.value = wallPost
    }

    fun displayPostDetailsComplete() {
        _navigateToSelectedPost.value = null
    }

    private fun getPosts() {
        val api = VkApiUser(
            VkApiClient(
                "77c481fd77c481fd77c481fd2e77b23213777c477c481fd17fb084e6a6e0c09eab0229d",
                HttpClient(CIO)
            )
        )
        viewModelScope.launch {
            _posts.value = api
                .wall
                .get(-140230980).execute().get()
                .items
                .map { wallPost ->
                    ShrinkedWallPost(
                        wallPost.id,
                        wallPost.attachments?.get(0)?.photo?.sizes?.first {
                            it.type?.equals("x") ?: false
                        }?.src ?: "null",
                        wallPost.text,
                    )
                }
                .filter {
                    it.imageSrcUrl != "null"
                }
        }
    }
}