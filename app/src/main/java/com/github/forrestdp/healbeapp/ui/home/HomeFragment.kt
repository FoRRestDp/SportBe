package com.github.forrestdp.healbeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.forrestdp.healbeapp.databinding.FragmentHomeBinding

// const val REQUEST_ENABLE_BT = 32893

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // val root = inflater.inflate(R.layout.fragment_home, container, false)
        binding.buttonWorkout.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationWorkout())
        }
        binding.buttonFeed.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationFeed())
        }
        binding.buttonMeasurements.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationMeasurement())
        }
        binding.buttonSettings.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSettings())
        }
        binding.buttonMainapp.setOnClickListener {
            Toast.makeText(requireContext(), "Пока не поддерживается", Toast.LENGTH_SHORT).show()
        }
        binding.buttonSupport.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSupport())
        }
        return binding.root
    }
}