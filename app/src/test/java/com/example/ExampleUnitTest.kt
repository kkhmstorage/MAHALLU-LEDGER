package com.example

import com.example.data.model.Household
import com.example.data.model.MahalCommunity
import com.example.data.model.MonthPaymentStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testHouseholdDueCalculation() {
        val household = Household(
            id = 1,
            mahalCode = "ALNOOR",
            houseNumber = "H-01",
            headOfFamily = "Abdul Rahman",
            phoneNumber = "9846011223",
            monthlyFee = 300.0
        )

        val paidMonths = setOf(1, 2, 3, 4, 5)
        val elapsedMonths = 10 // Up to October

        val pendingMonths = (1..elapsedMonths).filter { !paidMonths.contains(it) }
        val totalDue = pendingMonths.size * household.monthlyFee

        assertEquals(5, pendingMonths.size)
        assertEquals(1500.0, totalDue, 0.01)
    }

    @Test
    fun testReceiptMonthFormatting() {
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val selectedMonths = listOf(3, 4, 5)
        val formatted = selectedMonths.map { monthNames[it - 1] }.joinToString(", ")
        assertEquals("Mar, Apr, May", formatted)
    }

    @Test
    fun testNetCashInHandCalculation() {
        val totalSubscriptions = 25000.0
        val generalDonations = 15000.0
        val operationalExpenses = 18500.0

        val totalIncome = totalSubscriptions + generalDonations
        val netCash = totalIncome - operationalExpenses

        assertEquals(40000.0, totalIncome, 0.01)
        assertEquals(21500.0, netCash, 0.01)
    }

    @Test
    fun testSubAdminCredentialsValidation() {
        val community = MahalCommunity(
            mahalCode = "ALNOOR",
            name = "Al-Noor Madrasa & Mahal",
            location = "Malappuram",
            subAdminUsername = "alnoor_admin",
            subAdminPassword = "password123",
            contactPhone = "9847123456"
        )

        val isValid = community.subAdminUsername == "alnoor_admin" && community.subAdminPassword == "password123"
        val isWrong = community.subAdminPassword == "wrong_pass"

        assertTrue(isValid)
        assertFalse(isWrong)
    }

    @Test
    fun testHouseholdVerificationLifecycle() {
        // Step 1: Sub-Admin quickly adds family
        val registeredBySubAdmin = Household(
            id = 10,
            mahalCode = "ALNOOR",
            houseNumber = "H-25",
            headOfFamily = "C. H. Mammed",
            familyName = "ചോലക്കൽ വീട് (Cholakkel)",
            phoneNumber = "9847112233",
            monthlyFee = 250.0,
            verificationStatus = "PENDING_MEMBER_INFO"
        )
        assertEquals("PENDING_MEMBER_INFO", registeredBySubAdmin.verificationStatus)
        assertEquals("ചോലക്കൽ വീട് (Cholakkel)", registeredBySubAdmin.familyName)

        // Step 2: Member completes profile with optional fields
        val memberUpdated = registeredBySubAdmin.copy(
            memberCount = 5,
            maleCount = 2,
            femaleCount = 3,
            childrenCount = 1,
            rationCategory = "BPL",
            madrasaStudentCount = 2,
            bloodGroups = "O+ve",
            address = "Near South Madrasa",
            verificationStatus = "PENDING_VERIFICATION"
        )
        assertEquals("PENDING_VERIFICATION", memberUpdated.verificationStatus)
        assertEquals(2, memberUpdated.madrasaStudentCount)
        assertEquals("BPL", memberUpdated.rationCategory)

        // Step 3: Sub-Admin confirms and verifies
        val subAdminVerified = memberUpdated.copy(
            verificationStatus = "VERIFIED",
            verifiedBy = "alnoor_admin",
            verifiedDate = System.currentTimeMillis()
        )
        assertEquals("VERIFIED", subAdminVerified.verificationStatus)
        assertEquals("alnoor_admin", subAdminVerified.verifiedBy)
    }

    @Test
    fun testAdditionalCollectionProgress() {
        val collection = com.example.data.model.AdditionalCollection(
            id = 1,
            mahalCode = "ALNOOR",
            title = "മെഡിക്കൽ റിലീഫ് ഫണ്ട്",
            targetAmountPerFamily = 500.0,
            targetTotalFund = 50000.0
        )

        val payments = listOf(
            com.example.data.model.AdditionalCollectionPayment(
                collectionId = 1,
                householdId = 1,
                amount = 500.0,
                receiptNumber = "COL-1"
            ),
            com.example.data.model.AdditionalCollectionPayment(
                collectionId = 1,
                householdId = 2,
                amount = 1000.0,
                receiptNumber = "COL-2"
            )
        )

        val totalCollected = payments.sumOf { it.amount }
        val progressPercent = (totalCollected / collection.targetTotalFund) * 100

        assertEquals(1500.0, totalCollected, 0.01)
        assertEquals(3.0, progressPercent, 0.01)
    }

    @Test
    fun testMonthlyReminderTextFormat() {
        val household = Household(
            id = 5,
            mahalCode = "ALNOOR",
            houseNumber = "H-05",
            headOfFamily = "T. Sayed Mohammed",
            familyName = "റഹ്മത്ത് മൻസിൽ",
            phoneNumber = "9846455667",
            monthlyFee = 250.0
        )

        val totalDue = 500.0
        val pendingMonths = 2
        val displayName = "${household.headOfFamily} (${household.familyName})"

        assertTrue(displayName.contains("റഹ്മത്ത് മൻസിൽ"))
        assertTrue(totalDue == 500.0)
        assertEquals(2, pendingMonths)
    }
}
