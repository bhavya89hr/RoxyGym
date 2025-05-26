package com.bhavya.roxygym.screens

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(navController: NavController) {
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Prices") }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(scrollState)
        .padding(16.dp)
    ) {
      SurfaceC("1 Month = 1000")
      Spacer(Modifier.height(8.dp))
      SurfaceC("3 Months = 2500")
      Spacer(Modifier.height(8.dp))
      SurfaceC("6 Months = 4500")
      Spacer(Modifier.height(8.dp))
      SurfaceC("1 Year = 8000")
    }
  }
}


@Composable
fun SurfaceC(text:String){
  Surface(
    modifier = Modifier.height(150.dp)
      .padding(16.dp)
      .fillMaxWidth(),
    color = Color.Red,
    shape = RoundedCornerShape(16.dp),
    shadowElevation = 8.dp
  ) {
    Text(textAlign = TextAlign.Center,
      text = text,
      fontSize = 30.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White, // White text color
      modifier = Modifier.padding(16.dp)
    )


  }}