package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatusHistoryEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ContactMessageEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.TechnicianEntity
import com.example.data.repository.CoolingRepository
import com.example.ui.strings.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen {
    HOME,
    BOOKING,
    CONFIRMATION,
    TRACK,
    ADMIN_LOGIN,
    ADMIN_DASHBOARD
}

class CoolingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CoolingRepository
    init {
        val db = AppDatabase.getDatabase(application)
        repository = CoolingRepository(db)
    }

    // Language state - Hindi as default
    private val _language = MutableStateFlow(Language.HI)
    val language: StateFlow<Language> = _language.asStateFlow()

    // Navigation screen state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Service selected for booking
    private val _selectedServiceForBooking = MutableStateFlow<ServiceEntity?>(null)
    val selectedServiceForBooking: StateFlow<ServiceEntity?> = _selectedServiceForBooking.asStateFlow()

    // Last confirmed booking
    private val _lastSubmittedBooking = MutableStateFlow<BookingEntity?>(null)
    val lastSubmittedBooking: StateFlow<BookingEntity?> = _lastSubmittedBooking.asStateFlow()

    // Admin authentication state
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Search and filter state for Admin / Track
    private val _adminSearchQuery = MutableStateFlow("")
    val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

    private val _adminStatusFilter = MutableStateFlow("ALL")
    val adminStatusFilter: StateFlow<String> = _adminStatusFilter.asStateFlow()

    private val _adminCategoryFilter = MutableStateFlow("ALL")
    val adminCategoryFilter: StateFlow<String> = _adminCategoryFilter.asStateFlow()

    // Currently opened booking in Admin modal / Invoice
    private val _activeAdminBooking = MutableStateFlow<BookingEntity?>(null)
    val activeAdminBooking: StateFlow<BookingEntity?> = _activeAdminBooking.asStateFlow()

    private val _activeBookingHistory = MutableStateFlow<List<BookingStatusHistoryEntity>>(emptyList())
    val activeBookingHistory: StateFlow<List<BookingStatusHistoryEntity>> = _activeBookingHistory.asStateFlow()

    // Repository reactive data streams
    val visibleServices: StateFlow<List<ServiceEntity>> = repository.visibleServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServices: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val businessSettings: StateFlow<BusinessSettingsEntity> = repository.businessSettings
        .combine(MutableStateFlow(BusinessSettingsEntity())) { settings, default ->
            settings ?: default
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BusinessSettingsEntity())

    val allTechnicians: StateFlow<List<TechnicianEntity>> = repository.allTechnicians
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTechnicians: StateFlow<List<TechnicianEntity>> = repository.activeTechnicians
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val approvedReviews: StateFlow<List<ReviewEntity>> = repository.approvedReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contactMessages: StateFlow<List<ContactMessageEntity>> = repository.contactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.HI) Language.EN else Language.HI
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun startBooking(service: ServiceEntity? = null) {
        _selectedServiceForBooking.value = service
        _currentScreen.value = AppScreen.BOOKING
    }

    fun createBooking(
        name: String,
        mobile: String,
        category: String,
        service: ServiceEntity,
        fullAddress: String,
        locality: String,
        preferredDate: String,
        preferredTimeSlot: String,
        problemDesc: String,
        contactMethod: String,
        onSuccess: (BookingEntity) -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank() || mobile.length < 10 || fullAddress.isBlank() || locality.isBlank()) {
            onError("Please fill in all required fields with a valid 10-digit mobile number.")
            return
        }

        viewModelScope.launch {
            try {
                val bookingId = repository.generateBookingId()
                val settings = businessSettings.value
                val inspectionFee = if (service.inspectionCharge > 0) service.inspectionCharge else settings.baseInspectionCharge

                val newBooking = BookingEntity(
                    id = bookingId,
                    customerName = name.trim(),
                    mobileNumber = mobile.trim(),
                    category = category,
                    serviceId = service.id,
                    serviceName = if (_language.value == Language.HI) service.nameHi else service.nameEn,
                    fullAddress = fullAddress.trim(),
                    locality = locality.trim(),
                    preferredDate = preferredDate,
                    preferredTimeSlot = preferredTimeSlot,
                    problemDesc = problemDesc.trim(),
                    preferredContactMethod = contactMethod,
                    status = "NEW",
                    inspectionCharge = inspectionFee,
                    repairCharge = 0,
                    partsCharge = 0,
                    discountAmount = 0,
                    totalAmount = inspectionFee,
                    paymentMethod = "CASH",
                    paymentStatus = "PENDING"
                )

                repository.createBooking(newBooking)
                _lastSubmittedBooking.value = newBooking
                _currentScreen.value = AppScreen.CONFIRMATION
                onSuccess(newBooking)
            } catch (e: Exception) {
                onError("Failed to submit booking: ${e.localizedMessage}")
            }
        }
    }

    fun selectAdminBooking(booking: BookingEntity?) {
        _activeAdminBooking.value = booking
        if (booking != null) {
            viewModelScope.launch {
                repository.getBookingHistory(booking.id).collect { history ->
                    _activeBookingHistory.value = history
                }
            }
        } else {
            _activeBookingHistory.value = emptyList()
        }
    }

    fun setAdminSearch(query: String) {
        _adminSearchQuery.value = query
    }

    fun setAdminStatusFilter(status: String) {
        _adminStatusFilter.value = status
    }

    fun setAdminCategoryFilter(cat: String) {
        _adminCategoryFilter.value = cat
    }

    fun adminLogin(pin: String): Boolean {
        val currentPin = businessSettings.value.adminPin
        return if (pin.trim() == currentPin.trim()) {
            _isAdminLoggedIn.value = true
            _currentScreen.value = AppScreen.ADMIN_DASHBOARD
            true
        } else {
            false
        }
    }

    fun adminLogout() {
        _isAdminLoggedIn.value = false
        _currentScreen.value = AppScreen.HOME
    }

    fun updateBookingStatus(
        bookingId: String,
        newStatus: String,
        note: String,
        technicianId: Long? = null,
        technicianName: String? = null
    ) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, newStatus, note, technicianId, technicianName)
            // refresh active booking
            allBookings.value.find { it.id == bookingId }?.let {
                _activeAdminBooking.value = it.copy(
                    status = newStatus,
                    assignedTechnicianId = technicianId ?: it.assignedTechnicianId,
                    assignedTechnicianName = technicianName ?: it.assignedTechnicianName
                )
            }
        }
    }

    fun updateBookingBilling(
        bookingId: String,
        inspection: Int,
        repair: Int,
        parts: Int,
        discount: Int,
        payMethod: String,
        payStatus: String
    ) {
        viewModelScope.launch {
            repository.updateBookingBilling(
                bookingId = bookingId,
                inspectionCharge = inspection,
                repairCharge = repair,
                partsCharge = parts,
                discountAmount = discount,
                paymentMethod = payMethod,
                paymentStatus = payStatus
            )
            val calculatedTotal = (inspection + repair + parts - discount).coerceAtLeast(0)
            _activeAdminBooking.value = _activeAdminBooking.value?.copy(
                inspectionCharge = inspection,
                repairCharge = repair,
                partsCharge = parts,
                discountAmount = discount,
                totalAmount = calculatedTotal,
                paymentMethod = payMethod,
                paymentStatus = payStatus
            )
        }
    }

    fun updateBookingNotes(bookingId: String, notes: String) {
        viewModelScope.launch {
            repository.updateBookingNotes(bookingId, notes)
            _activeAdminBooking.value = _activeAdminBooking.value?.copy(internalNotes = notes)
        }
    }

    fun rescheduleBooking(bookingId: String, newDate: String, newSlot: String, note: String) {
        viewModelScope.launch {
            repository.rescheduleBooking(bookingId, newDate, newSlot, note)
            _activeAdminBooking.value = _activeAdminBooking.value?.copy(
                preferredDate = newDate,
                preferredTimeSlot = newSlot,
                status = "RESCHEDULED"
            )
        }
    }

    fun cancelBooking(bookingId: String, reason: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId, reason)
            _activeAdminBooking.value = _activeAdminBooking.value?.copy(status = "CANCELLED")
        }
    }

    fun addService(service: ServiceEntity) {
        viewModelScope.launch { repository.addService(service) }
    }

    fun updateService(service: ServiceEntity) {
        viewModelScope.launch { repository.updateService(service) }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch { repository.deleteService(service) }
    }

    fun toggleServiceVisibility(service: ServiceEntity) {
        viewModelScope.launch { repository.updateService(service.copy(isHidden = !service.isHidden)) }
    }

    fun addTechnician(name: String, phone: String, specialty: String) {
        viewModelScope.launch {
            repository.addTechnician(
                TechnicianEntity(name = name, phone = phone, specialty = specialty)
            )
        }
    }

    fun deleteTechnician(technician: TechnicianEntity) {
        viewModelScope.launch { repository.deleteTechnician(technician) }
    }

    fun updateSettings(settings: BusinessSettingsEntity) {
        viewModelScope.launch { repository.updateSettings(settings) }
    }

    fun submitContactMessage(name: String, mobile: String, message: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.sendContactMessage(name, mobile, message)
            onDone()
        }
    }

    fun markMessageRead(id: Long) {
        viewModelScope.launch { repository.markMessageRead(id) }
    }

    fun exportBookingsCsv(bookings: List<BookingEntity>): String {
        val sb = StringBuilder()
        sb.append("Booking ID,Customer Name,Mobile,Category,Service,Locality,Date,Slot,Status,Technician,Total (INR),Payment Status,Payment Mode,Created At\n")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        for (b in bookings) {
            val dateStr = sdf.format(Date(b.createdAt))
            sb.append("\"${b.id}\",\"${b.customerName}\",\"${b.mobileNumber}\",\"${b.category}\",\"${b.serviceName}\",\"${b.locality}\",\"${b.preferredDate}\",\"${b.preferredTimeSlot}\",\"${b.status}\",\"${b.assignedTechnicianName ?: "None"}\",${b.totalAmount},\"${b.paymentStatus}\",\"${b.paymentMethod}\",\"$dateStr\"\n")
        }
        return sb.toString()
    }
}
