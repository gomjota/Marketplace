package com.juangomez.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juangomez.persistence.dao.ProductDao
import com.juangomez.persistence.entities.DatabaseProductEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProductsRoomTest {
    private lateinit var productDao: ProductDao
    private lateinit var database: MarketplaceDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, MarketplaceDatabase::class.java
        ).build()
        productDao = database.productsDao()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertTheSameAmountOfProducts() {
        val productsToSave = listOf(
            DatabaseProductEntity(
                code = "COPPER",
                name = "COPPER",
                price = 5f
            ),
            DatabaseProductEntity(
                code = "COMMANDER2",
                name = "COMMANDER",
                price = 10f
            ),
            DatabaseProductEntity(
                code = "PULSAR",
                name = "PULSAR",
                price = 15f
            )
        )

        productDao.insertAll(productsToSave)
            .andThen(productDao.getAll())
            .test()
            .assertNoErrors()
            .assertValue { productsToSave.size == it.size }


    }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteAllProducts() {
        val productsToSave = listOf(
            DatabaseProductEntity(
                code = "COPPER",
                name = "COPPER",
                price = 5f
            ),
            DatabaseProductEntity(
                code = "COMMANDER2",
                name = "COMMANDER",
                price = 20f
            ),
            DatabaseProductEntity(
                code = "PULSAR",
                name = "PULSAR",
                price = 7.5f
            )
        )

        productDao.insertAll(productsToSave)
            .andThen(productDao.deleteAll())
            .andThen(productDao.getAll())
            .test()
            .assertNoErrors()
            .assertValue { it.isEmpty() }
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }
}