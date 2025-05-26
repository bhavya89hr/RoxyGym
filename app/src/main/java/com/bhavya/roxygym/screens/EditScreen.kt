import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bhavya.roxygym.screens.FirestoreUsers
import com.bhavya.roxygym.screens.FirestoreViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.navigation.NavController

@Composable
fun EditScreen(viewModel: FirestoreViewModel = viewModel(),navController: NavController) {
    val users by viewModel.users.collectAsState()

    LazyColumn(    modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(users) { user ->
            UserItem(user, onDelete = { viewModel.DeleteUser(user.id) })
        }
    }
}

@Composable
fun UserItem(user: FirestoreUsers, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = user.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        }
        Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(Color.Red)) {
            Text("Remove", color = Color.White)
        }
    }
}
