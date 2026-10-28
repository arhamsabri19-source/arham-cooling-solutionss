package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue300
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.SkyBlue500

@Composable
fun AppHeader(
    language: Language,
    onToggleLanguage: () -> Unit,
    onAdminClick: () -> Unit,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    titleOverride: String? = null,
    isAdminLoggedIn: Boolean = false,
    newBookingsCount: Int = 0
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkBlue900,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkBlue900, DarkBlue800)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .testTag("header_back_button")
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = FrostWhite
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        // Brand Logo Icon with Snowflake / Cooling Aura
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(SkyBlue500, DarkBlue900)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "Arham Cooling Logo",
                                tint = FrostWhite,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Text(
                            text = titleOverride ?: AppStrings.brandName(language),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrostWhite,
                            maxLines = 1
                        )
                        if (titleOverride == null) {
                            Text(
                                text = AppStrings.tagline(language),
                                fontSize = 11.sp,
                                color = SkyBlue300,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Action Controls: Language Toggle & Admin Portal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Language Switcher Badge Button
                    Box(
                        modifier = Modifier
                            .testTag("language_toggle_button")
                            .clip(RoundedCornerShape(20.dp))
                            .background(DarkBlue800)
                            .clickable { onToggleLanguage() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Change Language",
                                tint = SkyBlue400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.HI) "हिंदी" else "ENG",
                                color = FrostWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Admin Portal Button
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .testTag("admin_portal_button")
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isAdminLoggedIn) SkyBlue500 else Color(0x33FFFFFF))
                    ) {
                        if (newBookingsCount > 0 && isAdminLoggedIn) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Color.Red,
                                        contentColor = FrostWhite
                                    ) {
                                        Text("$newBookingsCount")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Admin Portal",
                                    tint = FrostWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                                contentDescription = "Admin Portal",
                                tint = FrostWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
