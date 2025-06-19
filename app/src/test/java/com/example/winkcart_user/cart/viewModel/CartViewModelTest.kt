package com.example.winkcart_user.cart.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.winkcart_user.data.Repository.createDraftOrderWithItems
import com.example.winkcart_user.data.Repository.createFakeDraftOrderRequest
import com.example.winkcart_user.data.Repository.createPercentageCoupon
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.repository.ProductRepo
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RunWith(AndroidJUnit4::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var repo: ProductRepo

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        coEvery { repo.readCustomerID() } returns flowOf("123")
        coEvery { repo.getAllDraftOrders() } returns flowOf(DraftOrderResponse(draft_orders = emptyList()))
        viewModel = CartViewModel(repo)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setAppliedCoupon should update appliedCoupon and recalculate total`() = runTest {
        // Given
        val coupon = createPercentageCoupon(value = 10.0)

        viewModel.setCurrencyForTest("EGP", "1.0")

        val draftOrder = createDraftOrderWithItems(
            LineItemDraft(
                price = "100.0",
                quantity = 1,
                product_id = 1,
                variant_id = 1,
                title = "Test Item",
                properties = emptyList()
            )
        )
        viewModel.setDraftOrdersForTest(ResponseStatus.Success(DraftOrderResponse(listOf(draftOrder))))

        // When
        viewModel.setAppliedCoupon(coupon)
        advanceUntilIdle()

        // Then
        assertEquals("90.00 EGP", viewModel.totalAmount.value)
        assertEquals("10.00 EGP", viewModel.discountAmount.value)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `clearAppliedCoupon should reset appliedCoupon and remove discount`() = runTest {
        // Given
        val coupon = createPercentageCoupon(value = 10.0)

        viewModel.setCurrencyForTest("EGP", "1.0")

        val draftOrder = createDraftOrderWithItems(
            LineItemDraft(
                price = "200.0",
                quantity = 1,
                product_id = 1,
                variant_id = 1,
                title = "Test Item",
                properties = emptyList()
            )
        )

        viewModel.setDraftOrdersForTest(ResponseStatus.Success(DraftOrderResponse(listOf(draftOrder))))

        viewModel.setAppliedCoupon(coupon)
        advanceUntilIdle()

        // When
        viewModel.clearAppliedCoupon()
        advanceUntilIdle()

        // Then
        assertEquals("200.00 EGP", viewModel.totalAmount.value)
        assertEquals("0.00 EGP", viewModel.discountAmount.value)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createDraftCartOrder should return error if draft order already exists`() = runTest {
        // Given
        val existingOrder = createDraftOrderWithItems(
            LineItemDraft(
                title = "Test Item",
                price = "100.0",
                quantity = 1,
                product_id = 1,
                variant_id = 1,
                properties = listOf(
                    com.example.winkcart_user.data.model.draftorder.cart.Property("SavedAt", "Cart")
                )
            )
        )
        coEvery { repo.getAllDraftOrders() } returns flowOf(
            DraftOrderResponse(listOf(existingOrder))
        )

        val draftOrderRequest = createFakeDraftOrderRequest()
        coEvery { repo.createDraftOrder(any()) } returns flowOf(Any())

        // When
        viewModel.createDraftCartOrder("123", draftOrderRequest)
        advanceUntilIdle()

        // Then
        val result = viewModel.createDraftOrderResponse.value
        assert(result is ResponseStatus.Error && (result).error.message == "Draft order already exists")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateDraftOrder should update and refresh orders`() = runTest {
        val draftOrderRequest = createFakeDraftOrderRequest()
        val updatedDraft = createDraftOrderWithItems(
            LineItemDraft(title = "Updated Item", price = "50.0", quantity = 1, product_id = 1, variant_id = 1, properties = emptyList())
        )
        coEvery { repo.updateDraftOrder(any(), any()) } returns flowOf(
            DraftOrderResponse(listOf(updatedDraft))
        )
        coEvery { repo.readCustomersID() } returns "123"
        coEvery { repo.getAllDraftOrders() } returns flowOf(DraftOrderResponse(listOf(updatedDraft)))

        viewModel.updateDraftOrder(999, draftOrderRequest)
        advanceUntilIdle()

        val result = viewModel.updateDraftOrder.value
        assert(result is ResponseStatus.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getDraftOrders should fetch and filter matching cart orders`() = runTest {
        val order = createDraftOrderWithItems(
            LineItemDraft(
                title = "Filtered Item",
                price = "30.0",
                quantity = 1,
                product_id = 1,
                variant_id = 1,
                properties = listOf(com.example.winkcart_user.data.model.draftorder.cart.Property("SavedAt", "Cart"))
            )
        )
        coEvery { repo.readCustomersID() } returns "123"
        coEvery { repo.getAllDraftOrders() } returns flowOf(
            DraftOrderResponse(listOf(order))
        )

        viewModel.getDraftOrders("123")
        advanceUntilIdle()

        val result = viewModel.draftOrders.value
        assert(result is ResponseStatus.Success && (result ).result.draft_orders.size == 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteDraftOrder should delete order and refresh list`() = runTest {
        coEvery { repo.deleteDraftOrder(999L) } returns flowOf(Unit)
        coEvery { repo.readCustomersID() } returns "123"
        coEvery { repo.getAllDraftOrders() } returns flowOf(DraftOrderResponse(emptyList()))

        viewModel.setCustomerIdForTest("123")
        viewModel.deleteDraftOrder(999L)
        advanceUntilIdle()

        val result = viewModel.deleteDraftOrders.value
        assert(result is ResponseStatus.Success)
    }

    @Test
    fun `getRemainingMonthsText returns correct text for different month differences`() {
        val now = ZonedDateTime.now()
        val oneMonthLater = now.plusMonths(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val twoMonthsLater = now.plusMonths(2).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val pastDate = now.minusDays(10).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        assertEquals("1 month remaining", viewModel.getRemainingMonthsText(oneMonthLater))
        assertEquals("2 months remaining", viewModel.getRemainingMonthsText(twoMonthsLater))
        assertEquals("< a month remaining", viewModel.getRemainingMonthsText(pastDate))
    }

}
