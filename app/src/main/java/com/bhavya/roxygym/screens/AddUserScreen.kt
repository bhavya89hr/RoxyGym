package com.bhavya.roxygym.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(navController: NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedGender by remember { mutableStateOf("Male") }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var registrationDate by remember { mutableStateOf<Date?>(null) }
    var membershipDuration by remember { mutableStateOf(1) }  // Default to 1 month
    var membershipStatus by remember { mutableStateOf("") }
    var amountPaid by remember { mutableStateOf("") }
    var amountRemaining by remember { mutableStateOf("") }


    // Camera capture launcher
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap
            Toast.makeText(context, "Image Captured!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera permission request launcher
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            takePictureLauncher.launch()
        } else {
            Toast.makeText(context, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to calculate membership status
    fun getMembershipStatus(user: FirestoreUsers): String {
        val currentDate = Date()

        if (user.registrationDate != null && user.membershipDuration != null) {
            val calendar = Calendar.getInstance()

            // Convert Timestamp to Date before using it
            calendar.time = user.registrationDate.toDate()
            calendar.add(Calendar.MONTH, user.membershipDuration) // Add membership duration
            val expirationDate = calendar.time

            return if (currentDate.before(expirationDate)) "Active" else "Expired"
        }
        return "No Membership Info"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Register", color = Color.Black,
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                takePictureLauncher.launch()
                            } else {
                                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                        .align(Alignment.CenterHorizontally)
                ) {
                    imageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text(
                        text = "Tap to Capture",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(15.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Member Name") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone No.") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))
                OutlinedTextField(value = amountPaid, onValueChange = { amountPaid = it }, label = { Text("Amount Paid") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))
                OutlinedTextField(value = amountRemaining, onValueChange = { amountRemaining = it }, label = { Text("Amount Remaining") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp))

                Row(modifier = Modifier.padding(10.dp)) {
                    Text("Select Gender :", style = MaterialTheme.typography.titleMedium)
                    GenderRadioButton(
                        selectedGender = selectedGender,
                        onGenderSelected = { selectedGender = it }
                    )
                }

                Spacer(Modifier.height(15.dp))

                // Membership duration selection (1 month, 2 months, etc.)
                OutlinedTextField(
                    value = membershipDuration.toString(),
                    onValueChange = { membershipDuration = it.toIntOrNull() ?: 1 },
                    label = { Text("Membership Duration (Months)") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                )

                Spacer(Modifier.height(15.dp))

                RegisterButton(
                    isLoading = isLoading,
                    onClick = {
                        Log.d("RegisterButton", "Register button clicked")
                        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && imageBitmap != null) {
                            isLoading = true

                            // Convert Bitmap to Firebase-friendly format
                            val baos = ByteArrayOutputStream()
                            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val imageData = baos.toByteArray()

                            val userId = FirebaseFirestore.getInstance().collection("users").document().id
                            Log.d("Firestore", "Generated User ID: $userId")
                            val storageRef = FirebaseStorage.getInstance().reference.child("user_images/$userId.jpg")

                            val uploadTask = storageRef.putBytes(imageData)
                            uploadTask.addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                    // Save user info to Firestore
                                    val userData = mapOf(
                                        "name" to name,
                                        "email" to email,
                                        "phone" to phone,
                                        "address" to address,
                                        "gender" to selectedGender,
                                        "amountPaid" to amountPaid,
                                        "amountRemaining" to amountRemaining,
                                        "registrationDate" to FieldValue.serverTimestamp(),  // Store timestamp
                                        "membershipDuration" to membershipDuration,
                                        "imageUrl" to downloadUri.toString()
                                    )
                                    FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(userId)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            registrationDate = Date()  // Set registration date to current date
                                            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate("HomeScreen")
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        }
                    }
                )

                registrationDate?.let {
                    Text(
                        text = "Registration Date: ${SimpleDateFormat("yyyy-MM-dd").format(it)}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = membershipStatus,
                        color = if (membershipStatus == "Membership Active") Color.Green else Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun CameraImageUpload() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Image is captured and stored
        }
    }

    // Function to create image file and return URI
    fun getTempImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
        }


        val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        return androidx.core.content.FileProvider.getUriForFile(
            context, "${context.packageName}.provider", file
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image placeholder or captured image
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    val uri = getTempImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
        ) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Text(
                text = "Tap to Capture",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Upload Button
        Button(onClick = {
            imageUri?.let { uri ->
                // Handle image upload logic here
                println("Uploading image: $uri")
            }
        }) {
            Text("Upload Image")
        }
    }
}
@Composable
fun RegisterButton(onClick: () -> Unit, isLoading:Boolean) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        onClick = onClick,
        enabled =

        !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))

        } else {
            Text(text = "Register", style = MaterialTheme.typography.bodyLarge)
        }
    }

}

@Composable
fun GenderRadioButton(selectedGender: String,
                      onGenderSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        listOf("Male", "Female").forEach { gender ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onGenderSelected(gender) }
            ) {
                RadioButton(
                    colors = RadioButtonColors(selectedColor= Color.Red, unselectedColor = Color.Gray,
                        disabledSelectedColor = Color.Gray, disabledUnselectedColor = Color.Gray),
                    selected = selectedGender == gender,
                    onClick = { onGenderSelected(gender) }
                )
                Text(text = gender, modifier = Modifier.padding(start = 4.dp))
            }
        }}

}
