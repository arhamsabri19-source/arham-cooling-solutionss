package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppHeader
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.BookingConfirmationScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.ContactUsDialog
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.PolicyDialog
import com.example.ui.screens.TrackBookingScreen
import com.example.ui.theme.ArhamCoolingTheme
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.SurfaceLight
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.CoolingViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArhamCoolingTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(coolingViewModel: CoolingViewModel = viewModel()) {
    val currentScreen by coolingViewModel.currentScreen.collectAsState()
    val language by coolingViewModel.language.collectAsState()
    val visibleServices by coolingViewModel.visibleServices.collectAsState()
    val allServices by coolingViewModel.allServices.collectAsState()
    val allBookings by coolingViewModel.allBookings.collectAsState()
    val settings by coolingViewModel.businessSettings.collectAsState()
    val technicians by coolingViewModel.allTechnicians.collectAsState()
    val reviews by coolingViewModel.approvedReviews.collectAsState()
    val messages by coolingViewModel.contactMessages.collectAsState()
    val selectedServiceForBooking by coolingViewModel.selectedServiceForBooking.collectAsState()
    val lastSubmittedBooking by coolingViewModel.lastSubmittedBooking.collectAsState()
    val activeAdminBooking by coolingViewModel.activeAdminBooking.collectAsState()
    val activeBookingHistory by coolingViewModel.activeBookingHistory.collectAsState()
    val isAdminLoggedIn by coolingViewModel.isAdminLoggedIn.collectAsState()

    val snackbarHostState = remember { SnackbarHostStateState() }
    val scope = rememberCoroutineScope()

    var showContactDialog by remember { mutableStateOf(false) }
    var policyDialogTitle by remember { mutableStateOf<String?>(null) }
    var policyDialogContent by remember { mutableStateOf("") }

    val newBookingsCount = remember(allBookings) {
        allBookings.count { it.status == "NEW" }
    }

    // Handle system back navigation
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        when (currentScreen) {
            AppScreen.ADMIN_DASHBOARD -> coolingViewModel.adminLogout()
            else -> coolingViewModel.navigateTo(AppScreen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            if (currentScreen != AppScreen.ADMIN_DASHBOARD) {
                AppHeader(
                    language = language,
                    onToggleLanguage = { coolingViewModel.toggleLanguage() },
                    onAdminClick = {
                        if (isAdminLoggedIn) {
                            coolingViewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
                        } else {
                            coolingViewModel.navigateTo(AppScreen.ADMIN_LOGIN)
                        }
                    },
                    showBackButton = currentScreen != AppScreen.HOME,
                    onBackClick = { coolingViewModel.navigateTo(AppScreen.HOME) },
                    titleOverride = when (currentScreen) {
                        AppScreen.BOOKING -> if (language == com.example.ui.strings.Language.HI) "सर्विस बुकिंग" else "Service Booking"
                        AppScreen.CONFIRMATION -> if (language == com.example.ui.strings.Language.HI) "बुकिंग पुष्टि" else "Booking Confirmed"
                        AppScreen.TRACK -> if (language == com.example.ui.strings.Language.HI) "बुकिंग ट्रैक करें" else "Track Booking"
                        AppScreen.ADMIN_LOGIN -> if (language == com.example.ui.strings.Language.HI) "एडमिन लॉगिन" else "Admin Login"
                        else -> null
                    },
                    isAdminLoggedIn = isAdminLoggedIn,
                    newBookingsCount = newBookingsCount
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SurfaceLight
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            // Adaptive container width for tablets / foldables
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
            ) {
                when (currentScreen) {
                    AppScreen.HOME -> {
                        CustomerHomeScreen(
                            services = visibleServices,
                            reviews = reviews,
                            settings = settings,
                            language = language,
                            onBookServiceClick = { service ->
                                coolingViewModel.startBooking(service)
                            },
                            onTrackBookingClick = {
                                coolingViewModel.navigateTo(AppScreen.TRACK)
                            },
                            onContactUsClick = {
                                showContactDialog = true
                            },
                            onPrivacyPolicyClick = {
                                policyDialogTitle = if (language == com.example.ui.strings.Language.HI) "गोपनीयता नीति (Privacy Policy)" else "Privacy Policy"
                                policyDialogContent = settings.privacyPolicyText
                            },
                            onTermsClick = {
                                policyDialogTitle = if (language == com.example.ui.strings.Language.HI) "नियम व शर्तें (Terms & Conditions)" else "Terms & Conditions"
                                policyDialogContent = settings.termsText
                            }
                        )
                    }

                    AppScreen.BOOKING -> {
                        BookingScreen(
                            services = visibleServices,
                            settings = settings,
                            initialService = selectedServiceForBooking,
                            language = language,
                            onSubmitBooking = { name, mobile, category, service, address, locality, date, slot, problem, contactMethod ->
                                coolingViewModel.createBooking(
                                    name = name,
                                    mobile = mobile,
                                    category = category,
                                    service = service,
                                    fullAddress = address,
                                    locality = locality,
                                    preferredDate = date,
                                    preferredTimeSlot = slot,
                                    problemDesc = problem,
                                    contactMethod = contactMethod,
                                    onSuccess = {},
                                    onError = { error ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(error)
                                        }
                                    }
                                )
                            }
                        )
                    }

                    AppScreen.CONFIRMATION -> {
                        val booking = lastSubmittedBooking
                        if (booking != null) {
                            BookingConfirmationScreen(
                                booking = booking,
                                settings = settings,
                                language = language,
                                onReturnHome = { coolingViewModel.navigateTo(AppScreen.HOME) },
                                onTrackBooking = { coolingViewModel.navigateTo(AppScreen.TRACK) }
                            )
                        } else {
                            coolingViewModel.navigateTo(AppScreen.HOME)
                        }
                    }

                    AppScreen.TRACK -> {
                        TrackBookingScreen(
                            bookings = allBookings,
                            settings = settings,
                            language = language
                        )
                    }

                    AppScreen.ADMIN_LOGIN -> {
                        AdminLoginScreen(
                            language = language,
                            onLogin = { pin ->
                                coolingViewModel.adminLogin(pin)
                            },
                            onBack = {
                                coolingViewModel.navigateTo(AppScreen.HOME)
                            }
                        )
                    }

                    AppScreen.ADMIN_DASHBOARD -> {
                        AdminDashboardScreen(
                            bookings = allBookings,
                            services = allServices,
                            technicians = technicians,
                            settings = settings,
                            messages = messages,
                            activeBooking = activeAdminBooking,
                            activeHistory = activeBookingHistory,
                            language = language,
                            onSelectBooking = { coolingViewModel.selectAdminBooking(it) },
                            onUpdateStatus = { bookingId, newStatus, note, techId, techName ->
                                coolingViewModel.updateBookingStatus(bookingId, newStatus, note, techId, techName)
                            },
                            onUpdateBilling = { bookingId, insp, rep, parts, disc, payMethod, payStatus ->
                                coolingViewModel.updateBookingBilling(bookingId, insp, rep, parts, disc, payMethod, payStatus)
                            },
                            onUpdateNotes = { bookingId, notes ->
                                coolingViewModel.updateBookingNotes(bookingId, notes)
                            },
                            onReschedule = { bookingId, newDate, newSlot, note ->
                                coolingViewModel.rescheduleBooking(bookingId, newDate, newSlot, note)
                            },
                            onCancel = { bookingId, reason ->
                                coolingViewModel.cancelBooking(bookingId, reason)
                            },
                            onAddService = { coolingViewModel.addService(it) },
                            onUpdateService = { coolingViewModel.updateService(it) },
                            onToggleServiceVisibility = { coolingViewModel.toggleServiceVisibility(it) },
                            onDeleteService = { coolingViewModel.deleteService(it) },
                            onAddTechnician = { name, phone, spec -> coolingViewModel.addTechnician(name, phone, spec) },
                            onDeleteTechnician = { coolingViewModel.deleteTechnician(it) },
                            onUpdateSettings = { coolingViewModel.updateSettings(it) },
                            onExportCsv = { coolingViewModel.exportBookingsCsv(it) },
                            onLogout = { coolingViewModel.adminLogout() }
                        )
                    }
                }
            }
        }
    }

    if (showContactDialog) {
        ContactUsDialog(
            settings = settings,
            language = language,
            onDismiss = { showContactDialog = false },
            onSubmitMessage = { name, mobile, message ->
                coolingViewModel.submitContactMessage(name, mobile, message) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (language == com.example.ui.strings.Language.HI) "पूछताछ संदेश सफलतापूर्वक भेजा गया!" else "Message sent successfully!"
                        )
                    }
                }
            }
        )
    }

    if (policyDialogTitle != null) {
        PolicyDialog(
            title = policyDialogTitle!!,
            content = policyDialogContent,
            onDismiss = { policyDialogTitle = null }
        )
    }
}

private fun SnackbarHostStateState() = SnackbarHostState()
