package com.example.winkcart_user.ui.home.main

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl
import com.example.winkcart_user.data.model.vendors.SmartCollection
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.home.ads.ADSPager
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.example.winkcart_user.ui.utils.rememberImeState
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavController, brandsViewModel: BrandsViewModel
) {
    val brandsState by brandsViewModel.brandList.collectAsState()
    when (brandsState) {
        is ResponseStatus.Success -> {
            HomeScreenSuccess(navController, brandsViewModel)
        }
        is ResponseStatus.Error -> {
            HomeScreenError(
                message = (brandsState as ResponseStatus.Error).error.message
                    ?: "An unknown error occurred",
                onRetry = { brandsViewModel.getSmartCollections() }
            )
        }else -> {
            HomeScreenLoading()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenSuccess(
    navController: NavController,
    viewModel: BrandsViewModel,
) {
    val state = viewModel.brandList.collectAsState()
    val smartCollections = (state.value as? ResponseStatus.Success)?.result?.smart_collections ?: emptyList()
    var filteredBrands by remember { mutableStateOf(smartCollections) }
    val textFieldState = rememberTextFieldState()
    val scrollState = rememberScrollState()
    val allBrandTitles = smartCollections.map{ it.title }
    val imeState = rememberImeState()
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "WinkCart",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A111B)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(245, 245, 245)
                )
            )
        }
    ) { paddingValues ->
        val x = paddingValues
        Column(
            modifier = Modifier
                .padding(5.dp)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(40.dp))

            SimpleSearchBarSimple(
                textFieldState = textFieldState,
                onSearch = { query ->
                    filteredBrands = if (query.isBlank()) {
                        smartCollections
                    } else {
                        smartCollections.filter { it.title.contains(query, ignoreCase = true) }
                    }
                },
                searchResults = allBrandTitles
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Deliver to ")
                Text(
                    "Kom Hamada",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow"
                )
            }

            Card(
                modifier = Modifier.background(Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ADSPager()
                }
            }

            if (filteredBrands.isNotEmpty()) {
                    VendorsScetion(
                        brandItems = filteredBrands,
                        modifier = Modifier.fillMaxWidth(),
                        navController = navController
                    )

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Try searching with different keywords 🔍",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
    LaunchedEffect(imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBarSimple(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = {
                        textFieldState.edit {
                            replace(0, length, it)
                        }
                        onSearch(it)
                    },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                    },
                    expanded = false,
                    onExpandedChange = {  },
                    placeholder = { Text("Search") },
                )

            },
            expanded = false,
            onExpandedChange = { },
            colors = SearchBarDefaults.colors(
                containerColor = Color.White,
                dividerColor = Color.LightGray,
            )
        ) {
            searchResults.take(3).forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}



@Composable
fun VendorsScetion(
    brandItems: List<SmartCollection>,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            text = "Top Brands",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        Text(
            text = "Shop your favorites",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
            color = Color.Gray
        )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val pairedItems = brandItems.chunked(2)

                items(pairedItems) { pair ->
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { item ->
                            VendorCard(
                                vendorName = item.title,
                                imageUrl = item.image?.src ?: "",
                                onClick = {
                                    navController.navigate(
                                        NavigationRout.VendorProducts.createRoute(item.title)
                                    )
                                }
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.height(150.dp))
                        }
                    }
                }
            }
    }
}


@Composable
fun HomeScreenLoading() {
    val text = "L.O.A.D.A.I.N.G"
    Column(
        Modifier
            .fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        val blurList = text.mapIndexed { index, char ->
            if (char == ' ') {
                remember { mutableFloatStateOf(0f) }
            } else {
                val infiniteTransition =
                    rememberInfiniteTransition(label = "infinite transition $index")
                infiniteTransition.animateFloat(
                    initialValue = 10f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 500,
                            easing = LinearEasing,

                            ),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(
                            offsetMillis = 1000 / text.length * index
                        )
                    ),
                    label = "label",
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

            ) {
            text.forEachIndexed { index, char ->

                Text(
                    text = char.toString(),
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .graphicsLayer {
                            if (char != ' ') {
                                val blueAmount = blurList[index].value
                                renderEffect = BlurEffect(
                                    radiusX = blueAmount,
                                    radiusY = blueAmount,
                                )
                            }
                        }
                        .then(
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                                Modifier.fullContentBlure(
                                    { blurList[index].value.roundToInt() }
                                )
                            } else {
                                Modifier
                            }
                        )
                )
            }

        }
    }
}

private fun Modifier.fullContentBlure(
    blureRadius: () -> Int,
    color: Color = Color.Black
): Modifier {
    return drawWithCache {
        val radius = blureRadius()
        val nativePaint: Paint = Paint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            if (radius > 0) {
                BlurMaskFilter(
                    radius.toFloat(),
                    BlurMaskFilter.Blur.NORMAL
                )
            }
        }
        onDrawWithContent {
            drawContent()
            drawIntoCanvas { canvas ->
                canvas.save()
                val rect = RectF(0f, 0f, size.width, size.height)
                canvas.nativeCanvas.drawRect(rect, nativePaint)
                canvas.restore()

            }
        }

    }
}

@Composable
fun HomeScreenError(
    message: String,
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = Color.Red,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}


