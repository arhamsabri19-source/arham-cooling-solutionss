package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Hvac
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.SkyBlue50
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ServiceCard(
    service: ServiceEntity,
    language: Language,
    onBookClick: (ServiceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val serviceName = if (language == Language.HI) service.nameHi else service.nameEn
    val serviceDesc = if (language == Language.HI) service.descHi else service.descEn

    val icon: ImageVector = when (service.iconKey) {
        "ac_general" -> Icons.Default.Hvac
        "ac_clean" -> Icons.Default.CleaningServices
        "ac_install" -> Icons.Default.Build
        "ac_uninstall" -> Icons.Default.Build
        "ac_repair" -> Icons.Default.ElectricBolt
        "ac_cooling" -> Icons.Default.DeviceThermostat
        "ac_water" -> Icons.Default.WaterDrop
        "ac_gas" -> Icons.Default.Speed
        "ac_refill" -> Icons.Default.Opacity
        "ac_split", "ac_window" -> Icons.Default.AcUnit
        "fridge_repair" -> Icons.Default.Build
        "fridge_cooling" -> Icons.Default.DeviceThermostat
        "fridge_water" -> Icons.Default.WaterDrop
        "fridge_start" -> Icons.Default.ElectricBolt
        "fridge_maint" -> Icons.Default.CleaningServices
        "fridge_single", "fridge_double" -> Icons.Default.Kitchen
        else -> if (service.category == "AC") Icons.Default.AcUnit else Icons.Default.Kitchen
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("service_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = FrostWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon Badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (service.category == "AC") SkyBlue100 else Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = serviceName,
                        tint = if (service.category == "AC") DarkBlue800 else Color(0xFFB45309),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (service.category == "AC") "AC" else "Refrigerator",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (service.category == "AC") SkyBlue500 else Color(0xFFD97706),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (service.category == "AC") SkyBlue50 else Color(0xFFFFFBEB))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = serviceName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = serviceDesc,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing details breakdown (Inspection vs Starting/Quote)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SkyBlue50)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (service.startingPrice > 0) {
                        Text(
                            text = AppStrings.priceStartingFrom(language, service.startingPrice),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue800
                        )
                    } else {
                        Text(
                            text = AppStrings.priceAfterInspection(language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlue800
                        )
                    }
                    Text(
                        text = AppStrings.inspectionFee(language, service.inspectionCharge),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = { onBookClick(service) },
                    modifier = Modifier.testTag("book_button_${service.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue800,
                        contentColor = FrostWhite
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text(
                        text = AppStrings.bookNowBtn(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
