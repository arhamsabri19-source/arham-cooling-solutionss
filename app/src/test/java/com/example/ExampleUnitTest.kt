package com.example

import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testBilingualStrings() {
        val hindiBrand = AppStrings.brandName(Language.HI)
        val hindiHeadline = AppStrings.headline(Language.HI)
        val englishHeadline = AppStrings.headline(Language.EN)

        assertEquals("Arham Cooling Solutions", hindiBrand)
        assertEquals("घर बैठे एसी और फ्रिज रिपेयर", hindiHeadline)
        assertEquals("AC & Fridge Repair at Your Doorstep", englishHeadline)
    }

    @Test
    fun testBillingCalculation() {
        val inspection = 199
        val repair = 500
        val parts = 1200
        val discount = 150
        val total = (inspection + repair + parts - discount).coerceAtLeast(0)

        assertEquals(1749, total)
    }
}
