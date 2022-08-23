package com.example.mymvvm.views

import foundation.ActivityScopeViewModel

interface FragmentHolder {
    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel
}