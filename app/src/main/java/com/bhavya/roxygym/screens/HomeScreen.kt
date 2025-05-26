package com.bhavya.roxygym.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bhavya.roxygym.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(topBar = {
        TopAppBar(title = {},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("LoginScreen") }) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "")
                }
            }
        )
    }) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

            ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {

                ClickableSurface(
                    onClick = {  },
                    modifier = Modifier.height(200.dp) // Adjust height as needed
                    .weight(1f).clickable {navController.navigate("AddUserScreen")}
                    ,text = "Add People", image = painterResource(id= R.drawable.adduser)
                )

                Spacer(Modifier.width(5.dp))


                ClickableSurface(
                    onClick = {  },
                    modifier = Modifier.height(200.dp) // Adjust height as needed
                        .clickable { navController.navigate("StatusScreen") }.weight(1f),
                    text="Status", image = painterResource(id=R.drawable.checklist)

                )

            }
            Spacer(Modifier.height(50.dp))



            ClickableSurface(
                onClick = {  },
                modifier = Modifier.padding(horizontal = 20.dp).height(200.dp).fillMaxWidth().clickable { navController.navigate("EditScreen") },
                text = "Edit", image = painterResource(id=R.drawable.discount)

            )
            Spacer(Modifier.height(50.dp))
            ClickableSurface(
                onClick = {  },
                modifier = Modifier.padding(horizontal = 20.dp).height(200.dp).fillMaxWidth().clickable { navController.navigate("PriceScreen") },
                text = "Prices", image = painterResource(id=R.drawable.affordable)

            )


        }
    }
}


@Composable
fun ClickableSurface(onClick: () -> Unit, modifier: Modifier=Modifier, text:String, image: Painter?,) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center



        ) {
            image?.let {
                Image(
                    painter = it,
                    contentDescription = "Surface Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),

                )
            }
            Text(
                text = text,
                modifier = Modifier.padding(16.dp), // Adds padding at the bottom
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}