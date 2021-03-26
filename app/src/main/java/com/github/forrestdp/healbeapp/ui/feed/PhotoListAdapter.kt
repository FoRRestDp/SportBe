package com.github.forrestdp.healbeapp.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.forrestdp.healbeapp.databinding.GridViewItemBinding
import com.github.forrestdp.healbeapp.util.ShrinkedWallPost

class PhotoListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<ShrinkedWallPost, PhotoListAdapter.WallPostViewHolder>(DiffCallback) {

    class WallPostViewHolder(private val binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallPost: ShrinkedWallPost) {
            binding.wallPost = wallPost
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShrinkedWallPost>() {
        override fun areItemsTheSame(
            oldItem: ShrinkedWallPost,
            newItem: ShrinkedWallPost
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ShrinkedWallPost,
            newItem: ShrinkedWallPost
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallPostViewHolder =
        WallPostViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: WallPostViewHolder, position: Int) {
        val wallPost = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(wallPost)
        }
        holder.bind(wallPost)
    }

    class OnClickListener(val clickListener: (wallPost: ShrinkedWallPost) -> Unit) {
        fun onClick(wallPost: ShrinkedWallPost) = clickListener(wallPost)
    }
}