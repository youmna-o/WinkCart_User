package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductImage
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.Image
import com.example.winkcart_user.data.model.vendors.SmartCollection
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository : ProductRepo {

    private val fakeSmartCollections = listOf(
        SmartCollection(title = "Nike", image = Image(src = "url1")),
        SmartCollection(title = "Adidas", image = Image(src = "url2"))
    )

    private val fakeProducts = listOf(
        createFakeProduct(
            id = 1111111111111,
            title = "Nike Air Max",
            handle = "nike-air-max",
            imageSrc = "https://fake.com/nike.jp"
        ),
        createFakeProduct(
            id = 2222222222222,
            title = "Adidas UltraBoos",
            handle = "adidas-ultra",
            imageSrc = "https://fake.com/adidas.jpg"
        ),
        createFakeProduct(
            id = 3333333333333,
            title = "Sneakers",
            handle = "Sneakers",
            imageSrc = "https://fake.com/Sneakers.jp"
        )
    )

    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        return flow {
            emit(SmartCollectionsResponse(smart_collections = fakeSmartCollections))
        }
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        return flow {
            emit(ProductResponse(products = fakeProducts))
        }
    }

    private fun createFakeProduct(
        id: Long,
        title: String,
        handle: String,
        imageSrc: String
    ): Product {
        return Product(
            id = id,
            title = title,
            handle = handle,
            body_html = "description",
            vendor = "brand",
            product_type = "type",
            created_at = "2025-01-01",
            updated_at = "2025-01-02",
            published_at = "2025-01-03",
            template_suffix = null,
            published_scope = "web",
            tags = "tag1,tag2",
            status = "active",
            admin_graphql_api_id = "gid://shopify/Product/$id",
            variants = emptyList(),
            options = emptyList(),
            images = listOf(
                ProductImage(
                    src = imageSrc,
                    id = id,
                    alt = "",
                    position = 1,
                    product_id = id,
                    created_at = "",
                    updated_at = "2025-01-01",
                    admin_graphql_api_id = "",
                    width = 1,
                    height = 1,
                    variant_ids = listOf(id)
                )
            ),
            image = ProductImage(
                src = imageSrc,
                id = id,
                alt = "",
                position = 1,
                product_id = id,
                created_at = "",
                updated_at = "2025-01-01",
                admin_graphql_api_id = "",
                width = 1,
                height = 1,
                variant_ids = listOf(id)
            )
        )
    }
}