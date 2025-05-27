package com.example.winkcart_user.categories.viewModel

import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.repository.FakeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class CategoriesViewModelTest  {

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var fakeRepo: FakeProductRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeProductRepository()
        viewModel = CategoriesViewModel(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllProducts emits Success with correct number of products`() = runTest {
        viewModel.getAllProducts()

        advanceUntilIdle()

        val result = viewModel.producs.value
        assertThat(result is ResponseStatus.Success, `is`(true))
        result as ResponseStatus.Success
        assertThat(result.result.products.size, `is`(3))
        assertThat(result.result.products[0].title, `is`("Nike Air Max"))
    }

}