package com.juangomez.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juangomez.database.dao.CartDao
import com.juangomez.database.entities.DatabaseCartEntity
import com.juangomez.database.entities.DatabaseProductEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Rule
import org.junit.rules.TestRule


@RunWith(AndroidJUnit4::class)
class CartRoomTest {
    private lateinit var cartDao: CartDao
    private lateinit var database: MarketplaceDatabase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, MarketplaceDatabase::class.java
        ).build()
        cartDao = database.cartDao()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertCartWithThreeProducts() {
        val products = listOf(
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


        val cartToSave = DatabaseCartEntity(products = products)

        cartDao.insertCart(cartToSave)
            .test()
            .assertNoErrors()
            .assertTerminated()

        cartDao.getCart()
            .test()
            .assertNoErrors()
            .assertValueCount(1)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }
}