package com.github.forrestdp.healbeapp.ui.workout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.NonNull
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
    private lateinit var modesRadioGroupList: List<RadioButton>
    private lateinit var purposesRadioGroupList: List<RadioButton>

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

        modesRadioGroupList = listOf(
            binding.radioRunning,
            binding.radioCycling,
            binding.radioFitness,
            binding.radioYoga,
        )

        purposesRadioGroupList = listOf(
            binding.radioFit,
            binding.radioFatburn,
            binding.radioStamina,
        )

        initializeModeRadioGroupBindings(binding, modesRadioGroupList)
        initializePurposeRadioGroupBindings(binding, purposesRadioGroupList)

        workoutViewModel.startWorkoutInProgressActivity.observe(viewLifecycleOwner) { workoutSettings ->
            if (workoutSettings != null) {
                val intent = Intent(requireActivity(), WorkoutInProgressActivity::class.java).apply {
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

    private fun initializePurposeRadioGroupBindings(binding: FragmentWorkoutBinding, list: List<RadioButton>) {
        binding.radioFit.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutPurpose(WorkoutPurpose.BE_FIT)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }

        binding.radioFatburn.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutPurpose(WorkoutPurpose.FAT_BURNING)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }

        binding.radioStamina.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutPurpose(WorkoutPurpose.STAMINA_DEVELOPMENT)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }
    }

    private fun initializeModeRadioGroupBindings(binding: FragmentWorkoutBinding, list: List<RadioButton>) {
        binding.radioRunning.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutMode(WorkoutMode.RUNNING)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }

        binding.radioCycling.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutMode(WorkoutMode.CYCLING)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }

        binding.radioFitness.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutMode(WorkoutMode.FITNESS)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }

        binding.radioYoga.setOnCheckedChangeListener { radioView, isChecked ->
            if (isChecked) {
                workoutViewModel.setWorkoutMode(WorkoutMode.YOGA)
                list.filter { it != radioView }.forEach { it.isChecked = false }
            }
        }
    }
}