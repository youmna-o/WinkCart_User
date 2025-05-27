package com.example.winkcart_user.brands.viewModel

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

class BrandViewModeltest {
    private lateinit var viewModel: BrandsViewModel
    private lateinit var fakeRepo: FakeProductRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeProductRepository()
        viewModel = BrandsViewModel(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun getSmartCollections_SuccessWithTrueTitle() = runTest {
        viewModel.getSmartCollections()
        advanceUntilIdle()
        val result = viewModel.brandList.value
        assertThat(result is ResponseStatus.Success, `is`(true))
        result as ResponseStatus.Success
        assertThat(result.result.smart_collections[0].title, `is`("Nike"))
    }



}