package com.github.forrestdp.healbeapp.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.forrestdp.healbeapp.databinding.FragmentProgressBinding

class ProgressFragment : Fragment() {

    private val viewModel: ProgressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProgressBinding.inflate(layoutInflater)

        viewModel.progressBarChartData.observe(viewLifecycleOwner) {
            binding.workoutProgressBarchart.data = it
            binding.workoutProgressBarchart.invalidate()
        }

        viewModel.progressLineChartData.observe(viewLifecycleOwner) {
            binding.workoutProgressLinechart.data = it
            binding.workoutProgressLinechart.invalidate()
        }

        return binding.root
    }

}