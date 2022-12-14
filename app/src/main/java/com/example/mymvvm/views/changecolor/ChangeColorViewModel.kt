package com.example.mymvvm.views.changecolor

import androidx.lifecycle.*
import com.example.mymvvm.R
import com.example.mymvvm.model.colors.ColorsRepository
import com.example.mymvvm.model.colors.NamedColor
import foundation.navigator.Navigator
import foundation.uiaction.UiActions
import foundation.views.BaseViewModel
import com.example.mymvvm.views.changecolor.ChangeColorFragment.Screen
import foundation.model.ErrorResult
import foundation.model.PendingResult
import foundation.model.SuccessResult
import foundation.views.LiveResult
import foundation.views.MediatorLiveResult
import foundation.views.MutableLiveResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)

    // main destination (contains merged values from _availableColors & _currentColorId)
    private val _colorsList = MediatorLiveResult<List<NamedColorListItem>>()
    val colorsList: LiveResult<List<NamedColorListItem>> = _colorsList

    // side destination, also the same result can be achieved by using Transformations.map() function.

    val screenTitle: LiveData<String> = Transformations.map(colorsList){ result ->
        if (result is SuccessResult){
            val currentColor = result.data.first{ it.selected}
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else{
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    init {
        viewModelScope.launch {
            delay(2000)
           // _availableColors.value = SuccessResult(colorsRepository.getAvailableColors())
            _availableColors.value = ErrorResult(RuntimeException())
        }
        // initializing MediatorLiveData
        _colorsList.addSource(_availableColors) { mergeSources() }
        _colorsList.addSource(_currentColorId) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorsRepository.getById(currentColorId)
        colorsRepository.currentColor = currentColor
        navigator.goBack(result = currentColor)
    }
    fun tryAgain(){
        viewModelScope.launch{
            _availableColors.postValue(PendingResult())
            delay(2000)
            _availableColors.postValue(SuccessResult(colorsRepository.getAvailableColors()))
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), then we use both of these values in order to create a list of
     * [NamedColorListItem], it is a list to be displayed in RecyclerView.
     */
    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return

        _colorsList.value = colors.map { colorsList ->
            colorsList.map { NamedColorListItem(it, currentColorId == it.id) }
        }
    }

}