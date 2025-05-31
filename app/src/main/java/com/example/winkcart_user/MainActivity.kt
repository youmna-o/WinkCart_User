package com.example.winkcart_user

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.auth.login.LoginScreen
import com.example.winkcart_user.ui.auth.signUp.SignUpScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.auth.AuthFactory
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.productInfo.ProductInfo

import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var vm =  CategoriesViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper())))

        var authFactory = AuthFactory(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper())))
        var authViewModel = ViewModelProvider(this,authFactory).get(AuthViewModel :: class.java)

        setContent {
            val scroll = rememberScrollState()
            val productState = vm.producs.collectAsState()

            val subCategories = remember(productState.value) {
                vm.getALlSubCategories()
            }

            val menProducts = remember (productState.value) {
                vm.getMenProducts()
            }
            val womenProducts = remember (productState.value) {
                vm.getWomenProducts()
            }
            val kidsProducts = remember (productState.value) {
                vm.getKidsProducts()
            }

            var myProducts =  remember (productState.value) {
                vm.getProductsList()
            }

            var myProduct =  remember (productState.value) {
                vm.getProduct(id=9083149353208)
            }



            LaunchedEffect(subCategories) {
                Log.i("TAG", "Unique Product Types: $subCategories")
                Log.i("TAG", "men Product Types: $menProducts")
                Log.i("TAG", "women Product Types: $womenProducts")
                Log.i("TAG", "kids Product Types: $kidsProducts")

                Log.i("Product", "**************************: ${myProduct?.body_html}")
                Log.i("Product", "**************************: ${myProduct?.title}")
                myProduct?.images?.forEach { image ->
                    Log.i("Product", "**************************: ${image.src}")
                }
                myProduct?.options?.filter {
                    it.name == "Size"
                }?.forEach { size ->
                    for(i in size.values){
                        Log.i("Product", "**************************: ${i}")
                    }
                }

                myProduct?.options?.filter {
                    it.name == "Color"
                }?.forEach { color ->
                    for(i in color.values){
                    Log.i("Product", "**************************color is : ${i}")
                }
                }

                     Log.i("Product", "**************************: ${myProduct?.variants[0]?.price}")
                     Log.i("Product", "------------------: ${vm.getRate()}")
                      Log.i("Product", "------------------: ${vm.getReview()}")
                }

            WinkCart_UserTheme {
                val  navController = rememberNavController()
           //    ProductInfo(navController, scroll)
                NavHost(
                    navController = navController,
                    startDestination = NavigationRout.SignUp.rout

                ) {
                    composable(NavigationRout.SignUp.rout) { SignUpScreen(navController,authViewModel) }
                    composable(NavigationRout.Login.rout) { LoginScreen(navController,authViewModel)}
                    composable(NavigationRout.ProductInfo.rout) { ProductInfo(navController, scroll) }

                }
               // LoginScreen()
                //SignUpScreen()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    Greeting(
////                        name = "Android",
////                        modifier = Modifier.padding(innerPadding)
////                    )
//                }
            }

           // var state = BrandsViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper()))).brandList.collectAsState()

        }
    }
}

@Composable
 fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )



}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WinkCart_UserTheme {
        Greeting("Android")
    }
}