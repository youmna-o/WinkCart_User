package com.example.winkcart_user.favourite

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.Customer
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.data.model.draftorder.cart.Property
import com.example.winkcart_user.data.model.orders.LineItem
import com.example.winkcart_user.data.repository.ProductRepo
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest {
    private lateinit var  viewModel: FavouriteViewModel
    lateinit var repo: ProductRepo
    @Before
    fun setUp() {
        repo= mockk(relaxed = true)
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = FavouriteViewModel(repo)
    }

    @Test
    fun `createDraftFavouriteOrder creates order when not exists`() = runTest {
        // Arrange
        val customerId = "123"
        val customerIdLong = 123L

        val draftOrderRequest = DraftOrderRequest(
            draft_order = DraftOrder(
                id = 1L,
                customer = Customer(id = customerIdLong),
                line_items = listOf(
                    LineItemDraft(
                        variant_id = 101,
                        product_id = 201,
                        title = "Product 1",
                        price = "99.99",
                        quantity = 1,
                        properties = listOf(Property(name = "SavedAt", value = "Favourite"))
                    )
                )
            )
        )

        val existingResponse = DraftOrderResponse(draft_orders = emptyList())

        // Mock repo methods
        coEvery { repo.getAllDraftOrders() } returns flowOf(existingResponse)
        coEvery { repo.createDraftOrder(draftOrderRequest) } returns flowOf(Unit)
        coEvery { repo.readCustomersID() } returns customerId


        viewModel.createDraftFavouriteOrder(customerId, draftOrderRequest)
        advanceUntilIdle()


        val result = viewModel.draftOrders.value
        assertTrue(result is ResponseStatus.Success || result is ResponseStatus.Loading)
    }


    @Test
    fun `deleteDraftOrder should update state with success when deletion is successful`() = runTest {
        val draftOrderId = 123L
        val customerId = "123"

        coEvery { repo.deleteDraftOrder(draftOrderId) } returns flowOf(Unit)
        coEvery { repo.readCustomersID() } returns customerId
        coEvery { repo.getAllDraftOrders() } returns flowOf(DraftOrderResponse(draft_orders = emptyList()))

        viewModel.deleteDraftOrder(draftOrderId)

        val state = viewModel.deleteDraftOrders.value
        assertTrue(state is ResponseStatus.Success)
    }


    @Test
    fun `getDraftOrders should update state with filtered orders when successful`() = runTest {
        // Arrange
        val customerId = "123"
        val customerIdLong = 123L

        coEvery { repo.readCustomersID() } returns customerId

        val mockDraftOrders = listOf(
            DraftOrder(
                id = 1L,
                customer = Customer(id = customerIdLong),
                line_items = listOf(
                    LineItemDraft(
                        variant_id = 1,
                        product_id = 1,
                        title = "Item 1",
                        price = "10.0",
                        quantity = 1,
                        properties = listOf(Property(name = "SavedAt", value = "Favourite"))
                    )
                )
            )
        )

        coEvery { repo.getAllDraftOrders() } returns flowOf(DraftOrderResponse(draft_orders = mockDraftOrders))

        // Act
        viewModel.getDraftOrders(customerId)

        // Assert
        val state = viewModel.draftOrders.value
        assertTrue(state is ResponseStatus.Success)
        assertEquals(1, (state as ResponseStatus.Success).result.draft_orders.size)
    }




}