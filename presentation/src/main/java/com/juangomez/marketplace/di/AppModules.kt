package com.juangomez.marketplace.di

import androidx.room.Room
import com.juangomez.data.executor.JobExecutor
import com.juangomez.data.repositories.CartRepositoryImpl
import com.juangomez.data.repositories.ProductRepositoryImpl
import com.juangomez.database.MarketplaceDatabase
import com.juangomez.database.sources.DatabaseCartSourceImpl
import com.juangomez.database.sources.DatabaseProductsSourceImpl
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.marketplace.BuildConfig.BASE_URL
import com.juangomez.marketplace.BuildConfig.DEBUG
import com.juangomez.marketplace.common.UiThread
import com.juangomez.marketplace.viewmodels.ProductsViewModel
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.services.createNetworkClient
import com.juangomez.remote.sources.RemoteProductsSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
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
        RemoteProductsSourceImpl(get(API))
    }
}

val databaseModules = module {
    single(name = DATABASE) {
        Room.databaseBuilder(
            androidApplication(),
            MarketplaceDatabase::class.java,
            "marketplace"
        ).build()
    }

    single(name = DATABASE_CART_SOURCE) {
        DatabaseCartSourceImpl(get(DATABASE))
    }

    single(name = DATABASE_PRODUCT_SOURCE) {
        DatabaseProductsSourceImpl(get(DATABASE))
    }
}

val dataModules = module {
    single(DATA_CART_REPOSITORY) {
        CartRepositoryImpl(get(DATABASE_CART_SOURCE))
    }

    single(DATA_PRODUCT_REPOSITORY) {
        ProductRepositoryImpl(get(DATABASE_PRODUCT_SOURCE), get(REMOTE_PRODUCT_SOURCE))
    }

    single(JOB_EXECUTOR) {
        JobExecutor()
    }
}

val domainModules = module {
    single(UI_THREAD) {
        UiThread()
    }

    factory(ADD_PRODUCT_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(CREATE_CHECKOUT_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(DELETE_PRODUCT_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(GET_CART_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }

    factory(GET_PRODUCTS_USE_CASE) {
        AddProductUseCase(get(DATA_CART_REPOSITORY), get(JOB_EXECUTOR), get(UI_THREAD))
    }
}

val presentationModules = module {
    viewModel {
        ProductsViewModel(get(GET_PRODUCTS_USE_CASE))
    }
}