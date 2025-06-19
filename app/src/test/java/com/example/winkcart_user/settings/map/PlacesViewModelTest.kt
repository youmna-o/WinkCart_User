package com.example.winkcart_user.settings.map

import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlacesViewModelTest {

    private lateinit var viewModel: PlacesViewModel
    private val repo: ProductRepo = mockk(relaxed = true)
    private val placesClient: PlacesClient = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PlacesViewModel(repo, placesClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateLocation sets addressLatLon and default name`() = runTest {
        val newLatLng = LatLng(29.0, 32.0)
        viewModel.updateLocation(newLatLng)

        assertEquals(newLatLng, viewModel.addressLatLon.value)
        assertEquals("Selected Address", viewModel.addressName.value)
    }
}
