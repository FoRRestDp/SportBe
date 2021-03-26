package com.github.forrestdp.healbeapp.ui.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.forrestdp.healbeapp.R
import com.github.forrestdp.healbeapp.databinding.FragmentSupportBinding

class SupportFragment : Fragment() {

    private val viewModel: SupportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.titleImage =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_support_title)!!

        val binding = FragmentSupportBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.supportToolbar.elseToolbarUpArrow.setOnClickListener {
            it.findNavController().navigateUp()
        }
        return binding.root
    }

}