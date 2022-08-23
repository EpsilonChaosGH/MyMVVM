package com.example.mymvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.mymvvm.databinding.FragmentCurrentColorBinding
import com.example.mymvvm.databinding.PartResultBinding
import com.example.mymvvm.views.onTryAgain
import com.example.mymvvm.views.renderSimpleResult
import foundation.model.ErrorResult
import foundation.model.PendingResult
import foundation.model.SuccessResult
import foundation.views.BaseFragment
import foundation.views.BaseScreen
import foundation.views.screenViewModel

class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) { result ->

            renderSimpleResult(root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                })
        }
        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        onTryAgain(binding.root){
            viewModel.tryAgain()
        }
        return binding.root
    }


}

