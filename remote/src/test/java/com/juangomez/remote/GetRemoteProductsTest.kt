package com.juangomez.remote

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.sources.RemoteProductsSourceImpl
import com.juangomez.remote.services.createNetworkClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class GetRemoteProductsTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteProductsSource: RemoteProductsSource

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val client = createNetworkClient(mockWebServer.url("/").toString(), true)
        val service = client.create(RemoteProductsApi::class.java)
        remoteProductsSource = RemoteProductsSourceImpl(service)
    }

    @Test
    fun `should get three different products`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    "{\"products\":[{\"code\":\"VOUCHER\",\"name\":\"Cabify Voucher\"," +
                            "\"price\":5},{\"code\":\"TSHIRT\",\"name\":\"Cabify T-Shirt\"," +
                            "\"price\":20},{\"code\":\"MUG\",\"name\":\"Cabify Coffee Mug\"," +
                            "\"price\":7.5}]}"
                )
        )

        val productsExpected = listOf(
            ProductEntity("VOUCHER", "Cabify Voucher", 5f),
            ProductEntity("TSHIRT", "Cabify T-Shirt", 20f),
            ProductEntity("MUG", "Cabify Coffee Mug", 7.5f)
        )

        remoteProductsSource.getProducts()
            .test()
            .assertNoErrors()
            .assertValue { it == productsExpected }
            .assertComplete()
    }


    @Test
    fun `should get a server error`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
        )

        remoteProductsSource.getProducts()
            .test()
            .assertError { (it as? HttpException)?.code() == 500 }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}