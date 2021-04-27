package com.github.forrestdp.healbeapp.ui.workout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.WorkoutInProgressActivity
import com.github.forrestdp.healbeapp.databinding.FragmentWorkoutBinding
import com.github.forrestdp.healbeapp.model.WorkoutMode
import com.github.forrestdp.healbeapp.model.WorkoutPurpose

const val WORKOUT_IN_PROGRESS_EXTRA = "com.github.forrestdp.healbeapp.WorkoutInProgressActivity"

class WorkoutFragment : Fragment() {

    private val workoutViewModel by viewModels<WorkoutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        workoutViewModel.titleImage =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_workout_title)!!

        val binding = FragmentWorkoutBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.viewModel = workoutViewModel

        binding.workoutToolbar.elseToolbarUpArrow.setOnClickListener {
            it.findNavController().navigateUp()
        }

        workoutViewModel.startWorkoutInProgressActivity.observe(viewLifecycleOwner) { workoutSettings ->
            if (workoutSettings != null) {
                val intent =
                    Intent(requireActivity(), WorkoutInProgressActivity::class.java).apply {
                        putExtra(WORKOUT_IN_PROGRESS_EXTRA, workoutSettings)
                    }
                startActivity(intent)
                workoutViewModel.startWorkoutComplete()
            }
        }

        binding.buttonStart.setOnClickListener {
            workoutViewModel.startWorkout()
        }



        return binding.root
    }
}