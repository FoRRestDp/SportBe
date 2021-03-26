package com.github.forrestdp.healbeapp.ui.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentMeasurementBinding

class MeasurementFragment : Fragment() {

    private val viewModel: MeasurementViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewModel.titleImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_measurements_title)!!
        val binding = FragmentMeasurementBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.measurementToolbar.elseToolbarUpArrow.setOnClickListener {
            it.findNavController().navigateUp()
        }

        return binding.root
    }

}