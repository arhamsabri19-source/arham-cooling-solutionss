package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatusHistoryEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ContactMessageEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.TechnicianEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY displayOrder ASC, id ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE isHidden = 0 ORDER BY displayOrder ASC, id ASC")
    fun getVisibleServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun getServiceById(id: Long): ServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    fun getBookingById(id: String): Flow<BookingEntity?>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getBookingByIdDirect(id: String): BookingEntity?

    @Query("SELECT * FROM bookings WHERE id LIKE '%' || :query || '%' OR mobileNumber LIKE '%' || :query || '%' OR customerName LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchBookings(query: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}

@Dao
interface BookingStatusHistoryDao {
    @Query("SELECT * FROM booking_status_history WHERE bookingId = :bookingId ORDER BY timestamp ASC")
    fun getHistoryForBooking(bookingId: String): Flow<List<BookingStatusHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: BookingStatusHistoryEntity)
}

@Dao
interface TechnicianDao {
    @Query("SELECT * FROM technicians ORDER BY name ASC")
    fun getAllTechnicians(): Flow<List<TechnicianEntity>>

    @Query("SELECT * FROM technicians WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveTechnicians(): Flow<List<TechnicianEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnician(technician: TechnicianEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnicians(technicians: List<TechnicianEntity>)

    @Update
    suspend fun updateTechnician(technician: TechnicianEntity)

    @Delete
    suspend fun deleteTechnician(technician: TechnicianEntity)
}

@Dao
interface BusinessSettingsDao {
    @Query("SELECT * FROM business_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<BusinessSettingsEntity?>

    @Query("SELECT * FROM business_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsDirect(): BusinessSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: BusinessSettingsEntity)

    @Update
    suspend fun updateSettings(settings: BusinessSettingsEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE isApproved = 1 ORDER BY id DESC")
    fun getApprovedReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)
}

@Dao
interface ContactMessageDao {
    @Query("SELECT * FROM contact_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ContactMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ContactMessageEntity)

    @Query("UPDATE contact_messages SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)
}
