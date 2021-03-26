package com.github.forrestdp.healbeapp.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShrinkedWallPost(
    val id: Int,
    val imageSrcUrl: String,
    val text: String,
) : Parcelable
