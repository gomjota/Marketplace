package com.juangomez.remote

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.services.createNetworkClient
import com.juangomez.remote.sources.RemoteProductsSourceImpl
import kotlinx.coroutines.runBlocking
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
                    "{\"products\":[{\"code\":\"COPPER\",\"name\":\"COPPER\"," +
                            "\"price\":5},{\"code\":\"COMMANDER2\",\"name\":\"T-Shirt\"," +
                            "\"price\":20},{\"code\":\"PULSAR\",\"name\":\"Coffee PULSAR\"," +
                            "\"price\":7.5}]}"
                )
        )

        val productsExpected = listOf(
            ProductEntity("COPPER", "COPPER", 5f),
            ProductEntity("COMMANDER2", "T-Shirt", 20f),
            ProductEntity("PULSAR", "Coffee PULSAR", 7.5f)
        )

        runBlocking {
            val remoteProducts = remoteProductsSource.getProducts()
            assert(remoteProducts == productsExpected)
        }

    }

    @Test(expected = HttpException::class)
    fun `should get a server error`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
        )

        runBlocking {
            remoteProductsSource.getProducts()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}