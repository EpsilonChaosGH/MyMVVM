package foundation

import android.app.Application
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import foundation.navigator.IntermediateNavigator
import foundation.utils.Event
import foundation.utils.ResourceActions
import foundation.navigator.Navigator
import foundation.uiaction.UiActions
import foundation.views.BaseScreen
import foundation.views.LiveEvent
import foundation.views.MutableLiveEvent

const val ARG_SCREEN = "ARG_SCREEN"

/**
 * Implementation of [Navigator] and [UiActions].
 * It is based on activity view-model because instances of [Navigator] and [UiActions]
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 *
 * This view-model extends [AndroidViewModel] which means that it is not "usual" view-model and
 * it may contain android dependencies (context, bundles, etc.).
 */
class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(), Navigator by navigator, UiActions by uiActions {


    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
}