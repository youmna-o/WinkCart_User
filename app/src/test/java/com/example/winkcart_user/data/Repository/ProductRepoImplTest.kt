package com.example.winkcart_user.data.Repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
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

    @Test
    fun `getProductsByVendor should filter duplicate product titles`() = runTest {
        val vendor = "MockVendor"
        val fakeProducts = listOf(
            createMockProduct(id = 1, title = "Sneakers", price = 50.0),
            createMockProduct(id = 2, title = "Sneakers", price = 55.0),
            createMockProduct(id = 3, title = "Jacket", price = 80.0,vendor="Adidas")
        )
        val response = ProductResponse(products = fakeProducts)
        coEvery { remoteDataSource.getProductsByVendor(vendor) } returns flowOf(response)

        val result = productRepo.getProductsByVendor(vendor).first()

        assertNotNull(result)
        assertEquals(1, result?.products?.count { it.title == "Sneakers" })
        assertEquals(2, result?.products?.size)
        assertEquals("Jacket", result?.products?.get(1)?.title)
    }

    @Test
    fun `getUserOrders returns orders successfully`() = runTest {
        val ordersResponse = FakeOrderFactory.createFakeOrdersResponse()
        coEvery { localDataSource.readCustomersID() } returns "123"
        coEvery { productRepo.getUserOrders() } returns flowOf(ordersResponse)

        val result = productRepo.getUserOrders().first()

        assertNotNull(result)
        assertEquals(1, result?.orders?.size)
        assertEquals("MockVendor", result?.orders?.first()?.lineItems?.first()?.vendor)
    }
    @Test
    fun `getSpecificOrderDetails returns correct order details`() = runTest {

        val fakeOrder = FakeOrderFactory.createFakeOrdersResponse().orders.last()
        val orderDetailsResponse = OrderDetailsResponse(order = fakeOrder)

        coEvery { remoteDataSource.getSpecificOrderDEtails(999L) } returns flowOf(orderDetailsResponse)


        val result = productRepo.getSpecificOrderDetails(999L).first()

        assertNotNull(result)
        assertEquals(fakeOrder.id, result?.order?.id)
        assertEquals(fakeOrder.lineItems.first().vendor, result?.order?.lineItems?.first()?.vendor)
    }

    @Test
    fun `createOrder returns order successfully`() = runTest {

        val fakeOrderRequest = createFakeOrderRequest()
        val fakeOrdersResponse = FakeOrderFactory.createFakeOrdersResponse()

        coEvery { remoteDataSource.createOrder(fakeOrderRequest) } returns flowOf(fakeOrdersResponse)
        val result = productRepo.createOrder(fakeOrderRequest).first()
        assertNotNull(result)
        assertEquals(fakeOrdersResponse.orders.first().id, result?.orders?.first()?.id)
        assertEquals(fakeOrdersResponse.orders.first().lineItems.first().title, result?.orders?.first()?.lineItems?.first()?.title)
    }


}


