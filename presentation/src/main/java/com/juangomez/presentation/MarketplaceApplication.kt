package com.juangomez.presentation

import android.app.Application
import com.juangomez.presentation.di.*
import org.koin.android.ext.android.startKoin

class MarketplaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        loadKoin()
    }

    private fun loadKoin() {
        startKoin(
            this,
            listOf(
                remoteModules,
                databaseModules,
                dataModules,
                domainModules,
                presentationModules
            )

        )
    }
}