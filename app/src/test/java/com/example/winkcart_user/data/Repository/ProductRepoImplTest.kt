package com.example.winkcart_user.data.Repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.proddata.Variant
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.Image
import com.example.winkcart_user.data.model.vendors.SmartCollection
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import com.example.winkcart_user.data.repository.ProductRepoImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test


@ExperimentalCoroutinesApi
class ProductRepoImplTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var productRepo: ProductRepoImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        remoteDataSource = mockk()
        localDataSource = mockk()
        productRepo = ProductRepoImpl(remoteDataSource, localDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllProducts filters duplicates and returns ProductResponse`() = runTest {
        val fakeProducts = listOf(
            createMockProduct(
                id = 1,
                title = "T-Shirt",
                price = 22.00
            ),
            createMockProduct(
                id = 1,
                title = "Shoes",
                price = 11.00
            ),
            createMockProduct(
                id = 3,
                title = "T-Shirt",
                price = 22.00
            ),
        )
        val response = ProductResponse(products = fakeProducts)
        coEvery { remoteDataSource.getAllProducts() } returns flowOf(response)
        val result = productRepo.getAllProducts().first()
        assertNotNull(result)
        assertEquals(2, result?.products?.size)
        assertTrue(result?.products?.none { it.title == "T-Shirt" && it.id.toInt() == 3 } == true)
    }

    @Test
    fun `getSmartCollections should return expected result`() = runTest {
        val expectedCollections = listOf(
            SmartCollection("Nike", Image("url1")),
            SmartCollection("Adidas", Image("url2"))
        )
        coEvery { remoteDataSource.getSmartCollections() } returns flowOf(
            SmartCollectionsResponse(expectedCollections)
        )
        val result = productRepo.getSmartCollections().first()
        assertEquals(expectedCollections, result?.smart_collections)
    }

    @Test
    fun `getFilteredSmartCollections should filter out null images and duplicates`() = runTest {

        val originalList = listOf(
            SmartCollection("Nike", Image("url1")),
            SmartCollection("Adidas", null),
            SmartCollection("Nike", Image("url1")),
            SmartCollection("Puma", Image("url3"))
        )
        val expectedList = listOf(
            SmartCollection("Nike", Image("url1")),
            SmartCollection("Puma", Image("url3"))
        )
        coEvery { remoteDataSource.getSmartCollections() } returns flowOf(
            SmartCollectionsResponse(originalList)
        )
        val result = productRepo.getFilteredSmartCollections().first()
        assertEquals(expectedList, result?.smart_collections)
    }


}
fun createMockProduct(id: Long, title: String, price: Double): Product {
    val variant = Variant(
        id = id + 1000,
        product_id = id,
        title = "OS / black / 10",
        price = price.toString(),
        position = 1,
        inventory_policy = "deny",
        compare_at_price = null,
        option1 = "OS",
        option2 = "black",
        option3 = "10",
        created_at = "2025-06-08T07:23:29+03:00",
        updated_at = "2025-06-08T07:23:29+03:00",
        taxable = true,
        barcode = null,
        fulfillment_service = "manual",
        grams = 0,
        inventory_management = "shopify",
        requires_shipping = true,
        sku = "SKU-$id",
        weight = 0.0,
        weight_unit = "kg",
        inventory_item_id = id + 2000,
        inventory_quantity = 30,
        old_inventory_quantity = 30,
        admin_graphql_api_id = "gid://shopify/ProductVariant/${id + 1000}",
        image_id = null
    )
    return Product(
        id = id,
        title = title,
        body_html = "",
        vendor = "MockVendor",
        product_type = "MockType",
        created_at = "2025-01-01T00:00:00Z",
        handle = "mock-handle",
        updated_at = "2025-01-01T00:00:00Z",
        published_at = "2025-01-01T00:00:00Z",
        template_suffix = null,
        published_scope = "global",
        tags = "",
        status = "active",
        admin_graphql_api_id = "mock-api-id",
        options = emptyList(),
        images = emptyList(),
        image = null,
        variants =  listOf(variant)
    )
}

