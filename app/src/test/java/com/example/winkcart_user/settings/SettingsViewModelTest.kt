package com.example.winkcart_user.settings

import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.data.repository.ProductRepo
import io.mockk.coEvery
import io.mockk.every
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private val repo: ProductRepo = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createMockCustomerAddressRequest(): CustomerAddressRequest {
        val address = CustomerAddress(
            id = 1L,
            title = "Home",
            country = "Egypt",
            city = "Cairo",
            address = "123 Nile Street",
            name = "John Doe",
            phone = "1012345678",
            default = true
        )
        return CustomerAddressRequest(customerAddress = address)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repo.readCustomersID() } returns "1"
        viewModel = SettingsViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validateAddressForm returns true when all inputs are valid`() {
        val result = viewModel.validateAddressForm(
            title = "Home",
            address = "123 Street",
            contactPerson = "John",
            phoneNumber = "1012345678"
        )

        assertTrue(result)
        val state = viewModel.formValidationState.value
        assertFalse(state.titleError)
        assertFalse(state.addressError)
        assertFalse(state.contactPersonError)
        assertFalse(state.phoneError)
    }

    @Test
    fun `validateAddressForm returns false when phone number is invalid`() {
        val result = viewModel.validateAddressForm(
            title = "Home",
            address = "123 Street",
            contactPerson = "John",
            phoneNumber = "9999999999" // invalid prefix
        )

        assertFalse(result)
        val state = viewModel.formValidationState.value
        assertTrue(state.phoneError)
    }

    @Test
    fun `validateAddressForm returns false when any required field is blank`() {
        val result = viewModel.validateAddressForm(
            title = "",
            address = "",
            contactPerson = "",
            phoneNumber = ""
        )

        assertFalse(result)
        val state = viewModel.formValidationState.value
        assertTrue(state.titleError)
        assertTrue(state.addressError)
        assertTrue(state.contactPersonError)
        assertTrue(state.phoneError)
    }

    @Test
    fun `getCustomerAddresses updates state with addresses and default`() = runTest {
        val mockAddress = CustomerAddress(
            id = 1,
            title = "Home",
            country = "Egypt",
            city = "Cairo",
            address = "123",
            name = "Ali",
            phone = "1012345678",
            default = true
        )
        val response = CustomerAddressesResponse(addresses = listOf(mockAddress))

        coEvery { repo.getCustomerAddresses(1) } returns flowOf(response)

        viewModel.getCustomerAddresses(1)
        advanceUntilIdle()

        val state = viewModel.customerAddresses.value
        assertTrue(state is ResponseStatus.Success)
        assertEquals(mockAddress, (viewModel.defaultCustomerAddresses.value as ResponseStatus.Success).result)
        assertEquals("Cairo", (viewModel.defaultCity.value as ResponseStatus.Success).result)
    }

    @Test
    fun `setDefaultAddress triggers getCustomerAddresses on success`() = runTest {
        val address = CustomerAddress(
            id = 1,
            title = "Home",
            country = "Egypt",
            city = "Cairo",
            address = "123",
            name = "Ali",
            phone = "1012345678",
            default = true
        )
        val response = CustomerAddressesResponse(addresses = listOf(address))

        coEvery { repo.setDefaultAddress(1, 1) } returns flowOf(Unit)
        coEvery { repo.getCustomerAddresses(1) } returns flowOf(response)

        viewModel.setDefaultAddress(1, 1)
        advanceUntilIdle()

        assertTrue(viewModel.customerAddresses.value is ResponseStatus.Success)
    }

    @Test
    fun `getCustomerAddress sets value when success`() = runTest {
        val request = createMockCustomerAddressRequest()
        coEvery { repo.getCustomerAddress(1, 2) } returns flowOf(request)

        viewModel.getCustomerAddress(1, 2)
        advanceUntilIdle()

        val state = viewModel.customerAddress.value
        assertTrue(state is ResponseStatus.Success)
        assertEquals(request, (state as ResponseStatus.Success).result)
    }

    @Test
    fun `updateCustomerAddress updates and fetches addresses`() = runTest {
        val updateRequest = createMockCustomerAddressRequest()
        val addresses = CustomerAddressesResponse(addresses = listOf(updateRequest.customerAddress))

        coEvery { repo.updateCustomerAddress(1, 1, updateRequest) } returns flowOf(Unit)
        coEvery { repo.getCustomerAddresses(1) } returns flowOf(addresses)

        viewModel.updateCustomerAddress(1, 1, updateRequest)
        advanceUntilIdle()

        assertTrue(viewModel.customerAddressUpdateResponse.value is ResponseStatus.Success)
        assertTrue(viewModel.customerAddresses.value is ResponseStatus.Success)
    }

    @Test
    fun `deleteCustomerAddress deletes and refreshes addresses`() = runTest {
        val addresses = CustomerAddressesResponse(addresses = emptyList())
        coEvery { repo.deleteCustomerAddress(1, 1) } returns flowOf(Unit)
        coEvery { repo.getCustomerAddresses(1) } returns flowOf(addresses)

        viewModel.deleteCustomerAddress(1, 1)
        advanceUntilIdle()

        assertTrue(viewModel.deleteCustomerAddresses.value is ResponseStatus.Success)
        assertTrue(viewModel.customerAddresses.value is ResponseStatus.Success)
    }

    @Test
    fun `addCustomerAddress adds and refreshes addresses`() = runTest {
        val request = createMockCustomerAddressRequest()
        val addresses = CustomerAddressesResponse(addresses = listOf(request.customerAddress))

        coEvery { repo.addCustomerAddress(1, request) } returns flowOf(Unit)
        coEvery { repo.getCustomerAddresses(1) } returns flowOf(addresses)

        viewModel.addCustomerAddress(1, request)
        advanceUntilIdle()

        assertTrue(viewModel.customerAddresses.value is ResponseStatus.Success)
    }
}