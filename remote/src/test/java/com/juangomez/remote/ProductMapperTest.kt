package com.juangomez.remote

import com.juangomez.data.entities.ProductEntity
import com.juangomez.remote.entities.RemoteProductEntity
import com.juangomez.remote.mappers.toEntity
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `should have the same attributes`() {
        val remoteProductEntity = RemoteProductEntity("COPPER", "COPPER", 5f)
        val productEntity = ProductEntity("COPPER", "COPPER", 5f)

        assert(remoteProductEntity.toEntity() == productEntity)
    }

    @Test
    fun `should have different attributes`() {
        val remoteProductEntity = RemoteProductEntity("COPPER", "COPPER", 5f)
        val productEntity = ProductEntity("COPPERs", "COPPERs", 5f)

        assert(remoteProductEntity.toEntity() != productEntity)
    }
}