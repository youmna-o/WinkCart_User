package com.example.winkcart_user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.winkcart_user.cart.viewModel.CartFactory
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModel
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModelFactory
import com.example.winkcart_user.theme.WinkCart_UserTheme
import com.example.winkcart_user.profile.orders.viewModel.OrdersFactory
import com.example.winkcart_user.profile.orders.viewModel.OrdersViewModel
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
        }
        setContent {
            val retrofitHelper = RetrofitHelper()

            val cartViewModel: CartViewModel = viewModel(
                factory = CartFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(retrofitHelper),
                        localDataSource = LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences(
                                    "AppSettings",
                                    MODE_PRIVATE
                                )
                            )
                        )
                    )
                )
            )

            val ordersFactory = OrdersFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(retrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val ordersViewModel =
                ViewModelProvider(this, ordersFactory).get(OrdersViewModel::class.java)

            //map
            val placesClient= Places.createClient(LocalContext.current)
            val placesViewModelFactory = PlacesViewModelFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(retrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                ),
                placesClient
            )
            val placesViewModel =
                ViewModelProvider(this, placesViewModelFactory)[PlacesViewModel::class.java]

            WinkCart_UserTheme {
                cartViewModel.readCustomerID()
                AppInit(
                    cartViewModel = cartViewModel,
                    ordersViewModel = ordersViewModel,
                    placesViewModel=placesViewModel,

                )
            }

        }
    }
}




