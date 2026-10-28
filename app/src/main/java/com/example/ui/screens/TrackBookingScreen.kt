package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue50
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.StatusAssignedColor
import com.example.ui.theme.StatusCompletedColor
import com.example.ui.theme.StatusConfirmedColor
import com.example.ui.theme.StatusNewColor
import com.example.ui.theme.StatusProgressColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TrackBookingScreen(
    bookings: List<BookingEntity>,
    settings: BusinessSettingsEntity,
    language: Language
) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val searchResults = remember(bookings, searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            emptyList()
        } else {
            val q = searchQuery.trim().lowercase()
            bookings.filter {
                it.id.lowercase().contains(q) || it.mobileNumber.contains(q)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBlue50)
            .padding(16.dp)
            .testTag("track_booking_screen")
    ) {
        item {
            Text(
                text = if (language == Language.HI) "अपनी सर्विस बुकिंग ट्रैक करें" else "Track Your Service Booking",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue900
            )
            Text(
                text = if (language == Language.HI) "बुकिंग आईडी (उदा: ACS-202610-8492) या मोबाइल नंबर दर्ज करें" else "Enter Booking ID or registered 10-digit mobile number",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("e.g. ACS-202610-8492 or 9876543210") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SkyBlue500) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track_search_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (searchQuery.isNotBlank() && searchResults.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = FrostWhite),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == Language.HI) "कोई बुकिंग नहीं मिली।" else "No booking found matching this search.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        items(searchResults, key = { it.id }) { booking ->
            TrackBookingResultCard(
                booking = booking,
                settings = settings,
                language = language,
                onCallHelpline = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.contactPhone.replace(" ", "")}"))
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TrackBookingResultCard(
    booking: BookingEntity,
    settings: BusinessSettingsEntity,
    language: Language,
    onCallHelpline: () -> Unit
) {
    val statusColor = when (booking.status) {
        "NEW" -> StatusNewColor
        "CONFIRMED" -> StatusConfirmedColor
        "TECHNICIAN_ASSIGNED" -> StatusAssignedColor
        "IN_PROGRESS" -> StatusProgressColor
        "COMPLETED" -> StatusCompletedColor
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FrostWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "BOOKING ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(text = booking.id, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue900)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = AppStrings.statusLabel(booking.status, language),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = booking.serviceName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(
                text = "${booking.preferredDate} • Slot: ${booking.preferredTimeSlot}",
                fontSize = 12.sp,
                color = SkyBlue500
            )

            if (!booking.assignedTechnicianName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SkyBlue100)
                        .padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Engineering, contentDescription = null, tint = DarkBlue800, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Assigned Technician: ${booking.assignedTechnicianName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue900
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ₹${booking.totalAmount} (${booking.paymentStatus})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue800
                )

                Button(
                    onClick = onCallHelpline,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Helpline", fontSize = 11.sp)
                }
            }
        }
    }
}
