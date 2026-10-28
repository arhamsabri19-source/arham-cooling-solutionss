package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.StatusCompletedColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InvoiceView(
    booking: BookingEntity,
    settings: BusinessSettingsEntity,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val invoiceDate = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date(booking.createdAt))
    val isPaid = booking.paymentStatus == "PAID"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("invoice_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FrostWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Company Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkBlue800),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = null,
                                tint = FrostWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = settings.businessName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue900
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = settings.taglineEn,
                        fontSize = 11.sp,
                        color = SkyBlue500
                    )
                    Text(
                        text = "${settings.contactPhone} | ${settings.businessEmail}",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                // Invoice Status Stamp
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPaid) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isPaid) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = if (isPaid) StatusCompletedColor else Color(0xFFD97706),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPaid) "PAID" else "PENDING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPaid) StatusCompletedColor else Color(0xFFD97706)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Meta Info: Invoice ID & Customer Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "INVOICE TO:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(text = booking.customerName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(text = "Phone: ${booking.mobileNumber}", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "${booking.fullAddress}, ${booking.locality}", fontSize = 11.sp, color = TextSecondary, maxLines = 2)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "INVOICE NO:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(text = "INV-${booking.id}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBlue800)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "DATE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(text = invoiceDate, fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!booking.assignedTechnicianName.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SkyBlue100)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Serviced by: ${booking.assignedTechnicianName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue900
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Service item table header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "AMOUNT (₹)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Breakdown Rows
            InvoiceRow(label = "${booking.serviceName} (Diagnostic / Inspection)", amount = booking.inspectionCharge)

            if (booking.repairCharge > 0) {
                InvoiceRow(label = "Repair & Labor Service", amount = booking.repairCharge)
            }

            if (booking.partsCharge > 0) {
                InvoiceRow(label = "Genuine Spare Parts / Refrigerant Gas", amount = booking.partsCharge)
            }

            if (booking.discountAmount > 0) {
                InvoiceRow(label = "Discount", amount = -booking.discountAmount, isDiscount = true)
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFCBD5E1))
            Spacer(modifier = Modifier.height(8.dp))

            // Grand Total Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL AMOUNT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue900
                )
                Text(
                    text = "₹${booking.totalAmount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue800
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Payment Mode: ${booking.paymentMethod}", fontSize = 11.sp, color = TextSecondary)
                Text(text = "Status: ${booking.paymentStatus}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (isPaid) StatusCompletedColor else Color(0xFFD97706))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer note
            Text(
                text = "Thank you for trusting Arham Cooling Solutions. 30 days warranty on replaced parts.",
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Share & Close Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close")
                }

                Button(
                    onClick = {
                        shareInvoice(context, booking, settings)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }
            }
        }
    }
}

@Composable
private fun InvoiceRow(label: String, amount: Int, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (isDiscount) "-₹${kotlin.math.abs(amount)}" else "₹$amount",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDiscount) Color(0xFF16A34A) else TextPrimary
        )
    }
}

private fun shareInvoice(context: Context, booking: BookingEntity, settings: BusinessSettingsEntity) {
    val text = buildString {
        appendLine("===== ${settings.businessName} =====")
        appendLine("INVOICE: INV-${booking.id}")
        appendLine("Customer: ${booking.customerName}")
        appendLine("Mobile: ${booking.mobileNumber}")
        appendLine("Address: ${booking.fullAddress}, ${booking.locality}")
        appendLine("Service: ${booking.serviceName}")
        if (!booking.assignedTechnicianName.isNullOrBlank()) {
            appendLine("Technician: ${booking.assignedTechnicianName}")
        }
        appendLine("-----------------------------")
        appendLine("Inspection: ₹${booking.inspectionCharge}")
        if (booking.repairCharge > 0) appendLine("Repair/Labor: ₹${booking.repairCharge}")
        if (booking.partsCharge > 0) appendLine("Parts/Gas: ₹${booking.partsCharge}")
        if (booking.discountAmount > 0) appendLine("Discount: -₹${booking.discountAmount}")
        appendLine("TOTAL: ₹${booking.totalAmount}")
        appendLine("Payment: ${booking.paymentStatus} (${booking.paymentMethod})")
        appendLine("-----------------------------")
        appendLine("Helpline: ${settings.contactPhone}")
        appendLine("Thank you for your business!")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Service Invoice - ${booking.id}")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Invoice via"))
}
