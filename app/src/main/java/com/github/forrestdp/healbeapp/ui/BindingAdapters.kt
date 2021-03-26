package com.github.forrestdp.healbeapp.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.forrestdp.healbeapp.ui.feed.PhotoListAdapter
import com.github.forrestdp.healbeapp.util.ShrinkedWallPost

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ShrinkedWallPost>?) {
    val adapter = recyclerView.adapter as PhotoListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val imageUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .into(imageView)
            // .apply(RequestOptions()
                // .placeholder()
                // .error()
    }
}