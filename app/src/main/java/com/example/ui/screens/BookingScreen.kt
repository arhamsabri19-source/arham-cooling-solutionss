package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ServiceEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue50
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingScreen(
    services: List<ServiceEntity>,
    settings: BusinessSettingsEntity,
    initialService: ServiceEntity?,
    language: Language,
    onSubmitBooking: (
        name: String,
        mobile: String,
        category: String,
        service: ServiceEntity,
        address: String,
        locality: String,
        date: String,
        slot: String,
        problem: String,
        contactMethod: String
    ) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialService?.category ?: "AC") }

    val categoryServices = remember(services, selectedCategory) {
        services.filter { it.category == selectedCategory }
    }

    var selectedService by remember(categoryServices, initialService) {
        mutableStateOf(
            if (initialService != null && initialService.category == selectedCategory) initialService
            else categoryServices.firstOrNull()
        )
    }

    var fullAddress by remember { mutableStateOf("") }

    // Serviceable localities
    val localitiesList = remember(settings.serviceAreas) {
        settings.serviceAreas.split(",").map { it.trim() }.filter { it.isNotBlank() }
    }
    var selectedLocality by remember(localitiesList) {
        mutableStateOf(localitiesList.firstOrNull() ?: "Main City")
    }

    // Next 7 available dates
    val availableDates = remember {
        val list = mutableListOf<Pair<String, String>>() // Display Label, YYYY-MM-DD
        val cal = Calendar.getInstance()
        val displayFormat = SimpleDateFormat("EEE, dd MMM", Locale.US)
        val valueFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        for (i in 0..6) {
            val label = if (i == 0) "Today (${displayFormat.format(cal.time)})"
            else if (i == 1) "Tomorrow (${displayFormat.format(cal.time)})"
            else displayFormat.format(cal.time)
            list.add(Pair(label, valueFormat.format(cal.time)))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }
    var selectedDate by remember { mutableStateOf(availableDates.firstOrNull()?.second ?: "") }

    // Time Slots
    val timeSlots = listOf(
        "09:00 AM - 11:00 AM",
        "11:00 AM - 01:00 PM",
        "02:00 PM - 04:00 PM",
        "04:00 PM - 06:00 PM",
        "06:00 PM - 08:00 PM"
    )
    var selectedTimeSlot by remember { mutableStateOf(timeSlots[1]) }

    var problemDescription by remember { mutableStateOf("") }
    var preferredContactMethod by remember { mutableStateOf("PHONE") } // PHONE or WHATSAPP

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostWhite)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("booking_screen")
    ) {
        // Form Title & Note
        Text(
            text = if (language == Language.HI) "डोरस्टेप सर्विस बुक करें" else "Book Doorstep Service",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )
        Text(
            text = if (language == Language.HI) "खाता बनाए बिना केवल २ मिनट में तुरंत बुकिंग करें" else "Book directly in 2 minutes without account creation",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. APPLIANCE CATEGORY
        Text(
            text = if (language == Language.HI) "उपकरण चुनें (Category)" else "Select Category",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedCategory == "AC",
                onClick = {
                    selectedCategory = "AC"
                    selectedService = services.firstOrNull { it.category == "AC" }
                },
                label = { Text("Air Conditioner (AC)") },
                leadingIcon = { Icon(Icons.Default.AcUnit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkBlue800,
                    selectedLabelColor = FrostWhite,
                    selectedLeadingIconColor = FrostWhite
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("booking_category_ac")
            )
            FilterChip(
                selected = selectedCategory == "REFRIGERATOR",
                onClick = {
                    selectedCategory = "REFRIGERATOR"
                    selectedService = services.firstOrNull { it.category == "REFRIGERATOR" }
                },
                label = { Text("Refrigerator (फ्रिज)") },
                leadingIcon = { Icon(Icons.Default.Kitchen, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkBlue800,
                    selectedLabelColor = FrostWhite,
                    selectedLeadingIconColor = FrostWhite
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("booking_category_fridge")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. SPECIFIC SERVICE SELECTOR
        Text(
            text = if (language == Language.HI) "विशिष्ट सेवा चुनें (Select Service)" else "Specific Service",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))

        var serviceDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = serviceDropdownExpanded,
            onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedService?.let { if (language == Language.HI) it.nameHi else it.nameEn } ?: "Choose Service",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("booking_service_dropdown"),
                shape = RoundedCornerShape(10.dp)
            )

            ExposedDropdownMenu(
                expanded = serviceDropdownExpanded,
                onDismissRequest = { serviceDropdownExpanded = false }
            ) {
                categoryServices.forEach { s ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = if (language == Language.HI) s.nameHi else s.nameEn,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (s.startingPrice > 0) "From ₹${s.startingPrice} • Inspection: ₹${s.inspectionCharge}"
                                    else "Inspection: ₹${s.inspectionCharge}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        },
                        onClick = {
                            selectedService = s
                            serviceDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. CUSTOMER NAME & MOBILE
        Text(
            text = AppStrings.customerNameLabel(language) + " *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            placeholder = { Text(if (language == Language.HI) "उदा: राहुल शर्मा" else "e.g. Rahul Sharma") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SkyBlue500) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_customer_name_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = AppStrings.mobileNumberLabel(language) + " * (10 Digits)",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) mobileNumber = it },
            placeholder = { Text("9876543210") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SkyBlue500) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_mobile_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 4. FULL ADDRESS & LOCALITY
        Text(
            text = AppStrings.fullAddressLabel(language) + " *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullAddress,
            onValueChange = { fullAddress = it },
            placeholder = { Text(if (language == Language.HI) "मकान नं., फ्लैट, गली, नज़दीकी लैंडमार्क" else "House/Flat No, Building, Street, Landmark") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = SkyBlue500) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_address_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = AppStrings.localityLabel(language) + " *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))

        var localityDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = localityDropdownExpanded,
            onExpandedChange = { localityDropdownExpanded = !localityDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedLocality,
                onValueChange = { selectedLocality = it },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = localityDropdownExpanded) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = SkyBlue500) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("booking_locality_dropdown"),
                shape = RoundedCornerShape(10.dp)
            )

            ExposedDropdownMenu(
                expanded = localityDropdownExpanded,
                onDismissRequest = { localityDropdownExpanded = false }
            ) {
                localitiesList.forEach { loc ->
                    DropdownMenuItem(
                        text = { Text(loc) },
                        onClick = {
                            selectedLocality = loc
                            localityDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. PREFERRED DATE
        Text(
            text = AppStrings.selectDateLabel(language) + " *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableDates.forEach { (label, value) ->
                FilterChip(
                    selected = selectedDate == value,
                    onClick = { selectedDate = value },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkBlue800,
                        selectedLabelColor = FrostWhite
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. PREFERRED TIME SLOT
        Text(
            text = AppStrings.selectTimeSlotLabel(language) + " *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeSlots.forEach { slot ->
                FilterChip(
                    selected = selectedTimeSlot == slot,
                    onClick = { selectedTimeSlot = slot },
                    label = { Text(slot, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkBlue800,
                        selectedLabelColor = FrostWhite
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. PROBLEM DESCRIPTION
        Text(
            text = AppStrings.problemDescriptionLabel(language),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = problemDescription,
            onValueChange = { problemDescription = it },
            placeholder = { Text(if (language == Language.HI) "समस्या के बारे में बताएं (उदा: एसी में कूलिंग नहीं हो रही)" else "Describe issue (e.g. AC making sound, water dripping)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .testTag("booking_problem_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 8. CONTACT METHOD PREFERENCE
        Text(
            text = AppStrings.preferredContactMethodLabel(language),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue900
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { preferredContactMethod = "PHONE" }
            ) {
                RadioButton(
                    selected = preferredContactMethod == "PHONE",
                    onClick = { preferredContactMethod = "PHONE" }
                )
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = SkyBlue500)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = AppStrings.phoneCall(language), fontSize = 13.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { preferredContactMethod = "WHATSAPP" }
            ) {
                RadioButton(
                    selected = preferredContactMethod == "WHATSAPP",
                    onClick = { preferredContactMethod = "WHATSAPP" }
                )
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF25D366))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "WhatsApp", fontSize = 13.sp)
            }
        }

        // Error message card
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = errorMessage!!, color = Color(0xFF991B1B), fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                if (customerName.isBlank()) {
                    errorMessage = if (language == Language.HI) "कृपया ग्राहक का नाम दर्ज करें।" else "Please enter customer name."
                    return@Button
                }
                if (mobileNumber.length < 10) {
                    errorMessage = if (language == Language.HI) "कृपया वैध १० अंकों का मोबाइल नंबर दर्ज करें।" else "Please enter valid 10-digit mobile number."
                    return@Button
                }
                if (fullAddress.isBlank()) {
                    errorMessage = if (language == Language.HI) "कृपया पूरा पता दर्ज करें।" else "Please enter your full address."
                    return@Button
                }
                val svc = selectedService
                if (svc == null) {
                    errorMessage = "Please select a service."
                    return@Button
                }

                errorMessage = null
                isSubmitting = true
                onSubmitBooking(
                    customerName,
                    mobileNumber,
                    selectedCategory,
                    svc,
                    fullAddress,
                    selectedLocality,
                    selectedDate,
                    selectedTimeSlot,
                    problemDescription,
                    preferredContactMethod
                )
            },
            enabled = !isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_booking_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkBlue800,
                contentColor = FrostWhite
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(color = FrostWhite, modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = AppStrings.submitBookingBtn(language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
