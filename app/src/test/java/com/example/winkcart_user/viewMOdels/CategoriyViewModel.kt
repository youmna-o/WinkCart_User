package com.example.winkcart_user.viewMOdels

import com.example.winkcart_user.data.repository.createMockProduct
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.categorie.categoriesViewModel.CategoriesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getAllProducts success updates state to Success`() = runTest {

        val mockRepo = mockk<ProductRepo>()

        coEvery { mockRepo.getAllProducts() } returns flowOf(
            ProductResponse(
                products = listOf(
                    createMockProduct(1, "Men Shirt", 29.99).copy(tags = "men"),
                    createMockProduct(2, "Women Dress", 59.99).copy(tags = "women")
                )
            )
        )
        viewModel = CategoriesViewModel(mockRepo)
        viewModel.getAllProducts()

        advanceUntilIdle()

        val value = viewModel.products.value
        assertTrue(value is ResponseStatus.Success)
        assertEquals(2, (value as ResponseStatus.Success).result.products.size)
    }


    @Test
    fun `test getKidsProducts returns empty when no kids products present`() = runTest {
        val mockRepo = mockk<ProductRepo>()
        coEvery { mockRepo.getAllProducts() } returns flowOf(
            ProductResponse(
                products = listOf(
                    createMockProduct(1, "Men Shirt", 29.99, "men")
                )
            )
        )
        viewModel = CategoriesViewModel(mockRepo)

        viewModel.getAllProducts()
        advanceUntilIdle()

        val kidsProducts = viewModel.getKidsProducts()
        assertTrue(kidsProducts.isEmpty())
    }

    @Test
    fun `test getAllSubCategories returns all unique types`() = runTest {
        val mockRepo = mockk<ProductRepo>()

        coEvery { mockRepo.getAllProducts() } returns flowOf(
            ProductResponse(
                products = listOf(
                    createMockProduct(1, "Men Shirt", 29.99, "men").copy(product_type = "Clothing"),
                    createMockProduct(2, "Sandals", 49.99, "kids").copy(product_type = "Footwear"),
                    createMockProduct(3, "Scarf", 19.99, "women").copy(product_type = "Accessories"),
                    createMockProduct(4, "Mug", 9.99, "").copy(product_type = "Clothing")
                )
            )
        )

        val viewModel = CategoriesViewModel(mockRepo)
        viewModel.getAllProducts()
        advanceUntilIdle()
        val types = viewModel.getALlSubCategories()
        assertEquals(setOf("Clothing", "Footwear", "Accessories"), types)
    }




    @Test
    fun `test getAllSubCategories returns empty set if no products`() = runTest {
        val mockRepo = mockk<ProductRepo>()
        coEvery { mockRepo.getAllProducts() } returns flowOf(ProductResponse(products = emptyList()))
        viewModel = CategoriesViewModel(mockRepo)

        viewModel.getAllProducts()
        advanceUntilIdle()

        val types = viewModel.getALlSubCategories()
        assertTrue(types.isEmpty())
    }
}
