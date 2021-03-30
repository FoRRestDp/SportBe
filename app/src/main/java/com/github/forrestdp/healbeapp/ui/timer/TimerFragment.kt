package com.github.forrestdp.healbeapp.ui.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentTimerBinding
import com.github.forrestdp.healbeapp.model.database.SportBeDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.time.ExperimentalTime

class TimerFragment : Fragment() {

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    private val viewModel: TimerViewModel by navGraphViewModels(R.id.workout_in_progress_navigation) {
        TimerViewModelFactory(
            SportBeDatabase
                .getInstance(requireActivity().application)
                .sportBeDatabaseDao,
            requireActivity().application,
        )
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTimerBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.startOrResumeWorkout()

        binding.workoutStopButton.setOnClickListener {
            viewModel.stopWorkoutAndNavigateToHistory()
        }

        viewModel.navigateToHistoryFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(TimerFragmentDirections.actionNavigationTimerToNavigationHistory().apply {
                    justFinishedWorkoutId = it
                })
                viewModel.stopWorkoutComplete()
            }
        }

        return binding.root
    }
}