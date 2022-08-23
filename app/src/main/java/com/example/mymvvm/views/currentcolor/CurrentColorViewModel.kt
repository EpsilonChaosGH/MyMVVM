package com.example.mymvvm.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymvvm.R
import com.example.mymvvm.model.colors.ColorListener
import com.example.mymvvm.model.colors.ColorsRepository
import com.example.mymvvm.model.colors.NamedColor
import foundation.navigator.Navigator
import foundation.uiaction.UiActions
import foundation.views.BaseViewModel
import com.example.mymvvm.views.changecolor.ChangeColorFragment
import foundation.model.ErrorResult
import foundation.model.PendingResult
import foundation.model.SuccessResult
import foundation.model.takeSuccess
import foundation.views.LiveResult
import foundation.views.MutableLiveResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- example of listening results via model layer

    init {
        viewModelScope.launch {
            delay(2000)
            colorsRepository.addListener(colorListener)
        //colorsRepository.addListener(colorListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain(){
        viewModelScope.launch {
            _currentColor.postValue(PendingResult())
            delay(2000)
            colorsRepository.addListener(colorListener)
        }
    }

}