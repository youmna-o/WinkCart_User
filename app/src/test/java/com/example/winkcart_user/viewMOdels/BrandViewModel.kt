package com.example.winkcart_user.viewMOdels

import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.vendors.Image
import com.example.winkcart_user.data.model.vendors.SmartCollection
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.home.main.brandsViewModel.BrandsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
@OptIn(ExperimentalCoroutinesApi::class)
class BrandsViewModelTest {

    private lateinit var repo: ProductRepo
    private lateinit var viewModel: BrandsViewModel

    private val fakeSmartCollections = listOf(
        SmartCollection(
            title = "Nike",
            image = Image(src = "https://example.com/mens.png")
        ),
        SmartCollection(
            title = "Adidas",
            image = Image(src = "https://example.com/womens.png")
        ),
        SmartCollection(
            title = "Dior",
            image = null
        )
    )

    private val smartCollectionsResponse = SmartCollectionsResponse(fakeSmartCollections)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repo = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSmartCollections sets Success state with data`() = runTest {
        coEvery { repo.getFilteredSmartCollections() } returns flowOf(smartCollectionsResponse)

        viewModel = BrandsViewModel(repo)

        val result = viewModel.brandList.first { it !is ResponseStatus.Loading }

        assertTrue(result is ResponseStatus.Success)
        val data = (result as ResponseStatus.Success).result

        assertEquals(3, data.smart_collections.size)
        assertEquals("Nike", data.smart_collections[0].title)
        assertEquals("Adidas", data.smart_collections[1].title)
        assertEquals("Dior", data.smart_collections[2].title)
        assertEquals(null, data.smart_collections[2].image)
    }
}
