package com.example.mymvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymvvm.R
import com.example.mymvvm.databinding.FragmentChangeColorBinding
import com.example.mymvvm.views.onTryAgain
import com.example.mymvvm.views.renderSimpleResult
import foundation.views.HasScreenTitle
import foundation.views.BaseFragment
import foundation.views.BaseScreen
import foundation.views.screenViewModel

/**
 * Screen for changing color.
 * 1) Displays the list of available colors
 * 2) Allows choosing the desired color
 * 3) Chosen color is saved only after pressing "Save" button
 * 4) The current choice is saved via [SavedStateHandle] (see [ChangeColorViewModel])
 */
class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    /**
     * This screen has 1 argument: color ID to be displayed as selected.
     */
    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * Example of dynamic screen title
     */
    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.colorsList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result){
                adapter.items = it
            }
        }
        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // if screen title is changed -> need to notify activity about updates
            notifyScreenUpdates()
        }

        onTryAgain(binding.root){
            viewModel.tryAgain()
        }

        return binding.root
    }

    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {
        // waiting for list width
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })
    }
}