package com.juangomez.marketplace

import android.app.Application
import com.juangomez.marketplace.di.*
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