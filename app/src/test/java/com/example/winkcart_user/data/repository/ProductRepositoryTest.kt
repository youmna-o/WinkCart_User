package com.example.winkcart_user.data.repository

import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {

    private lateinit var fakeRepo: FakeProductRepository
    private lateinit var BrandViewModel: BrandsViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepo = FakeProductRepository()
        BrandViewModel = BrandsViewModel(fakeRepo)
        categoriesViewModel = CategoriesViewModel(fakeRepo)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun getSmartCollections_returnsExpectedSize(): Unit = runTest {
        val result = fakeRepo.getAllProducts().first()
        assertThat(result?.products?.size, `is`(3))
    }


    @Test
    fun getsmartCollections_firstTitle(): Unit = runTest {
        val result = fakeRepo.getSmartCollections().first()
        assertThat(result?.smart_collections?.get(0)?.title, `is`("Nike"))
    }

    @Test
    fun getAllProducts_returnsExpectedList() = runTest {
        val productResponse = fakeRepo.getAllProducts().first()
        val products = productResponse?.products
        assertThat(products?.size, `is`(3))
    }

    @Test
    fun getAllProducts_returnsNike() = runTest {
        val productResponse = fakeRepo.getAllProducts().first()
        val products = productResponse?.products
        assertThat(products?.get(0)?.title, `is`("Nike Air Max"))

    }


}
