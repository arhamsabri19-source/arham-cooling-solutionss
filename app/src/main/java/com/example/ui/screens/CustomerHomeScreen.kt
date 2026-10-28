package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.ServiceEntity
import com.example.ui.components.ServiceCard
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue300
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.SkyBlue50
import com.example.ui.theme.SkyBlue500
import com.example.ui.theme.StatusCompletedColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerHomeScreen(
    services: List<ServiceEntity>,
    reviews: List<ReviewEntity>,
    settings: BusinessSettingsEntity,
    language: Language,
    onBookServiceClick: (ServiceEntity?) -> Unit,
    onTrackBookingClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    val filteredServices = remember(services, selectedCategoryFilter) {
        when (selectedCategoryFilter) {
            "AC" -> services.filter { it.category == "AC" }
            "REFRIGERATOR" -> services.filter { it.category == "REFRIGERATOR" }
            else -> services
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("customer_home_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. HERO SECTION
        item {
            HeroBanner(
                language = language,
                settings = settings,
                onBookClick = { onBookServiceClick(null) },
                onCallClick = { dialPhone(context, settings.contactPhone) },
                onWhatsappClick = { openWhatsapp(context, settings.whatsappNumber, "Hello Arham Cooling Solutions, I want to inquire about home service.") },
                onTrackClick = onTrackBookingClick
            )
        }

        // 2. POPULAR SERVICES WITH CATEGORY TABS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == Language.HI) "हमारी सेवाएं" else "Our Services",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue900
                        )
                        Text(
                            text = if (language == Language.HI) "एसी व रेफ्रिजरेटर की संपूर्ण डोरस्टेप सेवा" else "Expert AC & Refrigerator Doorstep Solutions",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategoryFilter == "ALL",
                        onClick = { selectedCategoryFilter = "ALL" },
                        label = { Text(AppStrings.allServices(language), fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue800,
                            selectedLabelColor = FrostWhite
                        ),
                        modifier = Modifier.testTag("filter_all_services")
                    )
                    FilterChip(
                        selected = selectedCategoryFilter == "AC",
                        onClick = { selectedCategoryFilter = "AC" },
                        label = { Text(AppStrings.acServices(language), fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue800,
                            selectedLabelColor = FrostWhite
                        ),
                        modifier = Modifier.testTag("filter_ac_services")
                    )
                    FilterChip(
                        selected = selectedCategoryFilter == "REFRIGERATOR",
                        onClick = { selectedCategoryFilter = "REFRIGERATOR" },
                        label = { Text(AppStrings.fridgeServices(language), fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkBlue800,
                            selectedLabelColor = FrostWhite
                        ),
                        modifier = Modifier.testTag("filter_fridge_services")
                    )
                }
            }
        }

        // Services list items
        items(filteredServices, key = { it.id }) { service ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ServiceCard(
                    service = service,
                    language = language,
                    onBookClick = { onBookServiceClick(it) }
                )
            }
        }

        // 3. HOW IT WORKS SECTION
        item {
            HowItWorksSection(language = language)
        }

        // 4. WHY CHOOSE US SECTION
        item {
            WhyChooseUsSection(language = language)
        }

        // 5. CUSTOMER REVIEWS SECTION
        item {
            ReviewsSection(reviews = reviews, language = language)
        }

        // 6. FREQUENTLY ASKED QUESTIONS (FAQ)
        item {
            FaqSection(language = language, inspectionFee = settings.baseInspectionCharge)
        }

        // 7. SERVICE AREAS & CONTACT DETAILS
        item {
            ServiceAreaSection(
                settings = settings,
                language = language,
                onCall = { dialPhone(context, settings.contactPhone) },
                onWhatsapp = { openWhatsapp(context, settings.whatsappNumber, "Hello Arham Cooling Solutions!") },
                onOpenMap = { openMap(context, settings.googleMapsUrl) },
                onContactForm = onContactUsClick
            )
        }

        // 8. FOOTER
        item {
            FooterSection(
                settings = settings,
                language = language,
                onPrivacy = onPrivacyPolicyClick,
                onTerms = onTermsClick,
                onContact = onContactUsClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroBanner(
    language: Language,
    settings: BusinessSettingsEntity,
    onBookClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsappClick: () -> Unit,
    onTrackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBlue900, DarkBlue800, Color(0xFF0F366E))
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3338BDF8))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = SkyBlue300,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.tagline(language),
                        color = FrostWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Headline
            Text(
                text = AppStrings.headline(language),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FrostWhite,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = AppStrings.heroSub(language),
                fontSize = 13.sp,
                color = SkyBlue100,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Primary Booking Button
            Button(
                onClick = onBookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("hero_book_service_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyBlue500,
                    contentColor = FrostWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.bookAService(language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Quick Actions: Call Now & WhatsApp Us & Track
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCallClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("hero_call_now_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x33FFFFFF),
                        contentColor = FrostWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = AppStrings.callNow(language), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onWhatsappClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("hero_whatsapp_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = FrostWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = AppStrings.whatsappUs(language), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onTrackClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("hero_track_booking_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x33FFFFFF),
                        contentColor = FrostWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (language == Language.HI) "स्टेटस" else "Track", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun HowItWorksSection(language: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = AppStrings.howItWorksTitle(language),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )
        Text(
            text = if (language == Language.HI) "सरल और स्पष्ट ৪ चरणों में सेवा" else "Seamless service in 4 simple steps",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        val steps = listOf(
            Triple(AppStrings.step1Title(language), AppStrings.step1Desc(language), Icons.Default.Schedule),
            Triple(AppStrings.step2Title(language), AppStrings.step2Desc(language), Icons.Default.LocationOn),
            Triple(AppStrings.step3Title(language), AppStrings.step3Desc(language), Icons.Default.Handyman),
            Triple(AppStrings.step4Title(language), AppStrings.step4Desc(language), Icons.Default.Verified)
        )

        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DarkBlue800),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = FrostWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.first,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = step.second,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WhyChooseUsSection(language: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SkyBlue50)
            .padding(horizontal = 16.dp, vertical = 22.dp)
    ) {
        Text(
            text = AppStrings.whyChooseUsTitle(language),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )
        Text(
            text = if (language == Language.HI) "आधुनिक उपकरण और प्रामाणिक कार्यशैली" else "Customer trust built on quality and transparency",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        val reasons = listOf(
            Pair(AppStrings.reason1Title(language), AppStrings.reason1Desc(language)),
            Pair(AppStrings.reason2Title(language), AppStrings.reason2Desc(language)),
            Pair(AppStrings.reason3Title(language), AppStrings.reason3Desc(language)),
            Pair(AppStrings.reason4Title(language), AppStrings.reason4Desc(language))
        )

        reasons.forEach { (title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FrostWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SkyBlue100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SkyBlue500,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue900
                        )
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewsSection(reviews: List<ReviewEntity>, language: Language) {
    if (reviews.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AppStrings.reviewsTitle(language),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue900
                )
                Text(
                    text = if (language == Language.HI) "सत्यापित ग्राहकों के अनुभव" else "Real feedback from verified home service visits",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        reviews.take(3).forEach { review ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FrostWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = review.customerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue900
                        )
                        Row {
                            repeat(review.rating) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${review.serviceName} • ${review.dateStr}",
                        fontSize = 11.sp,
                        color = SkyBlue500
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = review.reviewText,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FaqSection(language: Language, inspectionFee: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SkyBlue50)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = AppStrings.faqsTitle(language),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )

        Spacer(modifier = Modifier.height(12.dp))

        val faqs = if (language == Language.HI) listOf(
            Pair("जांच शुल्क (Inspection Charge) क्या है?", "हमारा बेस डायग्नोस्टिक व विजिट चार्ज ₹$inspectionFee है। तकनीशियन उपकरण की जांच कर काम शुरू करने से पहले सटीक खर्च बताएंगे।"),
            Pair("बुकिंग के बाद तकनीशियन कब तक आएंगे?", "आपके चुने हुए समय स्लॉट के भीतर तकनीशियन पहुंचेंगे। आने से 15-30 मिनट पूर्व आपको कॉल किया जाएगा।"),
            Pair("क्या बदले गए स्पेयर पार्ट्स पर वारंटी मिलती है?", "हाँ, बदले गए सभी ओरिजिनल स्पेयर पार्ट्स और रिपेयर कार्य पर 30 दिन की सर्विस वारंटी दी जाती है।"),
            Pair("भुगतान कैसे कर सकते हैं?", "काम पूरा और संतोषजनक होने के बाद आप नकद (Cash) या यूपीआई (GPay/PhonePe/Paytm) द्वारा भुगतान कर सकते हैं।")
        ) else listOf(
            Pair("What is the inspection charge?", "Our standard diagnostic inspection charge is ₹$inspectionFee. The technician inspects the appliance and gives an accurate repair quote before commencing work."),
            Pair("How soon will the technician arrive?", "Technicians arrive within your selected time slot. They will call you 15-30 minutes prior to arrival."),
            Pair("Do you provide warranty on spare parts?", "Yes, we provide a 30-day warranty on all original spare parts replaced and the specific repair performed."),
            Pair("How can I pay for the service?", "You can pay via Cash or UPI (Google Pay, PhonePe, Paytm) after the service is completed to your satisfaction.")
        )

        faqs.forEach { (question, answer) ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = FrostWhite)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = question,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlue900,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = SkyBlue500
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = answer,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceAreaSection(
    settings: BusinessSettingsEntity,
    language: Language,
    onCall: () -> Unit,
    onWhatsapp: () -> Unit,
    onOpenMap: () -> Unit,
    onContactForm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = AppStrings.serviceAreasTitle(language),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue900
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = FrostWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = SkyBlue500, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.HI) "सेवा क्षेत्र (Serviceable Areas):" else "Serviceable Localities:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue900
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = settings.serviceAreas,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = SkyBlue500, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.HI) "कार्य समय: सुबह 08:30 से रात 08:30 (सातों दिन)" else "Working Hours: 08:30 AM - 08:30 PM (All 7 Days)",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenMap,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (language == Language.HI) "लोकेशन" else "Map", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onContactForm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (language == Language.HI) "पूछताछ" else "Inquire", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun FooterSection(
    settings: BusinessSettingsEntity,
    language: Language,
    onPrivacy: () -> Unit,
    onTerms: () -> Unit,
    onContact: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBlue900)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = settings.businessName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = FrostWhite
        )
        Text(
            text = AppStrings.tagline(language),
            fontSize = 11.sp,
            color = SkyBlue300
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = settings.businessAddress,
            fontSize = 10.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == Language.HI) "गोपनीयता नीति" else "Privacy Policy",
                fontSize = 11.sp,
                color = SkyBlue400,
                modifier = Modifier.clickable { onPrivacy() }
            )
            Text(text = "•", color = TextMuted)
            Text(
                text = if (language == Language.HI) "नियम व शर्तें" else "Terms & Conditions",
                fontSize = 11.sp,
                color = SkyBlue400,
                modifier = Modifier.clickable { onTerms() }
            )
            Text(text = "•", color = TextMuted)
            Text(
                text = if (language == Language.HI) "संपर्क करें" else "Contact Us",
                fontSize = 11.sp,
                color = SkyBlue400,
                modifier = Modifier.clickable { onContact() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "© 2026 Arham Cooling Solutions. All rights reserved.",
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}

private fun dialPhone(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
        context.startActivity(intent)
    } catch (_: Exception) {}
}

private fun openWhatsapp(context: Context, phone: String, message: String) {
    try {
        val cleanPhone = phone.replace("+", "").replace(" ", "")
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (_: Exception) {}
}

private fun openMap(context: Context, mapUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
