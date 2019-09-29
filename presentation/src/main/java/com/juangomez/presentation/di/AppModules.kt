package com.juangomez.presentation.di

import androidx.room.Room
import com.juangomez.data.repositories.CartRepositoryImpl
import com.juangomez.data.repositories.ProductRepositoryImpl
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.persistence.MarketplaceDatabase
import com.juangomez.persistence.sources.DatabaseCartSourceImpl
import com.juangomez.domain.interactors.*
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.presentation.BuildConfig.BASE_URL
import com.juangomez.presentation.BuildConfig.DEBUG
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import com.juangomez.presentation.viewmodels.ProductsViewModel
import com.juangomez.presentation.viewmodels.SplashViewModel
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.services.createNetworkClient
import com.juangomez.remote.sources.RemoteProductsSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit


val remoteModules = module {
    single { createNetworkClient(BASE_URL, DEBUG) }

    single {
        (get() as Retrofit).create(RemoteProductsApi::class.java)
    }

    single {
        RemoteProductsSourceImpl(get()) as RemoteProductsSource
    }
}

val databaseModules = module {
    single  {
        Room.databaseBuilder(
            androidApplication(),
            MarketplaceDatabase::class.java,
            "marketplace"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single {
        DatabaseCartSourceImpl(get()) as DatabaseCartSource
    }
}

val dataModules = module {
    single {
        CartRepositoryImpl(get()) as CartRepository
    }

    single {
        ProductRepositoryImpl(get()) as ProductRepository
    }
}

val domainModules = module {
    single {
        TwoForOneOffer()
    }

    single {
        BulkOffer()
    }

    factory {
        AddProductUseCase(get())
    }

    factory {
        CreateCheckoutUseCase(
            get(),
            get(),
            get()
        )
    }

    factory {
        DeleteProductUseCase(get())
    }

    factory {
        GetCartUseCase(get())
    }

    factory {
        DeleteCartUseCase(get())
    }

    factory {
        GetProductsUseCase(get())
    }
}

val presentationModules = module {
    viewModel {
        SplashViewModel()
    }

    viewModel {
        ProductsViewModel(get(), get(), get())
    }

    viewModel {
        CheckoutViewModel(get(), get(), get(), get())
    }
}