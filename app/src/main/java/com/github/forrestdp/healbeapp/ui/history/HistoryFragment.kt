package com.github.forrestdp.healbeapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentHistoryBinding
import com.github.forrestdp.healbeapp.model.database.SportBeDatabase
import com.github.forrestdp.healbeapp.model.database.entities.Workout

class HistoryFragment : Fragment() {

    private val navArgs by navArgs<HistoryFragmentArgs>()

    private val viewModel: HistoryViewModel by navGraphViewModels(R.id.workout_in_progress_navigation) {
        HistoryViewModelFactory(
            SportBeDatabase
                .getInstance(requireActivity().application)
                .sportBeDatabaseDao,
            navArgs.justFinishedWorkoutId,
            requireActivity().application,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHistoryBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.viewModel = viewModel

        viewModel.workoutLineChartData.observe(viewLifecycleOwner) {
            with(binding.workoutLineChart) {
                data = it
                invalidate()
            }
        }

        viewModel.workoutPieChartData.observe(viewLifecycleOwner) {
            with(binding.workoutPieChart) {
                data = it
                invalidate()
                setUsePercentValues(true)
            }
        }

        viewModel.isNoDataAvailable.observe(viewLifecycleOwner) { isNoDataAvailable: Boolean? ->
            when (isNoDataAvailable) {
                true -> {
                    binding.historyLinearLayout.children.forEach {
                        it.visibility = INVISIBLE

                    }
                    binding.workoutHistoryPulseDynamic.visibility = VISIBLE
                    binding.workoutHistoryPulseDynamic.text = "Во время тренировки статистика недоступна"
                }
                false -> {
                    binding.historyLinearLayout.children.forEach {
                        it.visibility = VISIBLE
                    }
                    binding.workoutHistoryPulseDynamic.text = "Динамика пульса"
                }
            }
        }

        return binding.root
    }

}