package com.example.winkcart_user.data.repository


import app.cash.turbine.test
import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.coupons.discount.DiscountCode
import com.example.winkcart_user.data.model.coupons.discount.DiscountCodesResponse
import com.example.winkcart_user.data.model.coupons.pricerule.PrerequisiteSubtotalRange
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRule
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.coupons.pricerule.ToEntitlementPurchase
import com.example.winkcart_user.data.model.coupons.pricerule.ToEntitlementQuantityRatio
import com.example.winkcart_user.data.model.draftorder.cart.Customer
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.data.model.vendors.Image
import com.example.winkcart_user.data.model.vendors.SmartCollection
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
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

    private fun createFakeCustomerAddressesResponse(): CustomerAddressesResponse {
        return CustomerAddressesResponse(
            addresses = listOf(
                createFakeCustomerAddress(id = 1),
                createFakeCustomerAddress(id = 2, title = "Work", city = "Alexandria", isDefault = false)
            )
        )
    }

    private fun createFakeCustomerAddress(
        id: Long = 1L,
        title: String = "Home",
        country: String = "Egypt",
        city: String = "Cairo",
        address: String = "123 Nile Street",
        name: String = "Rofaida Sobhy",
        phone: String = "01234567890",
        isDefault: Boolean = true
    ): CustomerAddress {
        return CustomerAddress(
            id = id,
            title = title,
            country = country,
            city = city,
            address = address,
            name = name,
            phone = phone,
            default = isDefault
        )
    }

    private fun createFakeCustomerAddressRequest(): CustomerAddressRequest {
        return CustomerAddressRequest(
            CustomerAddress(
                title = "Home",
                country = "Egypt",
                city = "Cairo",
                address = "123 Nile Street",
                name = "Rofaida Sobhy",
                phone = "01234567890"
            )
        )
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

  /*  @Test
    fun `getUserOrders returns orders successfully`() = runTest {
        val ordersResponse = FakeOrderFactory.createFakeOrdersResponse()
        coEvery { localDataSource.readCustomersID() } returns "123"
        coEvery { productRepo.getUserOrders() } returns flowOf(ordersResponse)

        val result = productRepo.getUserOrders().first()

        assertNotNull(result)
        assertEquals(1, result?.orders?.size)
        assertEquals("MockVendor", result?.orders?.first()?.lineItems?.first()?.vendor)
    }*/
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

    @Test
    fun `createDraftOrder returns successfully`() = runTest {
        val draft = DraftOrder(
            id = 1,
            line_items = listOf(
                LineItemDraft(
                    product_id = 1, quantity = 1, title = "Item", price = "10.0",
                    variant_id = 1,
                    properties = listOf()
                )
            ),
            customer = Customer(id = 123L)
        )
        val draftRequest = DraftOrderRequest(draft_order = draft)
        val mockResponse = Any()

        coEvery { remoteDataSource.createDraftOrder(draftRequest) } returns flowOf(mockResponse)

        val result = productRepo.createDraftOrder(draftRequest).first()

        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun `getAllDraftOrders returns response successfully`() = runTest {
        val draftOrderResponse = DraftOrderResponse(draft_orders = listOf())
        coEvery { remoteDataSource.getAllDraftOrders() } returns flowOf(draftOrderResponse)

        val result = productRepo.getAllDraftOrders().first()

        assertNotNull(result)
        assertEquals(draftOrderResponse, result)
    }

    @Test
    fun `deleteDraftOrder deletes successfully`() = runTest {
        coEvery { remoteDataSource.deleteDraftOrder(10L) } returns flowOf(Unit)

        val result = productRepo.deleteDraftOrder(10L).first()

        assertEquals(Unit, result)
    }

    @Test
    fun `updateDraftOrder updates and returns response`() = runTest {
        val draft = DraftOrder(
            id = 1,
            line_items = listOf(LineItemDraft(
                product_id = 2, quantity = 2, title = "Updated Item", price = "20.0",
                variant_id = 2,
                properties = listOf()
            )),
            customer = Customer(id = 456L)
        )
        val request = DraftOrderRequest(draft_order = draft)
        val response = DraftOrderResponse(draft_orders = listOf())

        coEvery { remoteDataSource.updateDraftOrder(5L, request) } returns flowOf(response)

        val result = productRepo.updateDraftOrder(5L, request).first()

        assertNotNull(result)
        assertEquals(response, result)
    }


    @Test
    fun `getDiscountCodesByPriceRule returns response successfully`() = runTest {
        val discountCode = DiscountCode(
            id = 1L,
            price_rule_id = 10L,
            code = "SUMMER20",
            usage_count = 0,
            created_at = "2024-06-01T00:00:00Z",
            updated_at = "2024-06-02T00:00:00Z"
        )
        val response = DiscountCodesResponse(discount_codes = listOf(discountCode))

        coEvery { remoteDataSource.getDiscountCodesByPriceRule(10L) } returns flowOf(response)

        val result = productRepo.getDiscountCodesByPriceRule(10L).first()

        assertNotNull(result)
        assertEquals(1, result?.discount_codes?.size)
        assertEquals("SUMMER20", result?.discount_codes?.first()?.code)
        assertEquals(10L, result?.discount_codes?.first()?.price_rule_id)
    }

    @Test
    fun `getPriceRules returns response successfully`() = runTest {
        val rule = PriceRule(
            id = 10L,
            title = "Summer Sale",
            value_type = "percentage",
            value = "-20.0",
            customer_selection = "all",
            target_type = "line_item",
            target_selection = "all",
            allocation_method = "across",
            starts_at = "2024-06-01T00:00:00Z",
            ends_at = "2024-08-01T00:00:00Z",
            allocation_limit = 100,
            once_per_customer = true,
            usage_limit = 500,
            created_at = "2024-05-20T10:00:00Z",
            updated_at = "2024-05-21T10:00:00Z",
            entitled_product_ids = listOf(1L, 2L),
            entitled_variant_ids = listOf(101L),
            entitled_collection_ids = listOf(201L),
            entitled_country_ids = listOf(),
            prerequisite_product_ids = listOf(),
            prerequisite_variant_ids = listOf(),
            prerequisite_collection_ids = listOf(),
            customer_segment_prerequisite_ids = listOf(),
            prerequisite_customer_ids = listOf(),
            prerequisite_subtotal_range = PrerequisiteSubtotalRange("150.0"),
            prerequisite_quantity_range = null,
            prerequisite_shipping_price_range = null,
            prerequisite_to_entitlement_quantity_ratio = ToEntitlementQuantityRatio(1, 1),
            prerequisite_to_entitlement_purchase = ToEntitlementPurchase(1),
            admin_graphql_api_id = "gid://shopify/PriceRule/10"
        )

        val response = PriceRulesResponse(price_rules = listOf(rule))

        coEvery { remoteDataSource.getPriceRules() } returns flowOf(response)

        val result = productRepo.getPriceRules().first()

        assertNotNull(result)
        assertEquals(1, result?.price_rules?.size)
        assertEquals("Summer Sale", result?.price_rules?.first()?.title)
    }



    @Test
    fun `addCustomerAddress should return expected flow`() = runTest {
        val customerId = 1L
        val request = createFakeCustomerAddressRequest()
        val expected = createFakeCustomerAddressRequest()


        coEvery {
            remoteDataSource.addCustomerAddress(customerId, request)
        } returns flowOf(expected)

        productRepo.addCustomerAddress(customerId, request).test {
            val result = awaitItem()
            assertEquals(expected, result)
            awaitComplete()
        }
    }

    @Test
    fun `getCustomerAddresses returns customer addresses`() = runTest {
        val customerId = 1L
        val expected = createFakeCustomerAddressesResponse()

        coEvery {
            remoteDataSource.getCustomerAddresses(customerId)
        } returns flowOf(expected)

        productRepo.getCustomerAddresses(customerId).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `setDefaultAddress returns Unit`() = runTest {
        val customerId = 1L
        val addressId = 10L

        coEvery {
            remoteDataSource.setDefaultAddress(customerId, addressId)
        } returns flowOf(Unit)

        productRepo.setDefaultAddress(customerId, addressId).test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCustomerAddress returns CustomerAddressRequest`() = runTest {
        val customerId = 1L
        val addressId = 10L
        val expected = createFakeCustomerAddressRequest() // sample request data

        coEvery {
            remoteDataSource.getCustomerAddress(customerId, addressId)
        } returns flowOf(expected)

        productRepo.getCustomerAddress(customerId, addressId).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `deleteCustomerAddress returns Unit`() = runTest {
        val customerId = 1L
        val addressId = 10L

        coEvery {
            remoteDataSource.deleteCustomerAddress(customerId, addressId)
        } returns flowOf(Unit)

        productRepo.deleteCustomerAddress(customerId, addressId).test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `updateCustomerAddress returns response`() = runTest {
        val customerId = 1L
        val addressId = 10L
        val request = createFakeCustomerAddressRequest() // sample
        val expected = Any()

        coEvery {
            remoteDataSource.updateCustomerAddress(customerId, addressId, request)
        } returns flowOf(expected)

        productRepo.updateCustomerAddress(customerId, addressId, request).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }


}