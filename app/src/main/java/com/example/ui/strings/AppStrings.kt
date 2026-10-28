package com.example.ui.strings

enum class Language {
    HI, // Hindi (Default)
    EN  // English
}

object AppStrings {
    fun brandName(lang: Language) = "Arham Cooling Solutions"

    fun tagline(lang: Language) = when (lang) {
        Language.HI -> "आपकी सुविधा, हमारी प्राथमिकता"
        Language.EN -> "Your Comfort, Our Priority"
    }

    fun headline(lang: Language) = when (lang) {
        Language.HI -> "घर बैठे एसी और फ्रिज रिपेयर"
        Language.EN -> "AC & Fridge Repair at Your Doorstep"
    }

    fun heroSub(lang: Language) = when (lang) {
        Language.HI -> "विश्वसनीय और त्वरित होम सर्विस। सर्टिफाइड तकनीशियन, उचित दाम और पारदर्शी सेवा।"
        Language.EN -> "Prompt doorstep repair, deep cleaning & gas service by verified technicians. Transparent pricing."
    }

    fun bookAService(lang: Language) = when (lang) {
        Language.HI -> "सर्विस बुक करें"
        Language.EN -> "Book a Service"
    }

    fun callNow(lang: Language) = when (lang) {
        Language.HI -> "कॉल करें"
        Language.EN -> "Call Now"
    }

    fun whatsappUs(lang: Language) = when (lang) {
        Language.HI -> "व्हाट्सएप करें"
        Language.EN -> "WhatsApp Us"
    }

    fun trackBooking(lang: Language) = when (lang) {
        Language.HI -> "बुकिंग स्टेटस देखें"
        Language.EN -> "Track Booking"
    }

    fun allServices(lang: Language) = when (lang) {
        Language.HI -> "सभी सेवाएं"
        Language.EN -> "All Services"
    }

    fun acServices(lang: Language) = when (lang) {
        Language.HI -> "एसी सेवाएं"
        Language.EN -> "AC Services"
    }

    fun fridgeServices(lang: Language) = when (lang) {
        Language.HI -> "रेफ्रिजरेटर सेवाएं"
        Language.EN -> "Fridge Services"
    }

    fun howItWorksTitle(lang: Language) = when (lang) {
        Language.HI -> "सर्विस कैसे काम करती है?"
        Language.EN -> "How It Works"
    }

    fun step1Title(lang: Language) = when (lang) {
        Language.HI -> "१. आसान बुकिंग"
        Language.EN -> "1. Simple Booking"
    }
    fun step1Desc(lang: Language) = when (lang) {
        Language.HI -> "अपनी जरूरत अनुसार एसी या फ्रिज सर्विस और पसंदीदा समय चुनें।"
        Language.EN -> "Select your required service and preferred date & time slot."
    }

    fun step2Title(lang: Language) = when (lang) {
        Language.HI -> "२. घर पर जांच (Inspection)"
        Language.EN -> "2. Doorstep Inspection"
    }
    fun step2Desc(lang: Language) = when (lang) {
        Language.HI -> "हमारे एक्सपर्ट तकनीशियन समय पर आपके घर पहुंच कर समस्या की जांच करेंगे।"
        Language.EN -> "Verified expert technicians inspect the appliance at your home."
    }

    fun step3Title(lang: Language) = when (lang) {
        Language.HI -> "३. उचित एस्टीमेट व रिपेयर"
        Language.EN -> "3. Clear Estimate & Repair"
    }
    fun step3Desc(lang: Language) = when (lang) {
        Language.HI -> "काम शुरू करने से पहले पूरा खर्च बताया जाएगा। मंजूरी के बाद तुरंत रिपेयर।"
        Language.EN -> "Clear quote provided before starting work. Fast fix with genuine parts."
    }

    fun step4Title(lang: Language) = when (lang) {
        Language.HI -> "४. वारंटी व आसान पेमेंट"
        Language.EN -> "4. Warranty & Easy Pay"
    }
    fun step4Desc(lang: Language) = when (lang) {
        Language.HI -> "संतोषजनक काम के बाद कैश या यूपीआई से भुगतान करें। 30 दिन की सर्विस गारंटी।"
        Language.EN -> "Pay via Cash or UPI only after work completion. 30-day service warranty."
    }

    fun whyChooseUsTitle(lang: Language) = when (lang) {
        Language.HI -> "हमें क्यों चुनें?"
        Language.EN -> "Why Choose Us"
    }

    fun reason1Title(lang: Language) = when (lang) {
        Language.HI -> "सर्टिफाइड और अनुभवी तकनीशियन"
        Language.EN -> "Verified & Experienced Technicians"
    }
    fun reason1Desc(lang: Language) = when (lang) {
        Language.HI -> "सभी तकनीशियन बैकग्राउंड वेरीफाइड और प्रशिक्षित हैं।"
        Language.EN -> "Skilled professionals trained on all leading AC & fridge brands."
    }

    fun reason2Title(lang: Language) = when (lang) {
        Language.HI -> "100% ओरिजिनल स्पेयर पार्ट्स"
        Language.EN -> "100% Genuine Spare Parts"
    }
    fun reason2Desc(lang: Language) = when (lang) {
        Language.HI -> "केवल प्रमाणित और ब्रांडेड पार्ट्स का उपयोग उचित दाम पर।"
        Language.EN -> "We strictly use authentic company spare parts with warranty."
    }

    fun reason3Title(lang: Language) = when (lang) {
        Language.HI -> "पारदर्शी मूल्य (कोई छिपा शुल्क नहीं)"
        Language.EN -> "Transparent & Upfront Pricing"
    }
    fun reason3Desc(lang: Language) = when (lang) {
        Language.HI -> "जांच शुल्क और रिपेयर शुल्क पहले से स्पष्ट।"
        Language.EN -> "Inspection and repair charges clearly stated upfront. No surprise fees."
    }

    fun reason4Title(lang: Language) = when (lang) {
        Language.HI -> "समय पर डोरस्टेप सेवा"
        Language.EN -> "On-Time Doorstep Service"
    }
    fun reason4Desc(lang: Language) = when (lang) {
        Language.HI -> "आपके चुने हुए समय पर त्वरित सेवा।"
        Language.EN -> "Flexible slots tailored to your busy schedule."
    }

    fun reviewsTitle(lang: Language) = when (lang) {
        Language.HI -> "संतुष्ट ग्राहकों की राय"
        Language.EN -> "Verified Customer Reviews"
    }

    fun faqsTitle(lang: Language) = when (lang) {
        Language.HI -> "अक्सर पूछे जाने वाले सवाल (FAQ)"
        Language.EN -> "Frequently Asked Questions"
    }

    fun serviceAreasTitle(lang: Language) = when (lang) {
        Language.HI -> "सेवा क्षेत्र व संपर्क विवरण"
        Language.EN -> "Service Areas & Contact Details"
    }

    fun bookNowBtn(lang: Language) = when (lang) {
        Language.HI -> "बुक करें"
        Language.EN -> "Book Now"
    }

    fun priceStartingFrom(lang: Language, amount: Int) = when (lang) {
        Language.HI -> "शुरुआती मूल्य: ₹$amount"
        Language.EN -> "Starting at: ₹$amount"
    }

    fun priceAfterInspection(lang: Language) = when (lang) {
        Language.HI -> "जांच के बाद कोटेशन"
        Language.EN -> "Price after inspection"
    }

    fun inspectionFee(lang: Language, amount: Int) = when (lang) {
        Language.HI -> "जांच शुल्क: ₹$amount"
        Language.EN -> "Inspection Charge: ₹$amount"
    }

    fun successBookingMessage(lang: Language) = when (lang) {
        Language.HI -> "Arham Cooling Solutions चुनने के लिए धन्यवाद! आपका बुकिंग अनुरोध प्राप्त हो गया है। हमारी टीम आपके समय की पुष्टि के लिए जल्द ही आपसे संपर्क करेगी।"
        Language.EN -> "Thank you for choosing Arham Cooling Solutions! Your booking request has been received. Our team will contact you to confirm your appointment."
    }

    fun bookingIdLabel(lang: Language) = when (lang) {
        Language.HI -> "बुकिंग आईडी"
        Language.EN -> "Booking ID"
    }

    fun customerNameLabel(lang: Language) = when (lang) {
        Language.HI -> "ग्राहक का नाम"
        Language.EN -> "Customer Name"
    }

    fun mobileNumberLabel(lang: Language) = when (lang) {
        Language.HI -> "मोबाइल नंबर"
        Language.EN -> "Mobile Number"
    }

    fun fullAddressLabel(lang: Language) = when (lang) {
        Language.HI -> "पूरा पता (मकान नं., गली, लैंडमार्क)"
        Language.EN -> "Full Address (House/Flat, Street, Landmark)"
    }

    fun localityLabel(lang: Language) = when (lang) {
        Language.HI -> "क्षेत्र / इलाका"
        Language.EN -> "Area / Locality"
    }

    fun selectDateLabel(lang: Language) = when (lang) {
        Language.HI -> "पसंदीदा तारीख"
        Language.EN -> "Preferred Date"
    }

    fun selectTimeSlotLabel(lang: Language) = when (lang) {
        Language.HI -> "पसंदीदा समय स्लॉट"
        Language.EN -> "Preferred Time Slot"
    }

    fun problemDescriptionLabel(lang: Language) = when (lang) {
        Language.HI -> "समस्या का विवरण (जैसे: कूलिंग कम है, पानी टपक रहा है)"
        Language.EN -> "Problem Description (e.g., Not cooling, Water leakage)"
    }

    fun preferredContactMethodLabel(lang: Language) = when (lang) {
        Language.HI -> "संपर्क का पसंदीदा माध्यम"
        Language.EN -> "Preferred Contact Method"
    }

    fun phoneCall(lang: Language) = when (lang) {
        Language.HI -> "फ़ोन कॉल"
        Language.EN -> "Phone Call"
    }

    fun submitBookingBtn(lang: Language) = when (lang) {
        Language.HI -> "बुकिंग अनुरोध भेजें"
        Language.EN -> "Submit Booking Request"
    }

    fun adminLoginTitle(lang: Language) = when (lang) {
        Language.HI -> "एडमिन पोर्टल लॉगिन"
        Language.EN -> "Admin Portal Login"
    }

    fun adminPinLabel(lang: Language) = when (lang) {
        Language.HI -> "एडमिन पिन दर्ज करें (Default: admin123)"
        Language.EN -> "Enter Admin PIN (Default: admin123)"
    }

    fun loginBtn(lang: Language) = when (lang) {
        Language.HI -> "लॉगिन करें"
        Language.EN -> "Login"
    }

    fun statusNew(lang: Language) = when (lang) {
        Language.HI -> "नया अनुरोध"
        Language.EN -> "New Request"
    }
    fun statusConfirmed(lang: Language) = when (lang) {
        Language.HI -> "पुष्ट (Confirmed)"
        Language.EN -> "Confirmed"
    }
    fun statusAssigned(lang: Language) = when (lang) {
        Language.HI -> "तकनीशियन नियुक्त"
        Language.EN -> "Technician Assigned"
    }
    fun statusInProgress(lang: Language) = when (lang) {
        Language.HI -> "काम चालू है"
        Language.EN -> "In Progress"
    }
    fun statusCompleted(lang: Language) = when (lang) {
        Language.HI -> "पूरा हुआ"
        Language.EN -> "Completed"
    }
    fun statusRescheduled(lang: Language) = when (lang) {
        Language.HI -> "समय बदला गया"
        Language.EN -> "Rescheduled"
    }
    fun statusCancelled(lang: Language) = when (lang) {
        Language.HI -> "रद्द किया गया"
        Language.EN -> "Cancelled"
    }

    fun statusLabel(status: String, lang: Language): String = when (status) {
        "NEW" -> statusNew(lang)
        "CONFIRMED" -> statusConfirmed(lang)
        "TECHNICIAN_ASSIGNED" -> statusAssigned(lang)
        "IN_PROGRESS" -> statusInProgress(lang)
        "COMPLETED" -> statusCompleted(lang)
        "RESCHEDULED" -> statusRescheduled(lang)
        "CANCELLED" -> statusCancelled(lang)
        else -> status
    }
}
