package com.example.winkcart_user.viewMOdels

import com.example.winkcart_user.data.Repository.createMockProduct
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VendorProductsViewModelTest {

    private lateinit var repo: ProductRepo
    private lateinit var viewModel: VendorProductsViewModel

    private val fakeProducts = listOf(

        createMockProduct(1, "Shoes", 29.99).copy(vendor = "Nike"),
        createMockProduct(2, "Jacket", 59.99).copy(vendor = "Nike")
    )

    private val fakeResponse = ProductResponse(products = fakeProducts)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repo = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProductsPyVendor emits Success state with data`() = runTest {
        coEvery { repo.getProductsByVendor("Nike") } returns flowOf(fakeResponse)


        viewModel = VendorProductsViewModel(repo)
        viewModel.getProductsPyVendor("Nike")

        val result = viewModel.productByVendor.first { it !is ResponseStatus.Loading }
        assertTrue(result is ResponseStatus.Success)

        val products = (result as ResponseStatus.Success).result.products
        assertEquals(2, products.size)
        assertEquals("Shoes", products[0].title)
        assertEquals("Nike", products[0].vendor)
    }
}
