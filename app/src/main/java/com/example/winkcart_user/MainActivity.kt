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
import com.example.winkcart_user.ui.home.main.brandsViewModel.BrandsFactory
import com.example.winkcart_user.ui.home.main.brandsViewModel.BrandsViewModel
import com.example.winkcart_user.cart.viewModel.CartFactory
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoriesViewModel
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoryFactory
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.favourite.FavouriteFactory
import com.example.winkcart_user.favourite.FavouriteViewModel
import com.example.winkcart_user.payment.viewModel.PaymentFactory
import com.example.winkcart_user.payment.viewModel.PaymentViewModel
import com.example.winkcart_user.settings.viewmodel.SettingsFactory
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModel
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModelFactory
import com.example.winkcart_user.ui.auth.AuthFactory
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorsProductFactory
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersFactory
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.ui.profile.userProfile.view.ProfileFactory
import com.example.winkcart_user.ui.profile.userProfile.view.ProfileViewModel
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
        }
        setContent {
            val retrofitHelper = RetrofitHelper
            val remoteDataSource = RemoteDataSourceImpl(retrofitHelper)
            val localDataSource = LocalDataSourceImpl(
                SettingsDaoImpl(
                    LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                )
            )
            val authFactory = AuthFactory(
                repo = FirebaseRepoImp(remoteDataSource),
                customerRepo = ProductRepoImpl(remoteDataSource, localDataSource)
            )


            var authViewModel = ViewModelProvider(this, authFactory).get(AuthViewModel::class.java)

            val profileFactory = ProfileFactory(
                repo = FirebaseRepoImp(remoteDataSource),
                productRepo = ProductRepoImpl(remoteDataSource, localDataSource)
            )
            var profileViewModel = ViewModelProvider(this, profileFactory).get(ProfileViewModel::class.java)

            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsFactory(
                    ProductRepoImpl(
                        RemoteDataSourceImpl(RetrofitHelper),
                        LocalDataSourceImpl(
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
            val cartViewModel: CartViewModel = viewModel(
                factory = CartFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
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
            val favViewModel: FavouriteViewModel = viewModel(
                factory = FavouriteFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
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

            val brandFactory = BrandsFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val brandViewModel =
                ViewModelProvider(this, brandFactory).get(BrandsViewModel::class.java)

            val vendorFactory = VendorsProductFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val vendorProductsViewModel =
                ViewModelProvider(this, vendorFactory).get(VendorProductsViewModel::class.java)

            val categoryFactory = CategoryFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val categoriesViewModel = ViewModelProvider(this, categoryFactory).get(
                CategoriesViewModel::class.java
            )


            val currencyFactory = CurrencyFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val currencyViewModel =
                ViewModelProvider(this, currencyFactory).get(CurrencyViewModel::class.java)

            val ordersFactory = OrdersFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val ordersViewModel =
                ViewModelProvider(this, ordersFactory).get(OrdersViewModel::class.java)

            val paymentFactory = PaymentFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
                    localDataSource = LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val paymentViewModel =
                ViewModelProvider(this, paymentFactory)[PaymentViewModel::class.java]


            //map
            val placesClient= Places.createClient(LocalContext.current)
            val placesViewModelFactory = PlacesViewModelFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper),
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
                    authViewModel, settingsViewModel = settingsViewModel,
                    cartViewModel = cartViewModel,
                    vendorProductViewModel = vendorProductsViewModel,
                    brandsViewModel = brandViewModel,
                    categoriesViewModel = categoriesViewModel,
                    currencyViewModel = currencyViewModel,
                    ordersViewModel = ordersViewModel,
                    favouriteViewModel = favViewModel,
                    paymentViewModel = paymentViewModel,
                    placesViewModel = placesViewModel,
                    profileViewModel = profileViewModel
                )
            }

        }
    }
}




