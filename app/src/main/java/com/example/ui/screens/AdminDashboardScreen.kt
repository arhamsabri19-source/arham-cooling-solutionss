package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatusHistoryEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ContactMessageEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.TechnicianEntity
import com.example.ui.components.InvoiceView
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.CashAmber
import com.example.ui.theme.DarkBlue800
import com.example.ui.theme.DarkBlue900
import com.example.ui.theme.FrostWhite
import com.example.ui.theme.SkyBlue100
import com.example.ui.theme.SkyBlue400
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
import com.example.ui.theme.UpiGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    bookings: List<BookingEntity>,
    services: List<ServiceEntity>,
    technicians: List<TechnicianEntity>,
    settings: BusinessSettingsEntity,
    messages: List<ContactMessageEntity>,
    activeBooking: BookingEntity?,
    activeHistory: List<BookingStatusHistoryEntity>,
    language: Language,
    onSelectBooking: (BookingEntity?) -> Unit,
    onUpdateStatus: (bookingId: String, newStatus: String, note: String, techId: Long?, techName: String?) -> Unit,
    onUpdateBilling: (bookingId: String, inspection: Int, repair: Int, parts: Int, discount: Int, payMethod: String, payStatus: String) -> Unit,
    onUpdateNotes: (bookingId: String, notes: String) -> Unit,
    onReschedule: (bookingId: String, newDate: String, newSlot: String, note: String) -> Unit,
    onCancel: (bookingId: String, reason: String) -> Unit,
    onAddService: (ServiceEntity) -> Unit,
    onUpdateService: (ServiceEntity) -> Unit,
    onToggleServiceVisibility: (ServiceEntity) -> Unit,
    onDeleteService: (ServiceEntity) -> Unit,
    onAddTechnician: (name: String, phone: String, specialty: String) -> Unit,
    onDeleteTechnician: (TechnicianEntity) -> Unit,
    onUpdateSettings: (BusinessSettingsEntity) -> Unit,
    onExportCsv: (List<BookingEntity>) -> String,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview & Bookings, 1: Services, 2: Technicians, 3: Settings, 4: Inquiries

    // Filter & Search states
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("ALL") }
    var categoryFilter by remember { mutableStateOf("ALL") }

    // Dialog states
    var showInvoiceDialog by remember { mutableStateOf(false) }
    var showBookingDetailDialog by remember { mutableStateOf(false) }
    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddTechnicianDialog by remember { mutableStateOf(false) }

    // Metrics calculations
    val todayDateStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val totalBookings = bookings.size
    val newRequests = bookings.count { it.status == "NEW" }
    val confirmedBookings = bookings.count { it.status == "CONFIRMED" }
    val todayBookings = bookings.count { it.preferredDate == todayDateStr }
    val inProgressJobs = bookings.count { it.status == "IN_PROGRESS" || it.status == "TECHNICIAN_ASSIGNED" }
    val completedJobs = bookings.count { it.status == "COMPLETED" }
    val cancelledBookings = bookings.count { it.status == "CANCELLED" }
    // Total revenue strictly from recorded payments
    val totalRevenue = bookings.filter { it.paymentStatus == "PAID" }.sumOf { it.totalAmount }

    // Filtered bookings list
    val filteredBookings = remember(bookings, searchQuery, statusFilter, categoryFilter) {
        bookings.filter { b ->
            val matchQuery = searchQuery.isBlank() ||
                b.id.contains(searchQuery, ignoreCase = true) ||
                b.mobileNumber.contains(searchQuery) ||
                b.customerName.contains(searchQuery, ignoreCase = true)
            val matchStatus = statusFilter == "ALL" || b.status == statusFilter
            val matchCategory = categoryFilter == "ALL" || b.category == categoryFilter
            matchQuery && matchStatus && matchCategory
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBlue900)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Admin Control Portal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FrostWhite)
                Text(text = settings.businessName, fontSize = 11.sp, color = SkyBlue400)
            }

            IconButton(
                onClick = onLogout,
                modifier = Modifier.testTag("admin_logout_button")
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = FrostWhite)
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBlue800,
            contentColor = FrostWhite,
            edgePadding = 8.dp
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Bookings (${bookings.size})", fontSize = 12.sp) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Services (${services.size})", fontSize = 12.sp) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Team (${technicians.size})", fontSize = 12.sp) })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Business Settings", fontSize = 12.sp) })
            Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("Inquiries (${messages.size})", fontSize = 12.sp) })
        }

        when (selectedTab) {
            0 -> {
                // BOOKINGS TAB: Metrics + Filter/Search + Bookings List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metrics Grid
                    item {
                        Text(text = "BUSINESS OVERVIEW", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MetricCard(title = "Total Bookings", value = "$totalBookings", color = DarkBlue800)
                            MetricCard(title = "New Requests", value = "$newRequests", color = StatusNewColor, isAlert = newRequests > 0)
                            MetricCard(title = "Confirmed", value = "$confirmedBookings", color = StatusConfirmedColor)
                            MetricCard(title = "Today's Jobs", value = "$todayBookings", color = SkyBlue500)
                            MetricCard(title = "In-Progress", value = "$inProgressJobs", color = StatusProgressColor)
                            MetricCard(title = "Completed", value = "$completedJobs", color = StatusCompletedColor)
                            MetricCard(title = "Cancelled", value = "$cancelledBookings", color = Color(0xFFDC2626))
                            MetricCard(title = "Revenue (Paid)", value = "₹$totalRevenue", color = UpiGreen)
                        }
                    }

                    // Export to CSV & Search/Filter Controls
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "MANAGE BOOKINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)

                            Button(
                                onClick = {
                                    val csvContent = onExportCsv(filteredBookings)
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, csvContent)
                                        type = "text/csv"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Export Bookings CSV"))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export CSV", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by ID or Mobile...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_search_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status Filter Chips
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val statuses = listOf("ALL", "NEW", "CONFIRMED", "TECHNICIAN_ASSIGNED", "IN_PROGRESS", "COMPLETED", "RESCHEDULED", "CANCELLED")
                            statuses.forEach { st ->
                                FilterChip(
                                    selected = statusFilter == st,
                                    onClick = { statusFilter = st },
                                    label = { Text(if (st == "ALL") "All Status" else st, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    // Bookings List Items
                    items(filteredBookings, key = { it.id }) { booking ->
                        AdminBookingCard(
                            booking = booking,
                            language = language,
                            onClick = {
                                onSelectBooking(booking)
                                showBookingDetailDialog = true
                            }
                        )
                    }

                    if (filteredBookings.isEmpty()) {
                        item {
                            Text(
                                text = "No bookings found for the selected criteria.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            1 -> {
                // SERVICES MANAGEMENT TAB
                ServicesManagementTab(
                    services = services,
                    onAddService = { showAddServiceDialog = true },
                    onToggleVisibility = onToggleServiceVisibility,
                    onDelete = onDeleteService,
                    onEdit = onUpdateService
                )
            }

            2 -> {
                // TECHNICIANS MANAGEMENT TAB
                TechniciansManagementTab(
                    technicians = technicians,
                    onAddTechnician = { showAddTechnicianDialog = true },
                    onDelete = onDeleteTechnician
                )
            }

            3 -> {
                // BUSINESS SETTINGS TAB
                BusinessSettingsTab(
                    settings = settings,
                    onSave = onUpdateSettings
                )
            }

            4 -> {
                // INQUIRIES TAB
                InquiriesTab(messages = messages)
            }
        }
    }

    // Detail & Action Dialog for Selected Booking
    if (showBookingDetailDialog && activeBooking != null) {
        BookingDetailAdminDialog(
            booking = activeBooking,
            history = activeHistory,
            technicians = technicians,
            settings = settings,
            language = language,
            onDismiss = {
                showBookingDetailDialog = false
                onSelectBooking(null)
            },
            onUpdateStatus = onUpdateStatus,
            onUpdateBilling = onUpdateBilling,
            onUpdateNotes = onUpdateNotes,
            onReschedule = onReschedule,
            onCancel = onCancel,
            onViewInvoice = {
                showInvoiceDialog = true
            }
        )
    }

    // Invoice Dialog
    if (showInvoiceDialog && activeBooking != null) {
        AlertDialog(
            onDismissRequest = { showInvoiceDialog = false },
            confirmButton = {},
            text = {
                InvoiceView(
                    booking = activeBooking,
                    settings = settings,
                    onClose = { showInvoiceDialog = false }
                )
            }
        )
    }

    // Add Service Dialog
    if (showAddServiceDialog) {
        AddServiceDialog(
            onDismiss = { showAddServiceDialog = false },
            onAdd = {
                onAddService(it)
                showAddServiceDialog = false
            }
        )
    }

    // Add Technician Dialog
    if (showAddTechnicianDialog) {
        AddTechnicianDialog(
            onDismiss = { showAddTechnicianDialog = false },
            onAdd = { name, phone, spec ->
                onAddTechnician(name, phone, spec)
                showAddTechnicianDialog = false
            }
        )
    }
}

@Composable
private fun MetricCard(title: String, value: String, color: Color, isAlert: Boolean = false) {
    Card(
        modifier = Modifier
            .width(104.dp)
            .height(72.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = if (isAlert) Color(0xFFFEE2E2) else FrostWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, fontSize = 10.sp, color = TextMuted, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun AdminBookingCard(
    booking: BookingEntity,
    language: Language,
    onClick: () -> Unit
) {
    val statusColor = when (booking.status) {
        "NEW" -> StatusNewColor
        "CONFIRMED" -> StatusConfirmedColor
        "TECHNICIAN_ASSIGNED" -> StatusAssignedColor
        "IN_PROGRESS" -> StatusProgressColor
        "COMPLETED" -> StatusCompletedColor
        "CANCELLED" -> Color(0xFFDC2626)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("admin_booking_card_${booking.id}"),
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
                Column {
                    Text(text = booking.id, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkBlue900)
                    Text(text = "${booking.customerName} • ${booking.mobileNumber}", fontSize = 12.sp, color = TextSecondary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "${booking.category}: ${booking.serviceName}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(
                text = "${booking.preferredDate} (${booking.preferredTimeSlot}) • Locality: ${booking.locality}",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tech: ${booking.assignedTechnicianName ?: "Unassigned"}",
                    fontSize = 11.sp,
                    color = if (booking.assignedTechnicianName != null) DarkBlue800 else TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${booking.totalAmount} (${booking.paymentStatus})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (booking.paymentStatus == "PAID") StatusCompletedColor else CashAmber
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun BookingDetailAdminDialog(
    booking: BookingEntity,
    history: List<BookingStatusHistoryEntity>,
    technicians: List<TechnicianEntity>,
    settings: BusinessSettingsEntity,
    language: Language,
    onDismiss: () -> Unit,
    onUpdateStatus: (bookingId: String, newStatus: String, note: String, techId: Long?, techName: String?) -> Unit,
    onUpdateBilling: (bookingId: String, inspection: Int, repair: Int, parts: Int, discount: Int, payMethod: String, payStatus: String) -> Unit,
    onUpdateNotes: (bookingId: String, notes: String) -> Unit,
    onReschedule: (bookingId: String, newDate: String, newSlot: String, note: String) -> Unit,
    onCancel: (bookingId: String, reason: String) -> Unit,
    onViewInvoice: () -> Unit
) {
    val context = LocalContext.current
    var inspectionChargeInput by remember { mutableStateOf(booking.inspectionCharge.toString()) }
    var repairChargeInput by remember { mutableStateOf(booking.repairCharge.toString()) }
    var partsChargeInput by remember { mutableStateOf(booking.partsCharge.toString()) }
    var discountInput by remember { mutableStateOf(booking.discountAmount.toString()) }
    var selectedPayMethod by remember { mutableStateOf(booking.paymentMethod) }
    var selectedPayStatus by remember { mutableStateOf(booking.paymentStatus) }

    var internalNotesInput by remember { mutableStateOf(booking.internalNotes) }

    var selectedTechnician by remember {
        mutableStateOf(technicians.firstOrNull { it.id == booking.assignedTechnicianId })
    }

    var showRescheduleFields by remember { mutableStateOf(false) }
    var rescheduleDate by remember { mutableStateOf(booking.preferredDate) }
    var rescheduleSlot by remember { mutableStateOf(booking.preferredTimeSlot) }

    var showCancelDialog by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800)) {
                Text("Close")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = booking.id, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue900)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SkyBlue100)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = booking.status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkBlue800)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Customer info with direct click-to-call
                Text(text = "Customer: ${booking.customerName}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Phone: ${booking.mobileNumber}", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${booking.mobileNumber}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = SkyBlue500, modifier = Modifier.size(16.dp))
                    }
                }
                Text(text = "Address: ${booking.fullAddress}, ${booking.locality}", fontSize = 12.sp, color = TextSecondary)
                Text(text = "Preferred: ${booking.preferredDate} (${booking.preferredTimeSlot})", fontSize = 12.sp, color = TextSecondary)
                if (booking.problemDesc.isNotBlank()) {
                    Text(text = "Problem: ${booking.problemDesc}", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // STATUS PROGRESSION CONTROLS
                Text(text = "UPDATE WORK STATUS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (booking.status == "NEW") {
                        Button(
                            onClick = { onUpdateStatus(booking.id, "CONFIRMED", "Admin confirmed appointment", null, null) },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusConfirmedColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Confirm Booking", fontSize = 11.sp)
                        }
                    }

                    if (booking.status == "CONFIRMED" || booking.status == "NEW") {
                        Button(
                            onClick = {
                                if (selectedTechnician != null) {
                                    onUpdateStatus(booking.id, "TECHNICIAN_ASSIGNED", "Technician ${selectedTechnician!!.name} assigned", selectedTechnician!!.id, selectedTechnician!!.name)
                                }
                            },
                            enabled = selectedTechnician != null,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusAssignedColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Assign Tech", fontSize = 11.sp)
                        }
                    }

                    if (booking.status == "TECHNICIAN_ASSIGNED" || booking.status == "CONFIRMED") {
                        Button(
                            onClick = { onUpdateStatus(booking.id, "IN_PROGRESS", "Work started at customer site", null, null) },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusProgressColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Mark In-Progress", fontSize = 11.sp)
                        }
                    }

                    if (booking.status == "IN_PROGRESS" || booking.status == "TECHNICIAN_ASSIGNED" || booking.status == "CONFIRMED") {
                        Button(
                            onClick = { onUpdateStatus(booking.id, "COMPLETED", "Job completed successfully", null, null) },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusCompletedColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Mark Completed", fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { showRescheduleFields = !showRescheduleFields },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reschedule", fontSize = 11.sp)
                    }

                    if (booking.status != "CANCELLED") {
                        Button(
                            onClick = { showCancelDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancel", fontSize = 11.sp)
                        }
                    }
                }

                // Reschedule input block
                if (showRescheduleFields) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SkyBlue50)) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Reschedule To:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = rescheduleDate,
                                onValueChange = { rescheduleDate = it },
                                label = { Text("Date (YYYY-MM-DD)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = rescheduleSlot,
                                onValueChange = { rescheduleSlot = it },
                                label = { Text("Slot") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    onReschedule(booking.id, rescheduleDate, rescheduleSlot, "Rescheduled by admin")
                                    showRescheduleFields = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800)
                            ) {
                                Text("Save New Schedule", fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // ASSIGN TECHNICIAN
                Text(text = "ASSIGN TECHNICIAN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))

                var techDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = techDropdownExpanded,
                    onExpandedChange = { techDropdownExpanded = !techDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedTechnician?.name ?: "Select Technician",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = techDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = techDropdownExpanded,
                        onDismissRequest = { techDropdownExpanded = false }
                    ) {
                        technicians.forEach { tech ->
                            DropdownMenuItem(
                                text = { Text("${tech.name} (${tech.specialty})") },
                                onClick = {
                                    selectedTechnician = tech
                                    techDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // BILLING & PAYMENT MANAGEMENT
                Text(text = "FINAL BILL & PAYMENT RECORD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inspectionChargeInput,
                        onValueChange = { inspectionChargeInput = it },
                        label = { Text("Inspection (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = repairChargeInput,
                        onValueChange = { repairChargeInput = it },
                        label = { Text("Repair/Labor (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = partsChargeInput,
                        onValueChange = { partsChargeInput = it },
                        label = { Text("Parts/Gas (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = discountInput,
                        onValueChange = { discountInput = it },
                        label = { Text("Discount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Payment Mode & Payment Status
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Payment Mode", fontSize = 11.sp, color = TextMuted)
                        Row {
                            FilterChip(
                                selected = selectedPayMethod == "CASH",
                                onClick = { selectedPayMethod = "CASH" },
                                label = { Text("Cash", fontSize = 11.sp) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            FilterChip(
                                selected = selectedPayMethod == "UPI",
                                onClick = { selectedPayMethod = "UPI" },
                                label = { Text("UPI", fontSize = 11.sp) }
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Payment Status", fontSize = 11.sp, color = TextMuted)
                        Row {
                            FilterChip(
                                selected = selectedPayStatus == "PAID",
                                onClick = { selectedPayStatus = "PAID" },
                                label = { Text("Paid", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDCFCE7), selectedLabelColor = StatusCompletedColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            FilterChip(
                                selected = selectedPayStatus == "PENDING",
                                onClick = { selectedPayStatus = "PENDING" },
                                label = { Text("Pending", fontSize = 11.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val insp = inspectionChargeInput.toIntOrNull() ?: 0
                            val rep = repairChargeInput.toIntOrNull() ?: 0
                            val parts = partsChargeInput.toIntOrNull() ?: 0
                            val disc = discountInput.toIntOrNull() ?: 0
                            onUpdateBilling(booking.id, insp, rep, parts, disc, selectedPayMethod, selectedPayStatus)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Billing", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onViewInvoice,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlue500),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View Invoice", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // INTERNAL NOTES
                Text(text = "INTERNAL BUSINESS NOTES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = internalNotesInput,
                    onValueChange = { internalNotesInput = it },
                    placeholder = { Text("e.g. Needs 1.5 ton fan capacitor, arrive after 4 PM...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = { onUpdateNotes(booking.id, internalNotesInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save Notes", fontSize = 11.sp)
                }

                // STATUS TIMELINE
                if (history.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "STATUS HISTORY TIMELINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Spacer(modifier = Modifier.height(6.dp))

                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
                    history.forEach { h ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = SkyBlue500, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "${h.status} • ${sdf.format(Date(h.timestamp))}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                if (h.note.isNotBlank()) {
                                    Text(text = h.note, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    // Cancel confirmation dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Booking") },
            text = {
                Column {
                    Text("Please enter cancellation reason:")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = cancelReason,
                        onValueChange = { cancelReason = it },
                        placeholder = { Text("e.g. Customer cancelled, appliance fixed...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancel(booking.id, cancelReason)
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Confirm Cancel")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCancelDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
private fun ServicesManagementTab(
    services: List<ServiceEntity>,
    onAddService: () -> Unit,
    onToggleVisibility: (ServiceEntity) -> Unit,
    onDelete: (ServiceEntity) -> Unit,
    onEdit: (ServiceEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("SERVICE CATALOG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text("Add, hide, or update service rates", fontSize = 12.sp, color = TextSecondary)
                }

                Button(
                    onClick = onAddService,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Service", fontSize = 11.sp)
                }
            }
        }

        items(services, key = { it.id }) { s ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = if (s.isHidden) Color(0xFFF1F5F9) else FrostWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = s.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (s.category == "AC") SkyBlue500 else Color(0xFFD97706)
                            )
                            if (s.isHidden) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("(Hidden)", fontSize = 10.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(text = s.nameEn, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = s.nameHi, fontSize = 11.sp, color = TextSecondary)
                        Text(
                            text = if (s.startingPrice > 0) "Starting: ₹${s.startingPrice} | Inspection: ₹${s.inspectionCharge}"
                            else "Inspection: ₹${s.inspectionCharge} (Quote on-site)",
                            fontSize = 11.sp,
                            color = DarkBlue800,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row {
                        IconButton(onClick = { onToggleVisibility(s) }) {
                            Icon(
                                imageVector = if (s.isHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle visibility",
                                tint = if (s.isHidden) TextMuted else DarkBlue800
                            )
                        }
                        IconButton(onClick = { onDelete(s) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TechniciansManagementTab(
    technicians: List<TechnicianEntity>,
    onAddTechnician: () -> Unit,
    onDelete: (TechnicianEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("SERVICE TEAM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text("Manage assigned field technicians", fontSize = 12.sp, color = TextSecondary)
                }

                Button(
                    onClick = onAddTechnician,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Tech", fontSize = 11.sp)
                }
            }
        }

        items(technicians, key = { it.id }) { tech ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = FrostWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SkyBlue100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Engineering, contentDescription = null, tint = DarkBlue800)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = tech.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkBlue900)
                            Text(text = "${tech.phone} • ${tech.specialty}", fontSize = 12.sp, color = TextSecondary)
                        }
                    }

                    IconButton(onClick = { onDelete(tech) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626))
                    }
                }
            }
        }
    }
}

@Composable
private fun BusinessSettingsTab(
    settings: BusinessSettingsEntity,
    onSave: (BusinessSettingsEntity) -> Unit
) {
    var businessName by remember { mutableStateOf(settings.businessName) }
    var phone by remember { mutableStateOf(settings.contactPhone) }
    var whatsapp by remember { mutableStateOf(settings.whatsappNumber) }
    var email by remember { mutableStateOf(settings.businessEmail) }
    var address by remember { mutableStateOf(settings.businessAddress) }
    var serviceAreas by remember { mutableStateOf(settings.serviceAreas) }
    var baseInspectionFee by remember { mutableStateOf(settings.baseInspectionCharge.toString()) }
    var adminPin by remember { mutableStateOf(settings.adminPin) }
    var upiId by remember { mutableStateOf(settings.upiId) }
    var saveSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("BUSINESS PROFILE & CONFIGURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Business Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Call Helpline Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = whatsapp,
            onValueChange = { whatsapp = it },
            label = { Text("WhatsApp Chat Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Business Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Store / Workshop Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = serviceAreas,
            onValueChange = { serviceAreas = it },
            label = { Text("Serviceable Areas (Comma Separated)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = baseInspectionFee,
            onValueChange = { baseInspectionFee = it },
            label = { Text("Default Base Inspection Fee (₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = upiId,
            onValueChange = { upiId = it },
            label = { Text("UPI ID for verification") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = adminPin,
            onValueChange = { adminPin = it },
            label = { Text("Admin Portal Security PIN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (saveSuccess) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Settings successfully saved!",
                    color = Color(0xFF166534),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                val fee = baseInspectionFee.toIntOrNull() ?: 199
                onSave(
                    settings.copy(
                        businessName = businessName,
                        contactPhone = phone,
                        whatsappNumber = whatsapp,
                        businessEmail = email,
                        businessAddress = address,
                        serviceAreas = serviceAreas,
                        baseInspectionCharge = fee,
                        adminPin = adminPin,
                        upiId = upiId
                    )
                )
                saveSuccess = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Save All Settings", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InquiriesTab(messages: List<ContactMessageEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("CUSTOMER CONTACT INQUIRIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        }

        if (messages.isEmpty()) {
            item {
                Text("No inquiries received yet.", color = TextMuted, fontSize = 13.sp, modifier = Modifier.padding(16.dp))
            }
        }

        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
        items(messages, key = { it.id }) { msg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = FrostWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = msg.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = sdf.format(Date(msg.timestamp)), fontSize = 10.sp, color = TextMuted)
                    }
                    Text(text = "Phone: ${msg.mobileNumber}", fontSize = 12.sp, color = SkyBlue500)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = msg.message, fontSize = 12.sp, color = TextPrimary)
                }
            }
        }
    }
}

@Composable
private fun AddServiceDialog(
    onDismiss: () -> Unit,
    onAdd: (ServiceEntity) -> Unit
) {
    var category by remember { mutableStateOf("AC") }
    var nameEn by remember { mutableStateOf("") }
    var nameHi by remember { mutableStateOf("") }
    var descEn by remember { mutableStateOf("") }
    var descHi by remember { mutableStateOf("") }
    var startingPrice by remember { mutableStateOf("0") }
    var inspectionCharge by remember { mutableStateOf("199") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Service") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row {
                    FilterChip(
                        selected = category == "AC",
                        onClick = { category = "AC" },
                        label = { Text("AC") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = category == "REFRIGERATOR",
                        onClick = { category = "REFRIGERATOR" },
                        label = { Text("Refrigerator") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = nameEn, onValueChange = { nameEn = it }, label = { Text("Name (English)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = nameHi, onValueChange = { nameHi = it }, label = { Text("Name (Hindi)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = descEn, onValueChange = { descEn = it }, label = { Text("Description (English)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = descHi, onValueChange = { descHi = it }, label = { Text("Description (Hindi)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = startingPrice,
                    onValueChange = { startingPrice = it },
                    label = { Text("Starting Price (₹, 0 if inspection only)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = inspectionCharge,
                    onValueChange = { inspectionCharge = it },
                    label = { Text("Inspection Charge (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameEn.isNotBlank()) {
                        onAdd(
                            ServiceEntity(
                                category = category,
                                nameEn = nameEn,
                                nameHi = if (nameHi.isNotBlank()) nameHi else nameEn,
                                descEn = descEn,
                                descHi = if (descHi.isNotBlank()) descHi else descEn,
                                startingPrice = startingPrice.toIntOrNull() ?: 0,
                                inspectionCharge = inspectionCharge.toIntOrNull() ?: 199,
                                priceType = if ((startingPrice.toIntOrNull() ?: 0) > 0) "STARTING_FROM" else "AFTER_INSPECTION"
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800)
            ) {
                Text("Add Service")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AddTechnicianDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, phone: String, specialty: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Field Technician") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = specialty, onValueChange = { specialty = it }, label = { Text("Specialty (e.g. Split AC, Gas Refill)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onAdd(name, phone, specialty)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800)
            ) {
                Text("Add Technician")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
