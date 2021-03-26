package com.github.forrestdp.healbeapp.ui.post

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.util.ShrinkedWallPost
import java.lang.IllegalArgumentException

class PostViewModelFactory(
    private val wallPost: ShrinkedWallPost,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(wallPost, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}