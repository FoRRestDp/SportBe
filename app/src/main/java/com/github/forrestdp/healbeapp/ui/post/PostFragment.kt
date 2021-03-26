package com.github.forrestdp.healbeapp.ui.post

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(layoutInflater)
        val application = requireNotNull(activity).application

        binding.lifecycleOwner = this

        val wallPost = PostFragmentArgs.fromBundle(requireArguments()).selectedPost
        val viewModelFactory = PostViewModelFactory(wallPost, application)

        binding.postViewModel =
            ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        return binding.root
    }

}