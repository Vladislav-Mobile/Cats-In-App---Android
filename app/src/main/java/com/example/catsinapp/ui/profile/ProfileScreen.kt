package com.example.catsinapp.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catsinapp.R
import com.example.catsinapp.data.PetProfileData
import com.example.catsinapp.data.PetProfileRepository
import com.example.catsinapp.debug.AppLogger
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val YellowCard = Color(0xFFF5F7D0)
private val AmberLabel = Color(0xFF8B6914)

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = GreenDark,
    unfocusedBorderColor    = Color(0xFFDDD8C4),
    focusedLabelColor       = GreenDark,
    focusedContainerColor   = YellowCard,
    unfocusedContainerColor = YellowCard
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context      = LocalContext.current
    val profileState by PetProfileRepository.getProfile(context).collectAsState(initial = PetProfileData())

    var petName  by remember(profileState) { mutableStateOf(profileState.name)      }
    var dob      by remember(profileState) { mutableStateOf(profileState.birthDate)  }
    var weightKg by remember(profileState) { mutableStateOf(profileState.weightKg)  }
    var weightG  by remember(profileState) { mutableStateOf(profileState.weightG)   }
    var photoUri by remember(profileState) { mutableStateOf(profileState.photoUri)  }

    var isEditing      by remember { mutableStateOf(false) }
    var showSaved      by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // ЛОГ: загрузка фото при старте экрана
    LaunchedEffect(profileState.photoUri) {
        if (profileState.photoUri.isNotBlank()) {
            AppLogger.photoLoaded(success = true, uri = profileState.photoUri)
        }
    }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            photoUri = it.toString()
            // ЛОГ: новое фото выбрано
            AppLogger.photoLoaded(success = true, uri = it.toString())
        } ?: AppLogger.photoLoaded(success = false, uri = "user cancelled")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Аватар
            Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(24.dp)).background(YellowCard)
                        .then(if (isEditing) Modifier.clickable { photoLauncher.launch(arrayOf("image/*")) } else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri.isNotBlank()) {
                        AsyncImage(model = Uri.parse(photoUri), contentDescription = petName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)))
                    } else {
                        Image(painterResource(R.drawable.cat_buddy_profile), petName,
                            contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
                    }
                }
                if (isEditing) {
                    Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(GreenDark)
                        .clickable { photoLauncher.launch(arrayOf("image/*")) },
                        contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_camera), "Change photo",
                            tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("${petName}'s Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Health & Wellness Identity", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Pet Name
                FieldLabel("PET NAME")
                if (isEditing) {
                    OutlinedTextField(value = petName, onValueChange = { petName = it },
                        placeholder = { Text("Enter pet name", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = fieldColors(), singleLine = true)
                } else {
                    ReadField { Text(petName, fontSize = 16.sp, color = TextPrimary) }
                }

                // Date of Birth
                FieldLabel("DATE OF BIRTH")
                if (isEditing) {
                    Box(modifier = Modifier.fillMaxWidth()
                        .background(YellowCard, RoundedCornerShape(16.dp))
                        .clickable { showDatePicker = true }.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(painterResource(R.drawable.ic_calendar), null,
                                tint = GreenDark, modifier = Modifier.size(18.dp))
                            Text(dob.ifBlank { "Tap to select date" }, fontSize = 16.sp,
                                color = if (dob.isBlank()) TextSecondary else TextPrimary)
                        }
                    }
                } else {
                    ReadField {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(painterResource(R.drawable.ic_calendar), null,
                                tint = TextSecondary, modifier = Modifier.size(18.dp))
                            Text(dob, fontSize = 16.sp, color = TextPrimary)
                        }
                    }
                }

                // Weight
                FieldLabel("CURRENT WEIGHT")
                if (isEditing) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("KG", fontSize = 11.sp, color = TextSecondary,
                                fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                            OutlinedTextField(value = weightKg,
                                onValueChange = { weightKg = it.filter { c -> c.isDigit() || c == '.' } },
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                colors = fieldColors(), singleLine = true)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("G", fontSize = 11.sp, color = TextSecondary,
                                fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                            OutlinedTextField(value = weightG,
                                onValueChange = { weightG = it.filter { c -> c.isDigit() } },
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = fieldColors(), singleLine = true)
                        }
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WeightCard(weightKg, "KILOGRAMS (KG)", Modifier.weight(1f))
                        WeightCard(weightG,  "GRAMS (G)",      Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (isEditing) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = {
                                petName = profileState.name; dob = profileState.birthDate
                                weightKg = profileState.weightKg; weightG = profileState.weightG
                                photoUri = profileState.photoUri; isEditing = false
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark)
                        ) { Text("Cancel", fontWeight = FontWeight.SemiBold) }

                        Button(
                            onClick = {
                                PetProfileRepository.saveProfile(context, PetProfileData(
                                    name = petName, birthDate = dob,
                                    weightKg = weightKg, weightG = weightG, photoUri = photoUri
                                ))
                                // ЛОГ: сохранение профиля
                                AppLogger.profileSaved(
                                    name      = petName,
                                    dob       = dob,
                                    weightKg  = weightKg,
                                    weightG   = weightG,
                                    hasPhoto  = photoUri.isNotBlank()
                                )
                                isEditing = false; showSaved = true
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                        ) { Text("Save", color = Color.White, fontWeight = FontWeight.SemiBold) }
                    }
                } else {
                    Button(onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)) {
                        Text("Edit Profile", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                    OutlinedButton(onClick = { navController.navigate("weight_entry") },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark)) {
                        Text("Change Weight Data", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        if (showSaved) {
            LaunchedEffect(Unit) { kotlinx.coroutines.delay(2000); showSaved = false }
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
                .background(GreenDark, RoundedCornerShape(50.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text("✓  Profile saved!", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showDatePicker) {
        val dpState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { millis ->
                        dob = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }
                    showDatePicker = false
                }) { Text("OK", color = GreenDark) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = TextSecondary) } }
        ) {
            DatePicker(state = dpState, colors = DatePickerDefaults.colors(
                selectedDayContainerColor = GreenDark, todayDateBorderColor = GreenDark))
        }
    }
}

@Composable private fun FieldLabel(label: String) {
    Text(label, fontSize = 11.sp, color = AmberLabel, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
}
@Composable private fun ReadField(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(YellowCard, RoundedCornerShape(16.dp)).padding(16.dp)) { content() }
}
@Composable private fun WeightCard(value: String, unit: String, modifier: Modifier) {
    Column(modifier = modifier.background(YellowCard, RoundedCornerShape(16.dp)).padding(16.dp)) {
        Text(value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(unit, fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp)
    }
}