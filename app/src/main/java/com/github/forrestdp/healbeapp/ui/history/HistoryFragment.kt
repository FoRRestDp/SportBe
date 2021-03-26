package com.github.forrestdp.healbeapp.ui.history

import android.opengl.Visibility
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
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentHistoryBinding
import com.github.forrestdp.healbeapp.model.database.TimeSeriesDatabase
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundaries

class HistoryFragment : Fragment() {

    private val args: HistoryFragmentArgs by navArgs()

    private val viewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory(
            TimeSeriesDatabase
                .getInstance(requireActivity().application)
                .timeSeriesDao,
            requireActivity().application,
            WorkoutTimeBoundaries(startTimestamp = args.startTimestamp, endTimestamp =  args.endTimestamp),
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

        viewModel.isDataAvailable.observe(viewLifecycleOwner) { isDataAvailable: Boolean? ->
            if (isDataAvailable != true) {
                binding.historyLinearLayout.children.forEach {
                        it.visibility = INVISIBLE

                }
                binding.workoutHistoryPulseDynamic.visibility = VISIBLE
                binding.workoutHistoryPulseDynamic.text = "Во время тренировки статистика недоступна"
            } else {
                binding.historyLinearLayout.children.forEach {
                    it.visibility = VISIBLE
                }
                binding.workoutHistoryPulseDynamic.text = "Динамика пульса"
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

}