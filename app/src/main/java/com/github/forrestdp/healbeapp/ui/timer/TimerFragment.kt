package com.github.forrestdp.healbeapp.ui.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.healbeapp.databinding.FragmentTimerBinding
import com.github.forrestdp.healbeapp.model.database.TimeSeriesDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TimerFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel: TimerViewModel by viewModels {
        TimerViewModelFactory(
            TimeSeriesDatabase
                .getInstance(requireActivity().application)
                .timeSeriesDao,
            TimeSeriesDatabase
                .getInstance(requireActivity().application)
                .workoutBoundariesDao,
            requireActivity().application,
        )
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTimerBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.workoutStopButton.setOnClickListener {
            viewModel.stopWorkout()
        }

        viewModel.navigateToHistoryFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(
                    TimerFragmentDirections.actionNavigationTimerToNavigationHistory(
                    ).apply {
                        startTimestamp = it.startTimestamp
                        endTimestamp = it.endTimestamp
                    }
                )
                viewModel.stopWorkoutComplete()
            }
        }

        return binding.root
    }
}