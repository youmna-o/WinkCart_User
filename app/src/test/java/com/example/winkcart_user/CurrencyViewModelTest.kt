package com.example.winkcart_user

import com.example.winkcart_user.data.repository.ProductRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {

    private lateinit var viewModel: CurrencyViewModel
    private val repo: ProductRepo = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repo.readCurrencyCode() } returns flowOf("EGP")
        coEvery { repo.readCurrencyRate() } returns flowOf("30.5")
        viewModel = CurrencyViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `readCurrencyCode emits expected value`() = runTest {
        advanceUntilIdle()
        assertEquals("EGP", viewModel.currencyCode.value)
    }

    @Test
    fun `readCurrencyRate emits expected value`() = runTest {
        advanceUntilIdle()
        assertEquals("30.5", viewModel.currencyRate.value)
    }
}
