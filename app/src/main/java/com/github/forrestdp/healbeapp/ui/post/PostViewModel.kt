package com.github.forrestdp.healbeapp.ui.post

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.forrestdp.healbeapp.util.ShrinkedWallPost

class PostViewModel(wallPost: ShrinkedWallPost, app: Application) : ViewModel() {
    private val _selectedPost = MutableLiveData<ShrinkedWallPost>()
    val selectedPost: LiveData<ShrinkedWallPost> = _selectedPost

    init {
        _selectedPost.value = wallPost
    }
}