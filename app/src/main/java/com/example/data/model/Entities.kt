package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "AC" or "REFRIGERATOR"
    val nameEn: String,
    val nameHi: String,
    val descEn: String,
    val descHi: String,
    val startingPrice: Int = 0,
    val inspectionCharge: Int = 199,
    val priceType: String = "STARTING_FROM", // "STARTING_FROM" or "AFTER_INSPECTION"
    val iconKey: String = "ac_repair",
    val isHidden: Boolean = false,
    val displayOrder: Int = 0
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey
    val id: String, // e.g. "ACS-202610-8492"
    val customerName: String,
    val mobileNumber: String,
    val category: String,
    val serviceId: Long,
    val serviceName: String,
    val fullAddress: String,
    val locality: String,
    val preferredDate: String, // YYYY-MM-DD
    val preferredTimeSlot: String,
    val problemDesc: String = "",
    val preferredContactMethod: String = "PHONE", // "PHONE" or "WHATSAPP"
    val status: String = "NEW", // "NEW", "CONFIRMED", "TECHNICIAN_ASSIGNED", "IN_PROGRESS", "COMPLETED", "RESCHEDULED", "CANCELLED"
    val assignedTechnicianId: Long? = null,
    val assignedTechnicianName: String? = null,
    val internalNotes: String = "",
    val inspectionCharge: Int = 199,
    val repairCharge: Int = 0,
    val partsCharge: Int = 0,
    val discountAmount: Int = 0,
    val totalAmount: Int = 0,
    val paymentMethod: String = "CASH", // "CASH", "UPI"
    val paymentStatus: String = "PENDING", // "PENDING", "PAID", "REFUNDED"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "booking_status_history")
data class BookingStatusHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingId: String,
    val status: String,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "technicians")
data class TechnicianEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val specialty: String, // e.g. "AC & Refrigerator Specialist"
    val isActive: Boolean = true
)

@Entity(tableName = "business_settings")
data class BusinessSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val businessName: String = "Arham Cooling Solutions",
    val taglineEn: String = "Your Comfort, Our Priority",
    val taglineHi: String = "आपकी सुविधा, हमारी प्राथमिकता",
    val contactPhone: String = "+91 98765 43210",
    val whatsappNumber: String = "+91 98765 43210",
    val businessEmail: String = "contact@arhamcooling.com",
    val businessAddress: String = "Shop No. 4, Main Market, Near City Centre, Delhi / NCR",
    val serviceAreas: String = "Civil Lines, Model Town, Sector 14, Sector 21, Urban Estate, DLF Phase 1-3, Old City",
    val googleMapsUrl: String = "https://maps.google.com/?q=Arham+Cooling+Solutions",
    val baseInspectionCharge: Int = 199,
    val adminPin: String = "admin123",
    val upiId: String = "arhamcooling@upi",
    val privacyPolicyText: String = "At Arham Cooling Solutions, we respect your privacy. We collect your name, phone number, and address strictly to deliver doorstep appliance repair services. We never sell, rent, or share customer data with third parties.",
    val termsText: String = "1. Inspection charge applies for diagnostics. 2. Final repair quote is given before commencing work. 3. 30-day warranty applies on parts replaced and specific repairs done. 4. Cash or verified UPI payments accepted upon service completion."
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val rating: Int = 5,
    val reviewText: String,
    val serviceName: String,
    val dateStr: String,
    val isApproved: Boolean = true
)

@Entity(tableName = "contact_messages")
data class ContactMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val mobileNumber: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
