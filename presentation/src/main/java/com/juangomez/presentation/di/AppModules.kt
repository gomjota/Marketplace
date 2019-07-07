package com.juangomez.presentation.di

import androidx.room.Room
import com.juangomez.data.executor.JobExecutor
import com.juangomez.data.repositories.CartRepositoryImpl
import com.juangomez.data.repositories.ProductRepositoryImpl
import com.juangomez.data.sources.database.DatabaseCartSource
import com.juangomez.data.sources.database.DatabaseProductsSource
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.database.MarketplaceDatabase
import com.juangomez.database.sources.DatabaseCartSourceImpl
import com.juangomez.database.sources.DatabaseProductsSourceImpl
import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.interactors.*
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.presentation.BuildConfig.BASE_URL
import com.juangomez.presentation.BuildConfig.DEBUG
import com.juangomez.presentation.common.UiThread
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import com.juangomez.presentation.viewmodels.ProductsViewModel
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.services.createNetworkClient
import com.juangomez.remote.sources.RemoteProductsSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit

private val RETROFIT_INSTANCE = "retrofit_instance"
private val API = "api"
private val REMOTE_PRODUCT_SOURCE = "remote_product_source"

private val DATABASE = "database"
private val DATABASE_CART_SOURCE = "database_cart_source"
private val DATABASE_PRODUCT_SOURCE = "database_product_source"

private val DATA_CART_REPOSITORY = "data_cart_repository"
private val DATA_PRODUCT_REPOSITORY = "data_product_repository"
private val JOB_EXECUTOR = "job_executor"

private val TWO_FOR_ONE_OFFER = "two_for_one_offer"
private val BULK_OFFER = "bulk_offer"
private val ADD_PRODUCT_USE_CASE = "add_product_use_case"
private val CREATE_CHECKOUT_USE_CASE = "create_checkout_use_case"
private val DELETE_PRODUCT_USE_CASE = "delete_product_use_case"
private val GET_CART_USE_CASE = "get_cart_use_case"
private val GET_PRODUCTS_USE_CASE = "get_products_use_case"

private val UI_THREAD = "ui_thread"

val remoteModules = module {
    single(name = RETROFIT_INSTANCE) { createNetworkClient(BASE_URL, DEBUG) }

    single(name = API) {
        (get(RETROFIT_INSTANCE) as Retrofit).create(RemoteProductsApi::class.java)
    }

    single(name = REMOTE_PRODUCT_SOURCE) {
        RemoteProductsSourceImpl(get(API)) as RemoteProductsSource
    }
}

val databaseModules = module {
    single(name = DATABASE) {
        Room.databaseBuilder(
            androidApplication(),
            MarketplaceDatabase::class.java,
            "marketplace"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single(name = DATABASE_CART_SOURCE) {
        DatabaseCartSourceImpl(get(DATABASE)) as DatabaseCartSource
    }

    single(name = DATABASE_PRODUCT_SOURCE) {
        DatabaseProductsSourceImpl(get(DATABASE)) as DatabaseProductsSource
    }
}

val dataModules = module {
    single(DATA_CART_REPOSITORY) {
        CartRepositoryImpl(get(DATABASE_CART_SOURCE)) as CartRepository
    }

    single(DATA_PRODUCT_REPOSITORY) {
        ProductRepositoryImpl(get(REMOTE_PRODUCT_SOURCE), get(DATABASE_PRODUCT_SOURCE)) as ProductRepository
    }

    single(JOB_EXECUTOR) {
        JobExecutor() as ThreadExecutor
    }
}

val domainModules = module {
    single(UI_THREAD) {
        UiThread() as PostExecutionThread
    }

    single(TWO_FOR_ONE_OFFER) {
        TwoForOneOffer()
    }

    single(BULK_OFFER) {
        BulkOffer()
    }

    factory(ADD_PRODUCT_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(CREATE_CHECKOUT_USE_CASE) {
        CreateCheckoutUseCase(
            get(DATA_CART_REPOSITORY),
            get(TWO_FOR_ONE_OFFER),
            get(BULK_OFFER),
            get(JOB_EXECUTOR),
            get(UI_THREAD)
        )
    }

    factory(DELETE_PRODUCT_USE_CASE) {
        DeleteProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(GET_CART_USE_CASE) {
        GetCartUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(GET_PRODUCTS_USE_CASE) {
        GetProductsUseCase(get(DATA_PRODUCT_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }
}

val presentationModules = module {
    viewModel {
        ProductsViewModel(get(GET_PRODUCTS_USE_CASE), get(ADD_PRODUCT_USE_CASE), get(GET_CART_USE_CASE))
    }

    viewModel {
        CheckoutViewModel(get(ADD_PRODUCT_USE_CASE), get(DELETE_PRODUCT_USE_CASE), get(CREATE_CHECKOUT_USE_CASE))
    }
}