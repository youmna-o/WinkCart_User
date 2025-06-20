package com.example.winkcart_user.payment.viewModel

import com.example.winkcart_user.data.repository.ProductRepo
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PaymentViewModelTest {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var repo: ProductRepo

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        coEvery { repo.readCustomersID() } returns "123"
        viewModel = PaymentViewModel(repo)
    }

    @Test
    fun `validateCardForm returns true for valid input`() {
        val isValid = viewModel.validateCardForm("John Doe", "1234567812345678", "12/26", "123")
        assertTrue(isValid)
        val state = viewModel.formValidationState.value
        assertFalse(state.nameOnCartError)
        assertFalse(state.cardNumberError)
        assertFalse(state.expireDateError)
        assertFalse(state.cvvError)
    }

    @Test
    fun `validateCardForm returns false for invalid input`() {
        val isValid = viewModel.validateCardForm("", "123", "", "1")
        assertFalse(isValid)
        val state = viewModel.formValidationState.value
        assertTrue(state.nameOnCartError)
        assertTrue(state.cardNumberError)
        assertTrue(state.expireDateError)
        assertTrue(state.cvvError)
    }

}
