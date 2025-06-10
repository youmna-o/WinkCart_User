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
import com.example.winkcart_user.settings.viewmodel.SettingsFactory
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.auth.AuthFactory
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorsProductFactory

import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersFactory
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
        val retrofitHelper = RetrofitHelper
            val remoteDataSource = RemoteDataSourceImpl(retrofitHelper)
            val localDataSource =  LocalDataSourceImpl(
                SettingsDaoImpl(
                    LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                )
            )
            val authFactory = AuthFactory(
                repo = FirebaseRepoImp(remoteDataSource),
                customerRepo = ProductRepoImpl(remoteDataSource,localDataSource)
            )


           // var authFactory = AuthFactory(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper)))
            var authViewModel = ViewModelProvider(this,authFactory).get(AuthViewModel :: class.java)

            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsFactory(
                    ProductRepoImpl(
                        RemoteDataSourceImpl(RetrofitHelper),
                        LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )


            val cartViewModel: CartViewModel = viewModel(
                factory = CartFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                        localDataSource =   LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )
            val favViewModel: FavouriteViewModel = viewModel(
                factory = FavouriteFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                        localDataSource =   LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )



            val brandFactory = BrandsFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val brandViewModel =  ViewModelProvider(this,brandFactory).get(BrandsViewModel :: class.java)

            val vendorFactory = VendorsProductFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val vendorProductsViewModel =  ViewModelProvider(this,vendorFactory).get(VendorProductsViewModel :: class.java)

             val categoryFactory = CategoryFactory(

                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val categoriesViewModel =  ViewModelProvider(this,categoryFactory).get(
                CategoriesViewModel :: class.java)

            val currencyFactory = CurrencyFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val currencyViewModel =  ViewModelProvider(this,currencyFactory).get(CurrencyViewModel :: class.java)



            val ordersFactory = OrdersFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val ordersViewModel = ViewModelProvider(this,ordersFactory).get(OrdersViewModel::class.java)



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
                )
           }

            }
        }
    }
/*

@Composable
fun AppInit(authViewModel : AuthViewModel,
            cartViewModel: CartViewModel,
            categoriesViewModel : CategoriesViewModel,
            settingsViewModel: SettingsViewModel,
            vendorProductViewModel :VendorProductsViewModel,
            brandsViewModel: BrandsViewModel,
            currencyViewModel : CurrencyViewModel,
            ordersViewModel: OrdersViewModel
) {
    val scroll = rememberScrollState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val screensWithBottomBar = listOf(
        NavigationRout.Home.route,
        NavigationRout.Cart.route,
        NavigationRout.Settings.route,
        NavigationRout.categories.route,
        NavigationRout.Profile.route
    )
    val showBottomBar = currentRoute in screensWithBottomBar
    LaunchedEffect(currentRoute) {
        Log.i("BottomBar", "Current Route: $currentRoute | Show BottomBar: $showBottomBar")
    }

    WinkCart_UserTheme{
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background ,
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { paddingValues ->
            val pa =paddingValues
            NavHost(
                navController = navController,
                startDestination = NavigationRout.Login.route,
                modifier = Modifier.padding(2.dp)
            ) {
                composable(NavigationRout.Login.route) {
                    LoginScreen(
                        navController = navController, authViewModel = authViewModel,
                        cartViewModel = TODO()
                    )
                }
                composable(NavigationRout.SignUp.route) {
                    SignUpScreen(navController = navController,authViewModel=authViewModel)
                }
                composable(NavigationRout.Home.route) {
                    HomeScreen(navController = navController,brandsViewModel=brandsViewModel)
                }
                composable(NavigationRout.Orders.route) {
                    OrdersScreen(navController = navController, ordersViewModel = ordersViewModel)
                }
                composable(NavigationRout.categories.route) {
                    CategoriesScreen(categoriesViewModel,navController,currencyViewModel)
                }

                composable(NavigationRout.Settings.route) { SettingsView(settingsViewModel) }
                composable(NavigationRout.Cart.route) { CartView(cartViewModel) }
                composable(NavigationRout.Profile.route) { ProfileScreen(navController) }
                composable("vendor_products/{brand}") { backStackEntry ->
                    val brand = backStackEntry.arguments?.getString("brand") ?: ""
                    VendorProductScreen(brand, navController = navController, vendorProductsViewModel = vendorProductViewModel)
                }
                composable(NavigationRout.OrderDetails.route) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                   OrderDetailsScreen(navController,ordersViewModel,orderId.toLong())
                }
                composable(NavigationRout.ProductInfo.route) {
                        backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductInfo(
                        productId.toLong(),
                        navController = navController,
                        scrollState = scroll,
                        categoriesViewModel = categoriesViewModel,
                        cartViewModel = cartViewModel
                  //  currencyViewModel = currencyViewModel,
                   // favouriteViewModel = favViewModel
                )
           }

            }
        }
    }

    }

*/
