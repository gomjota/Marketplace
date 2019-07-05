package com.juangomez.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juangomez.database.dao.ProductDao
import com.juangomez.database.entities.DatabaseProductEntity
import org.junit.After
import org.junit.Assert.assertTrue
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
                code = "VOUCHER",
                name = "Cabify Voucher",
                price = 5f
            ),
            DatabaseProductEntity(
                code = "TSHIRT",
                name = "Cabify T-Shirt",
                price = 10f
            ),
            DatabaseProductEntity(
                code = "Mug",
                name = "Cabify Mug",
                price = 15f
            )
        )

        productDao.insertAll(productsToSave)
        productDao.getAll()
            .test()
            .assertNoErrors()
            .assertValue { productsToSave.size == it.size }
    }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteAllProducts() {
        val productsToSave = listOf(
            DatabaseProductEntity(
                code = "VOUCHER",
                name = "Cabify Voucher",
                price = 5f
            ),
            DatabaseProductEntity(
                code = "TSHIRT",
                name = "Cabify T-Shirt",
                price = 20f
            ),
            DatabaseProductEntity(
                code = "MUG",
                name = "Cabify Coffee Mug",
                price = 7.5f
            )
        )

        productDao.insertAll(productsToSave)
        productDao.deleteAll()
        productDao.getAll()
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