package com.github.forrestdp.healbeapp.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private val feedViewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val root = inflater.inflate(R.layout.fragment_feed, container, false)
//        val binding = DataBindingUtil.inflate<FragmentFeedBinding>(
//            layoutInflater,
//            R.layout.fragment_feed,
//            container,
//            false
//        )

        feedViewModel.titleImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_feed_title)!!
        val binding = FragmentFeedBinding.inflate(layoutInflater)


        binding.lifecycleOwner = this
        binding.feedViewModel = feedViewModel

        binding.photosList.adapter = PhotoListAdapter(PhotoListAdapter.OnClickListener {
            feedViewModel.displayPostDetails(it)
        })

        feedViewModel.navigateToSelectedPost.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(FeedFragmentDirections.actionNavigationFeedToNavigationPost(it))
                feedViewModel.displayPostDetailsComplete()
            }
        }

        binding.feedToolbar.elseToolbarUpArrow.setOnClickListener {
            it.findNavController().navigateUp()
        }

        return binding.root
    }
}