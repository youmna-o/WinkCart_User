package com.example.winkcart_user.viewModels

import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.viewMOdels.createFakeOrderDetailsResponse

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModelTest {

    private lateinit var viewModel: OrdersViewModel
    private val mockRepo = mockk<ProductRepo>()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getOrderDetails sets Success when repository returns data`() = runTest {
        val orderId = 123L
        val expected = createFakeOrderDetailsResponse(
            email = "john@example.com",
            currency = "USD",
            createAt = "2025-06-01T12:00:00",
        )

        coEvery { mockRepo.getSpecificOrderDetails(orderId) } returns flowOf(expected)

        viewModel = OrdersViewModel(mockRepo)
        viewModel.getOrderDetails(orderId)

        advanceUntilIdle()

        val state = viewModel.orderDetails.value
        assertTrue(state is ResponseStatus.Success)
        assertEquals(expected, (state as ResponseStatus.Success).result)
    }
}
