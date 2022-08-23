package com.example.mymvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.example.mymvvm.R
import com.example.mymvvm.databinding.PartResultBinding
import foundation.model.Result
import foundation.model.SuccessResult
import foundation.views.BaseFragment

fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit){
    val binding = PartResultBinding.bind(root)
    renderResult(

        root = root,
        result = result,
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        }

    )
}

fun BaseFragment.onTryAgain(root: View, onTryAgainPressed: () -> Unit){
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener { onTryAgainPressed() }
}