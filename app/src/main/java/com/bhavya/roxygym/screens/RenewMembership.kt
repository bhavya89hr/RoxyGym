import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bhavya.roxygym.screens.FirestoreUsers
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenewScreen(
    user: FirestoreUsers,
    navController: NavController
) {
    val context = LocalContext.current
    var durationInput by remember { mutableStateOf("") } // Store as String for text field
    val firestore = FirebaseFirestore.getInstance()
    val userDocRef = firestore.collection("users").document(user.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Renew Membership") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select Membership Duration", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = durationInput,
                onValueChange = { durationInput = it },
                label = { Text("Duration (Months)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val selectedDuration = durationInput.toIntOrNull()
                    if (selectedDuration == null || selectedDuration <= 0) {
                        Toast.makeText(context, "Please enter a valid duration (e.g., 1, 2, 3)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newRegistrationDate = Timestamp.now()

                    val updates = mapOf(
                        "registrationDate" to newRegistrationDate,
                        "membershipDuration" to selectedDuration
                    )

                    userDocRef.update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Membership Renewed!", Toast.LENGTH_SHORT).show()
                            navController.navigate("HomeScreen")
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to renew membership.", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Renew Membership")
            }
        }
    }
}
