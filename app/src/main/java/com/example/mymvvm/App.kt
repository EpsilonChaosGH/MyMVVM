package com.example.mymvvm

import android.app.Application
import com.example.mymvvm.model.colors.InMemoryColorsRepository
import foundation.BaseApplication
import foundation.model.Repository

/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories: List<Repository> = listOf<Repository>(
        InMemoryColorsRepository()
    )

}