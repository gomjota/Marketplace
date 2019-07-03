package com.juangomez.remote

import com.juangomez.data.entities.ProductEntity
import com.juangomez.remote.entities.RemoteProductEntity
import com.juangomez.remote.mappers.toEntity
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `should have the same attributes`() {
        val remoteProductEntity = RemoteProductEntity("VOUCHER", "Cabify Voucher", 5f)
        val productEntity = ProductEntity("VOUCHER", "Cabify Voucher", 5f)

        assert(remoteProductEntity.toEntity() == productEntity)
    }

    @Test
    fun `should have different attributes`() {
        val remoteProductEntity = RemoteProductEntity("VOUCHER", "Cabify Voucher", 5f)
        val productEntity = ProductEntity("VOUCHERs", "Cabify Vouchers", 5f)

        assert(remoteProductEntity.toEntity() != productEntity)
    }
}