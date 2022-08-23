package foundation.views

import foundation.ActivityScopeViewModel


interface FragmentsHolder {

    /**
     * Called when activity views should be re-drawn.
     */
    fun notifyScreenUpdates()

    /**
     * Get the current implementations of dependencies from activity VM scope.
     */
    fun getActivityScopeViewModel(): ActivityScopeViewModel

}