package foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.example.mymvvm.views.FragmentHolder
import foundation.model.ErrorResult
import foundation.model.PendingResult
import foundation.model.Result
import foundation.model.SuccessResult
import java.lang.Exception

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */
    fun notifyScreenUpdates() {
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()
    }

    /**
     * Hide all views in the [root] and then call one of the provided lambda functions
     * depending on [result]:
     * - [onPending] is called when [result] is [PendingResult]
     * - [onSuccess] is called when [result] is [SuccessResult]
     * - [onError] is called when [result] is [ErrorResult]
     */
    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }

    }
}