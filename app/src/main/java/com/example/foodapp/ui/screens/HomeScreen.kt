package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

@Composable
fun HomeScreen(yemeklerViewModel: YemeklerViewModel = hiltViewModel()) {
    val yemeklerResponse by yemeklerViewModel.yemekler.collectAsState()


    when(yemeklerResponse){
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
        }
        is ResourceState.Success -> {
            val response = (yemeklerResponse as ResourceState.Success).data
            Log.d("HOMESCREEN", "HomeScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.yemekler.size}")

            if(response.yemekler.isNotEmpty()){
                //EmptyStateComponent(textValue = response.yemekler[10].yemek_adi)
                //YemekKart(yemekUrl = response.yemekler[10].yemek_resim_adi) /////////

                //FoodCardComponent(yemekler = response.yemekler)

                YemekListesi(yemekler = response.yemekler)

            }else{
                // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }
        }
        is ResourceState.Error -> {
            val error = (yemeklerResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... $error")
        }
    }
}

@Composable
fun EmptyStateComponent(textValue : String){
    Text(text = textValue)
}

@Composable
fun HomeScreenAllFoodLazyGridComponent(){
    // TODO("LazyGrid yapısı istenilen componentin icine konacak")
}

@Composable
fun Yemekler(){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

/*
@Composable
fun SomeComposable() {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
                contentDescription = "Resim"
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {

                Button(onClick = { /* Resim tıklandığında yapılacak işlemi ekleyin */ }) {
                    Text("+")
                }
            }
        }
    }
}
*/


/*
@Composable
fun YemekListesiScreen(navController: NavHostController, sepet: List<Yemek>, sepeteEkle: (Yemek) -> Unit) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(yemekListesi.chunked(2)) { satirYemekler ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                satirYemekler.forEach { yemek ->
                    YemekKart(navController, yemek, sepeteEkle)
                }
            }
        }
    }
}

*/


/*
@Composable
fun YemekKart(yemekUrl : String) {
//    Column(
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .background(Color.Blue)
//            .padding(8.dp)
//            .clickable { } // TODO("Yemek detay sayfasına git")
//            .size(150.dp)
//    ) {
//        AsyncImage(
//            model = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
//            contentDescription = "Resim",
//            modifier = Modifier.background(Color.Red)
//        )
//
//        Spacer(modifier = Modifier.size(40.dp))
//
//        AsyncImage(
//            model = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
//            contentDescription = "Resim",
//            modifier = Modifier.background(Color.Red)
//        )
////        Text(yemek.isim, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
////        Text("${yemek.fiyat} TL", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
////        IconButton(
////            onClick = { sepeteEkle(yemek) },
////            modifier = Modifier.align(Alignment.End)
////        ) {
////            Icon(imageVector = Icons.Default.Add, contentDescription = "Sepete Ekle")
////        }
//    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(Color.Blue)
            .padding(16.dp),
        //verticalArrangement = Arrangement.SpaceEvenly,

    ){
        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                    //.background(Color.Red),

                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                FoodCard()
                FoodCard()

            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                    //.background(Color.Red),

                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                FoodCard()
                FoodCard()

            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(){
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp),
        shape = CardDefaults.shape,
        border = null,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
                    contentDescription = "Resim",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
                Text("Yemek Adı")
                Text("Yemek Fiyatı")
            }
        }
    )
}

*/

@Composable
fun YemekKart(yemek: Yemekler) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation =  CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = yemek.yemek_resim_adi,
                contentDescription = yemek.yemek_adi,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = yemek.yemek_adi)
        }
    }
}


@Composable
fun YemekListesi(yemekler: List<Yemekler>) {
    LazyColumn {
        items(yemekler.size) { yemek ->
            YemekKart(yemek = yemekler[yemek])
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun YemekKartPreview(){

}

