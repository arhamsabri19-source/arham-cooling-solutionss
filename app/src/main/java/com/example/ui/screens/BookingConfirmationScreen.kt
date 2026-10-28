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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.StatusCompletedColor
import com.example.ui.theme.StatusNewColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BookingConfirmationScreen(
    booking: BookingEntity,
    settings: BusinessSettingsEntity,
    language: Language,
    onReturnHome: () -> Unit,
    onTrackBooking: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostWhite)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("booking_confirmation_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Success Icon Animation / Box
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = StatusCompletedColor,
                modifier = Modifier.size(46.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (language == Language.HI) "अनुरोध सफलतापूर्वक दर्ज हुआ!" else "Request Received!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )

        Spacer(modifier = Modifier.height(12.dp))

        // The exact mandated message
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
        ) {
            Text(
                text = "Thank you for choosing Arham Cooling Solutions! Your booking request has been received. Our team will contact you to confirm your appointment.",
                fontSize = 13.sp,
                color = Color(0xFF166534),
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Booking Summary Card
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
                        Text(text = booking.id, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = DarkBlue800)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDBEAFE))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = AppStrings.statusLabel(booking.status, language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusNewColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(12.dp))

                ConfirmationRow(label = if (language == Language.HI) "ग्राहक" else "Customer", value = booking.customerName)
                ConfirmationRow(label = if (language == Language.HI) "मोबाइल" else "Mobile", value = booking.mobileNumber)
                ConfirmationRow(label = if (language == Language.HI) "सेवा" else "Service", value = booking.serviceName)
                ConfirmationRow(label = if (language == Language.HI) "दिनांक" else "Date", value = booking.preferredDate)
                ConfirmationRow(label = if (language == Language.HI) "समय स्लॉट" else "Time Slot", value = booking.preferredTimeSlot)
                ConfirmationRow(label = if (language == Language.HI) "पता" else "Address", value = "${booking.fullAddress}, ${booking.locality}")
                ConfirmationRow(label = if (language == Language.HI) "जांच शुल्क" else "Inspection Fee", value = "₹${booking.inspectionCharge}")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // WhatsApp inquiry with prefilled booking ID
        Button(
            onClick = {
                val prefilled = "Hello Arham Cooling Solutions, I have submitted a booking with ID: ${booking.id} for ${booking.serviceName} on ${booking.preferredDate} (${booking.preferredTimeSlot}). Please confirm."
                val cleanPhone = settings.whatsappNumber.replace("+", "").replace(" ", "")
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(prefilled)}")
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("whatsapp_booking_inquiry_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF25D366),
                contentColor = FrostWhite
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.HI) "व्हाट्सएप पर बुकिंग की पुष्टि पूछें" else "WhatsApp Inquiry with Booking ID",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onTrackBooking,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .testTag("track_status_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = if (language == Language.HI) "ट्रैक करें" else "Track", fontSize = 12.sp)
            }

            Button(
                onClick = onReturnHome,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .testTag("return_home_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = if (language == Language.HI) "होम पेज" else "Home", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ConfirmationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextMuted)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}
