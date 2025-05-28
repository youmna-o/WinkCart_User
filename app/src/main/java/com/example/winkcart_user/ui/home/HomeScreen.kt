package com.example.winkcart_user.ui.home

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavController, viewModel: BrandsViewModel = BrandsViewModel(
    repo = ProductRepoImpl(remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()))
)) {
    val brandsState by viewModel.brandList.collectAsState()
    when (brandsState) {
        is ResponseStatus.Success -> {
            HomeScreenSuccess(navController, viewModel)
        }
        else -> {
            HomeScreenLoading()
        }
    }
}

@Composable
fun HomeScreenSuccess (navController: NavController,  viewModel: BrandsViewModel = BrandsViewModel(
    repo = ProductRepoImpl(remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()))
)){
    var brands = viewModel.brandList.collectAsState()
    val brandPairs: List<Pair<String, String>> =
        (brands.value as? ResponseStatus.Success)?.result?.smart_collections
            ?.map { brand -> brand.title to (brand.image?.src ?: "") } ?: emptyList()
    val scrollState = rememberScrollState()

    Scaffold { paddingValues ->
        val x = paddingValues
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "WinkCart",
                color = Color(0xFF2A111B),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
            )
            val allItems = listOf("arwa", "khaled", "mohamed")
            val textFieldState = rememberTextFieldState()
            var searchResults by remember { mutableStateOf(allItems) }
            SimpleSearchBar(
                textFieldState = textFieldState,
                onSearch = { query ->
                    searchResults = if (query.isBlank()) {
                        allItems
                    } else {
                        allItems.filter {
                            it.contains(query, ignoreCase = true)
                        }
                    }
                },
                searchResults = searchResults,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
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

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .background(Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Blue),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("ads", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BrandSection(
                brandItems = brandPairs,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
@Composable
fun BrandSection(
    brandItems: List<Pair<String, String>>,
    modifier: Modifier = Modifier
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
        val pairedItems = brandItems.chunked(2)
        LazyRow {
            items(pairedItems) { pair ->
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BrandCard(
                        brandName = pair.getOrNull(0)?.first ?: "",
                        imageUrl = pair.getOrNull(0)?.second ?: ""
                    )
                   Spacer(modifier = Modifier.height(8.dp))
                    if (pair.size > 1) {
                        BrandCard(
                            brandName = pair[1].first,
                            imageUrl = pair[1].second
                        )
                    } else {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenLoading(){
        var text =  "L.O.A.D.A.I.N.G"  /*stringResource(R.string.l_o_a_d_i_n_g)*/
        Column(
            Modifier.fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally

        ) {
            val blurList = text.mapIndexed { index, char ->
                if (char == ' ') {
                    remember { mutableStateOf(0f) }
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
                        color = Color.White,
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
                val rect = RectF(0f, 0f, size.width.toFloat(), size.height.toFloat())
                canvas.nativeCanvas.drawRect(rect, nativePaint)
                canvas.restore()

            }
        }

    }

}