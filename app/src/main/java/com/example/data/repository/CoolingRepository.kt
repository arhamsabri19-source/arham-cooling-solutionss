package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatusHistoryEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ContactMessageEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.TechnicianEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class CoolingRepository(private val db: AppDatabase) {

    val visibleServices: Flow<List<ServiceEntity>> = db.serviceDao().getVisibleServices()
    val allServices: Flow<List<ServiceEntity>> = db.serviceDao().getAllServices()

    val allBookings: Flow<List<BookingEntity>> = db.bookingDao().getAllBookings()
    val businessSettings: Flow<BusinessSettingsEntity?> = db.businessSettingsDao().getSettings()

    val allTechnicians: Flow<List<TechnicianEntity>> = db.technicianDao().getAllTechnicians()
    val activeTechnicians: Flow<List<TechnicianEntity>> = db.technicianDao().getActiveTechnicians()

    val approvedReviews: Flow<List<ReviewEntity>> = db.reviewDao().getApprovedReviews()
    val allReviews: Flow<List<ReviewEntity>> = db.reviewDao().getAllReviews()

    val contactMessages: Flow<List<ContactMessageEntity>> = db.contactMessageDao().getAllMessages()

    suspend fun generateBookingId(): String {
        val dateFormat = SimpleDateFormat("yyyyMM", Locale.US)
        val prefix = "ACS-" + dateFormat.format(Date())
        val randomSuffix = Random.nextInt(1000, 9999)
        return "$prefix-$randomSuffix"
    }

    suspend fun createBooking(booking: BookingEntity): String {
        db.bookingDao().insertBooking(booking)
        db.bookingStatusHistoryDao().insertHistory(
            BookingStatusHistoryEntity(
                bookingId = booking.id,
                status = "NEW",
                note = "Booking request submitted by customer ${booking.customerName}"
            )
        )
        return booking.id
    }

    fun getBookingById(id: String): Flow<BookingEntity?> = db.bookingDao().getBookingById(id)

    fun getBookingHistory(bookingId: String): Flow<List<BookingStatusHistoryEntity>> =
        db.bookingStatusHistoryDao().getHistoryForBooking(bookingId)

    fun searchBookings(query: String): Flow<List<BookingEntity>> =
        db.bookingDao().searchBookings(query)

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: String,
        note: String,
        technicianId: Long? = null,
        technicianName: String? = null
    ) {
        val existing = db.bookingDao().getBookingByIdDirect(bookingId) ?: return
        val updated = existing.copy(
            status = newStatus,
            assignedTechnicianId = technicianId ?: existing.assignedTechnicianId,
            assignedTechnicianName = technicianName ?: existing.assignedTechnicianName
        )
        db.bookingDao().updateBooking(updated)
        db.bookingStatusHistoryDao().insertHistory(
            BookingStatusHistoryEntity(
                bookingId = bookingId,
                status = newStatus,
                note = note
            )
        )
    }

    suspend fun updateBookingBilling(
        bookingId: String,
        inspectionCharge: Int,
        repairCharge: Int,
        partsCharge: Int,
        discountAmount: Int,
        paymentMethod: String,
        paymentStatus: String
    ) {
        val existing = db.bookingDao().getBookingByIdDirect(bookingId) ?: return
        val calculatedTotal = (inspectionCharge + repairCharge + partsCharge - discountAmount).coerceAtLeast(0)
        val updated = existing.copy(
            inspectionCharge = inspectionCharge,
            repairCharge = repairCharge,
            partsCharge = partsCharge,
            discountAmount = discountAmount,
            totalAmount = calculatedTotal,
            paymentMethod = paymentMethod,
            paymentStatus = paymentStatus
        )
        db.bookingDao().updateBooking(updated)
        db.bookingStatusHistoryDao().insertHistory(
            BookingStatusHistoryEntity(
                bookingId = bookingId,
                status = existing.status,
                note = "Billing updated: Total ₹$calculatedTotal, Payment: $paymentStatus ($paymentMethod)"
            )
        )
    }

    suspend fun updateBookingNotes(bookingId: String, internalNotes: String) {
        val existing = db.bookingDao().getBookingByIdDirect(bookingId) ?: return
        db.bookingDao().updateBooking(existing.copy(internalNotes = internalNotes))
    }

    suspend fun rescheduleBooking(bookingId: String, newDate: String, newSlot: String, note: String) {
        val existing = db.bookingDao().getBookingByIdDirect(bookingId) ?: return
        val updated = existing.copy(
            preferredDate = newDate,
            preferredTimeSlot = newSlot,
            status = "RESCHEDULED"
        )
        db.bookingDao().updateBooking(updated)
        db.bookingStatusHistoryDao().insertHistory(
            BookingStatusHistoryEntity(
                bookingId = bookingId,
                status = "RESCHEDULED",
                note = "Rescheduled to $newDate ($newSlot). $note"
            )
        )
    }

    suspend fun cancelBooking(bookingId: String, reason: String) {
        val existing = db.bookingDao().getBookingByIdDirect(bookingId) ?: return
        val updated = existing.copy(status = "CANCELLED")
        db.bookingDao().updateBooking(updated)
        db.bookingStatusHistoryDao().insertHistory(
            BookingStatusHistoryEntity(
                bookingId = bookingId,
                status = "CANCELLED",
                note = "Cancelled: $reason"
            )
        )
    }

    suspend fun addService(service: ServiceEntity): Long = db.serviceDao().insertService(service)
    suspend fun updateService(service: ServiceEntity) = db.serviceDao().updateService(service)
    suspend fun deleteService(service: ServiceEntity) = db.serviceDao().deleteService(service)

    suspend fun addTechnician(technician: TechnicianEntity): Long = db.technicianDao().insertTechnician(technician)
    suspend fun updateTechnician(technician: TechnicianEntity) = db.technicianDao().updateTechnician(technician)
    suspend fun deleteTechnician(technician: TechnicianEntity) = db.technicianDao().deleteTechnician(technician)

    suspend fun updateSettings(settings: BusinessSettingsEntity) = db.businessSettingsDao().updateSettings(settings)

    suspend fun sendContactMessage(name: String, mobile: String, message: String) {
        db.contactMessageDao().insertMessage(
            ContactMessageEntity(
                customerName = name,
                mobileNumber = mobile,
                message = message
            )
        )
    }

    suspend fun markMessageRead(id: Long) = db.contactMessageDao().markAsRead(id)

    suspend fun addReview(review: ReviewEntity) = db.reviewDao().insertReview(review)
}
