import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bhavya.roxygym.screens.FirestoreData

import com.bhavya.roxygym.screens.FirestoreUsers
import com.bhavya.roxygym.screens.FirestoreViewModel
import com.google.firebase.firestore.auth.User
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale


@Composable
fun StatusScreen(navController: NavController, viewModel: FirestoreViewModel = viewModel()) {
    val users by viewModel.users.collectAsState()

    fun getMembershipStatus(user: FirestoreUsers): String {
        val currentDate = Date()
        return if (user.registrationDate != null && user.membershipDuration != null) {
            val calendar = Calendar.getInstance()
            calendar.time = user.registrationDate.toDate()
            calendar.add(Calendar.MONTH, user.membershipDuration)
            val expirationDate = calendar.time
            if (currentDate.before(expirationDate)) "Active" else "Expired"
        } else {
            "No Membership Info"
        }
    }

    LazyColumn {
        items(users) { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Profile Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.profileImageUrl)
                                .crossfade(true)
                                .placeholder(android.R.drawable.ic_menu_gallery) // optional: show while loading
                                .error(android.R.drawable.ic_delete) // optional: show if failed to load
                                .build(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )



                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Name: ${user.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "Email: ${user.email}", fontSize = 16.sp)
                            Text(text = "Phone: ${user.phone}", fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Other Info
                    Text(text = "Address: ${user.address}", fontSize = 16.sp)
                    Text(text = "Gender: ${user.gender}", fontSize = 16.sp)
                    Text(text = "Amount Paid: ${user.amountPaid}", fontSize = 16.sp)
                    Text(text = "Amount Remaining: ${user.amountRemaining}", fontSize = 16.sp)

                    val membershipStatus = getMembershipStatus(user)
                    Text(
                        text = "Status: $membershipStatus",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (membershipStatus == "Active") Color.Blue else Color.Red
                    )

                    user.registrationDate?.let {
                        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.toDate())
                        Text(
                            text = "Registration Date: $formattedDate",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Text(
                        text = "Membership Duration: ${user.membershipDuration ?: "N/A"} month(s)",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}