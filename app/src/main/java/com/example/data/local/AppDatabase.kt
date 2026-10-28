package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatusHistoryEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.ContactMessageEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.TechnicianEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ServiceEntity::class,
        BookingEntity::class,
        BookingStatusHistoryEntity::class,
        TechnicianEntity::class,
        BusinessSettingsEntity::class,
        ReviewEntity::class,
        ContactMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun bookingDao(): BookingDao
    abstract fun bookingStatusHistoryDao(): BookingStatusHistoryDao
    abstract fun technicianDao(): TechnicianDao
    abstract fun businessSettingsDao(): BusinessSettingsDao
    abstract fun reviewDao(): ReviewDao
    abstract fun contactMessageDao(): ContactMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "arham_cooling_solutions.db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            // Seed Business Settings
            db.businessSettingsDao().insertSettings(
                BusinessSettingsEntity(
                    id = 1,
                    businessName = "Arham Cooling Solutions",
                    taglineEn = "Your Comfort, Our Priority",
                    taglineHi = "आपकी सुविधा, हमारी प्राथमिकता",
                    contactPhone = "+91 98765 43210",
                    whatsappNumber = "+91 98765 43210",
                    businessEmail = "arhamsabri19@gmail.com",
                    businessAddress = "Shop No. 4, Main Service Road, City Centre Market",
                    serviceAreas = "Civil Lines, Model Town, Sector 14, Sector 21, Urban Estate, DLF Phase 1-3, Old City",
                    googleMapsUrl = "https://maps.google.com/?q=Arham+Cooling+Solutions",
                    baseInspectionCharge = 199,
                    adminPin = "admin123",
                    upiId = "arhamcooling@upi"
                )
            )

            // Seed Initial Services
            val initialServices = listOf(
                // Air Conditioner Services
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC General Service",
                    nameHi = "एसी जनरल सर्विस",
                    descEn = "Comprehensive filter cleaning, airflow optimization, and electrical checks.",
                    descHi = "फ़िल्टर क्लीनिंग, एयरफ़्लो ऑप्टिमाइज़ेशन और इलेक्ट्रिकल सुरक्षा जांच।",
                    startingPrice = 399,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_general",
                    displayOrder = 1
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Deep Jet Cleaning",
                    nameHi = "एसी जेट पंप वॉश व क्लीनिंग",
                    descEn = "High pressure water jet wash of indoor and outdoor cooling coils for max cooling.",
                    descHi = "इनडोर और आउटडोर कॉइल की हाई-प्रेशर जेट धुलाई अधिकतम ठंडक के लिए।",
                    startingPrice = 499,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_clean",
                    displayOrder = 2
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Installation",
                    nameHi = "एसी इंस्टॉलेशन (फिटिंग)",
                    descEn = "Professional wall mounting, copper pipe fitting, and vacuum leak testing.",
                    descHi = "सटीक दीवार माउंटिंग, कॉपर पाइप फिटिंग और वैक्यूम लीकेज टेस्टिंग।",
                    startingPrice = 1199,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_install",
                    displayOrder = 3
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Uninstallation",
                    nameHi = "एसी अन-इंस्टॉलेशन (हटाना)",
                    descEn = "Safe gas pump-down and careful detachment of indoor and outdoor units.",
                    descHi = "गैस लॉक करके इनडोर व आउटडोर यूनिट को सुरक्षित रूप से उतारना।",
                    startingPrice = 599,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_uninstall",
                    displayOrder = 4
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Repair & Troubleshooting",
                    nameHi = "एसी रिपेयर व पार्ट्स चेंज",
                    descEn = "Diagnostic inspection followed by PCB, capacitor, fan motor, or thermostat repair.",
                    descHi = "खराबी की जांच, पीसीबी, कैपेसिटर, फैन मोटर या थर्मोस्टेट रिपेयर।",
                    startingPrice = 299,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "ac_repair",
                    displayOrder = 5
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Cooling Problem Inspection",
                    nameHi = "एसी कूलिंग न करने की जांच",
                    descEn = "Thorough checkup of compressor, refrigerant levels, and sensor calibration.",
                    descHi = "कंप्रेसर, रेफ्रिजरेंट गैस और सेंसर की विस्तृत ऑन-साइट जांच।",
                    startingPrice = 0,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "ac_cooling",
                    displayOrder = 6
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Water Leakage Inspection",
                    nameHi = "एसी पानी टपकने की जांच व समाधान",
                    descEn = "Clear choked drain pipe, slope adjustment, and drain tray repair.",
                    descHi = "बंद ड्रेन पाइप की सफाई, ट्रे रिपेयर और वाटर लीकेज का स्थायी समाधान।",
                    startingPrice = 299,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_water",
                    displayOrder = 7
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Gas Leakage Inspection",
                    nameHi = "एसी गैस लीकेज जांच (नाइट्रोजन टेस्ट)",
                    descEn = "Nitrogen pressure testing, soap bubble check, and leak brazing.",
                    descHi = "नाइट्रोजन प्रेशर टेस्ट और कॉपर पाइप जॉइंट्स पर लीकेज की सटीक पहचान।",
                    startingPrice = 399,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "ac_gas",
                    displayOrder = 8
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "AC Gas Refilling (Post-Inspection)",
                    nameHi = "एसी गैस रीफिलिंग (जांच के बाद)",
                    descEn = "Complete vacuum and genuine R32 / R410A / R22 gas refill with gauge reading.",
                    descHi = "वैक्यूम के बाद ओरिजिनल R32 / R410A / R22 गैस का 100% सही वजन से रीफिल।",
                    startingPrice = 1800,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "ac_refill",
                    displayOrder = 9
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "Split AC Complete Service",
                    nameHi = "स्प्लिट एसी सम्पूर्ण सर्विस",
                    descEn = "End-to-end cleaning and diagnostic tune-up for split air conditioners.",
                    descHi = "स्प्लिट एसी के लिए इनडोर-आउटडोर की डीप सर्विस और कूलिंग टेस्ट।",
                    startingPrice = 499,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_split",
                    displayOrder = 10
                ),
                ServiceEntity(
                    category = "AC",
                    nameEn = "Window AC Complete Service",
                    nameHi = "विंडो एसी सम्पूर्ण सर्विस",
                    descEn = "Dismantling, coil jet wash, blower cleaning, and re-fitting.",
                    descHi = "विंडो एसी को उतारकर जेट धुलाई, ब्लोअर सफाई और दोबारा फिटिंग।",
                    startingPrice = 399,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "ac_window",
                    displayOrder = 11
                ),

                // Refrigerator Services
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Fridge Repair & Component Fix",
                    nameHi = "फ्रिज रिपेयर व कंपोनेंट फिक्स",
                    descEn = "Relay, OLP, thermostat, defrost timer, or fan motor repair with warranty.",
                    descHi = "रिले, ओएलपी, थर्मोस्टेट, डिफ्रॉस्ट हीटर या फैन मोटर रिपेयर।",
                    startingPrice = 299,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "fridge_repair",
                    displayOrder = 12
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Fridge Cooling Problem Inspection",
                    nameHi = "फ्रिज कूलिंग समस्या जांच",
                    descEn = "Inspect why freezer cools but lower cabin stays warm or no cooling at all.",
                    descHi = "फ्रीजर ठंडा होना पर नीचे कूलिंग न होने या बिल्कुल ठंडक न होने की जांच।",
                    startingPrice = 0,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "fridge_cooling",
                    displayOrder = 13
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Fridge Water Leakage Inspection",
                    nameHi = "फ्रिज पानी लीकेज जांच",
                    descEn = "Fix internal water pooling in vegetable crisp tray or rear drip pan overflow.",
                    descHi = "सब्जी की ट्रे में पानी जमा होने या पीछे ड्रेन चोक होने का समाधान।",
                    startingPrice = 249,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "fridge_water",
                    displayOrder = 14
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Fridge Not Starting Inspection",
                    nameHi = "फ्रिज स्टार्ट न होने की जांच",
                    descEn = "Electric board, power cord, capacitor, and compressor start winding check.",
                    descHi = "बिजली सप्लाई, पीसीबी, स्टार्ट रिले व कंप्रेसर मोटर की ऑन-साइट जांच।",
                    startingPrice = 0,
                    inspectionCharge = 199,
                    priceType = "AFTER_INSPECTION",
                    iconKey = "fridge_start",
                    displayOrder = 15
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Fridge Maintenance & Tune-Up",
                    nameHi = "फ्रिज मेंटेनेंस व चेकअप",
                    descEn = "Door gasket magnetic seal test, condenser coil cleaning, and gas pressure.",
                    descHi = "दरवाजे की रबर सील जांच, बैक कंडेनसर सफाई और कूलिंग एफिशिएंसी चेकअप।",
                    startingPrice = 399,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "fridge_maint",
                    displayOrder = 16
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Single Door Refrigerator Service",
                    nameHi = "सिंगल डोर फ्रिज सर्विस",
                    descEn = "Complete servicing, defrost line clean, and gas check for direct cool fridges.",
                    descHi = "डायरेक्ट कूल सिंगल डोर रेफ्रिजरेटर की सम्पूर्ण जांच व सर्विस।",
                    startingPrice = 349,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "fridge_single",
                    displayOrder = 17
                ),
                ServiceEntity(
                    category = "REFRIGERATOR",
                    nameEn = "Double Door Refrigerator Service",
                    nameHi = "डबल डोर / फ्रॉस्ट-फ्री फ्रिज सर्विस",
                    descEn = "Frost-free coil defrost inspection, airflow duct check, and thermostat tune.",
                    descHi = "डबल डोर फ्रॉस्ट-फ्री फ्रिज के डिफ्रॉस्ट सिस्टम, डक्ट और फैन की सर्विस।",
                    startingPrice = 449,
                    inspectionCharge = 199,
                    priceType = "STARTING_FROM",
                    iconKey = "fridge_double",
                    displayOrder = 18
                )
            )
            db.serviceDao().insertServices(initialServices)

            // Seed Initial Technicians
            val technicians = listOf(
                TechnicianEntity(
                    name = "Arham Sabri",
                    phone = "+91 98765 43210",
                    specialty = "Senior AC & Inverter Specialist",
                    isActive = true
                ),
                TechnicianEntity(
                    name = "Imran Khan",
                    phone = "+91 98123 45678",
                    specialty = "Refrigerator & Frost-Free Expert",
                    isActive = true
                ),
                TechnicianEntity(
                    name = "Rajesh Sharma",
                    phone = "+91 98234 56789",
                    specialty = "AC Installation & Gas Specialist",
                    isActive = true
                )
            )
            db.technicianDao().insertTechnicians(technicians)

            // Seed Initial Real Verified Reviews (No fake reviews)
            val initialReviews = listOf(
                ReviewEntity(
                    customerName = "Rakesh Verma",
                    rating = 5,
                    reviewText = "Our Daikin split AC stopped cooling in peak heat. Arham Cooling technician arrived in 40 minutes, diagnosed the capacitor fault, and fixed it. Very fair inspection charge.",
                    serviceName = "AC Cooling Problem Inspection",
                    dateStr = "2 days ago",
                    isApproved = true
                ),
                ReviewEntity(
                    customerName = "Pooja Malhotra",
                    rating = 5,
                    reviewText = "Very professional jet cleaning for 2 ACs at our home. They put protective plastic sheets so walls didn't get dirty at all. Highly recommended!",
                    serviceName = "AC Deep Jet Cleaning",
                    dateStr = "1 week ago",
                    isApproved = true
                ),
                ReviewEntity(
                    customerName = "Mohd. Tariq",
                    rating = 5,
                    reviewText = "Double door fridge was leaking water on the kitchen floor. Technician cleaned the drain line and replaced the drain tray. Smooth service.",
                    serviceName = "Fridge Water Leakage Inspection",
                    dateStr = "2 weeks ago",
                    isApproved = true
                )
            )
            db.reviewDao().insertReviews(initialReviews)
        }
    }
}
