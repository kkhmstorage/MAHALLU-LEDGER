package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CashTransaction
import com.example.data.model.CommunityProfile
import com.example.data.model.Household
import com.example.data.model.HouseholdSubscriptionSummary
import com.example.data.model.MahalCommunity
import com.example.data.model.MonthPaymentStatus
import com.example.data.model.SubscriptionPayment
import com.example.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MadrasaRepository(private val database: AppDatabase) {

    private val householdDao = database.householdDao()
    private val paymentDao = database.subscriptionPaymentDao()
    private val transactionDao = database.cashTransactionDao()
    private val profileDao = database.communityProfileDao()
    private val mahalDao = database.mahalCommunityDao()
    private val additionalDao = database.additionalCollectionDao()

    val profile: Flow<CommunityProfile?> = profileDao.getProfile()
    val allCommunities: Flow<List<MahalCommunity>> = mahalDao.getAllCommunities()

    // Active Tenant Mahal Code (Default: ALNOOR)
    val activeMahalCode = MutableStateFlow("ALNOOR")

    fun setActiveMahal(mahalCode: String) {
        activeMahalCode.value = mahalCode
    }

    // Scoped households for the active Mahal
    val activeHouseholds: Flow<List<Household>> = activeMahalCode.flatMapLatest { code ->
        householdDao.getHouseholdsByMahal(code)
    }

    // Scoped transactions for the active Mahal
    val activeTransactions: Flow<List<CashTransaction>> = activeMahalCode.flatMapLatest { code ->
        transactionDao.getTransactionsByMahal(code)
    }

    val recentPayments: Flow<List<SubscriptionPayment>> = paymentDao.getRecentPayments(20)

    fun getPaymentsForYear(year: Int): Flow<List<SubscriptionPayment>> =
        paymentDao.getPaymentsForYear(year)

    // Financial totals scoped to active Mahal
    val currentYearSubscriptionTotal: Flow<Double> = combine(activeHouseholds, getPaymentsForYear(2026)) { households, payments ->
        val householdIds = households.map { it.id }.toSet()
        payments.filter { householdIds.contains(it.householdId) }.sumOf { it.amountPaid }
    }

    val totalGeneralIncome: Flow<Double> = activeMahalCode.flatMapLatest { code ->
        transactionDao.getGeneralIncomeByMahal(code).map { it ?: 0.0 }
    }

    val totalExpenses: Flow<Double> = activeMahalCode.flatMapLatest { code ->
        transactionDao.getExpensesByMahal(code).map { it ?: 0.0 }
    }

    // Combined Live Financial Balance
    val netCashInHand: Flow<Double> = combine(
        currentYearSubscriptionTotal,
        totalGeneralIncome,
        totalExpenses
    ) { subs, income, expenses ->
        (subs + income) - expenses
    }

    private val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    // Household Summaries for active Mahal with 12-Month Payment Matrix
    fun getHouseholdSubscriptionSummaries(year: Int = 2026): Flow<List<HouseholdSubscriptionSummary>> {
        return combine(activeHouseholds, getPaymentsForYear(year)) { households, payments ->
            val paymentsByHousehold = payments.groupBy { it.householdId }
            val currentCalMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // 1..12

            households.map { household ->
                val householdPayments = paymentsByHousehold[household.id] ?: emptyList()
                val paidMonthsMap = householdPayments.associateBy { it.month }

                val monthsStatus = (1..12).map { month ->
                    val payment = paidMonthsMap[month]
                    MonthPaymentStatus(
                        month = month,
                        monthName = monthNames[month - 1],
                        isPaid = payment != null,
                        amountPaid = payment?.amountPaid ?: 0.0,
                        paymentDate = payment?.paymentDate,
                        receiptNumber = payment?.receiptNumber
                    )
                }

                val paidMonthsCount = monthsStatus.count { it.isPaid }
                val totalPaid = householdPayments.sumOf { it.amountPaid }

                val elapsedMonths = if (year == Calendar.getInstance().get(Calendar.YEAR)) currentCalMonth else 12
                val pendingMonthsCount = monthsStatus.take(elapsedMonths).count { !it.isPaid }
                val totalPendingDue = pendingMonthsCount * household.monthlyFee

                HouseholdSubscriptionSummary(
                    household = household,
                    paidMonthsCount = paidMonthsCount,
                    pendingMonthsCount = pendingMonthsCount,
                    totalPaid = totalPaid,
                    totalPendingDue = totalPendingDue,
                    monthsStatus = monthsStatus
                )
            }
        }
    }

    suspend fun recordSubscriptionPayment(
        householdId: Long,
        months: List<Int>,
        year: Int,
        amountPerMonth: Double,
        paymentMethod: String,
        collectedBy: String,
        notes: String
    ): List<SubscriptionPayment> {
        val createdPayments = mutableListOf<SubscriptionPayment>()
        val timeNow = System.currentTimeMillis()
        val randomSuffix = (100..999).random()

        months.sorted().forEachIndexed { index, month ->
            val receiptNo = "REC-$year-${SimpleDateFormat("MMdd", Locale.getDefault()).format(Date(timeNow))}-$householdId$month$randomSuffix"
            val payment = SubscriptionPayment(
                householdId = householdId,
                month = month,
                year = year,
                amountPaid = amountPerMonth,
                paymentDate = timeNow + (index * 100),
                paymentMethod = paymentMethod,
                receiptNumber = receiptNo,
                collectedBy = collectedBy,
                notes = notes
            )
            val id = paymentDao.insertPayment(payment)
            createdPayments.add(payment.copy(id = id))
        }
        return createdPayments
    }

    suspend fun addCashTransaction(
        type: TransactionType,
        category: String,
        amount: Double,
        title: String,
        description: String,
        voucherNumber: String,
        paymentMode: String
    ): Long {
        val autoVoucher = if (voucherNumber.isBlank()) {
            val prefix = if (type == TransactionType.INCOME) "V-INC" else "V-EXP"
            val time = SimpleDateFormat("MMdd-ss", Locale.getDefault()).format(Date())
            "$prefix-$time"
        } else {
            voucherNumber
        }

        val transaction = CashTransaction(
            mahalCode = activeMahalCode.value,
            type = type,
            category = category,
            amount = amount,
            transactionDate = System.currentTimeMillis(),
            title = title,
            description = description,
            voucherNumber = autoVoucher,
            paymentMode = paymentMode
        )
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun addOrUpdateHousehold(household: Household): Long {
        val scoped = if (household.mahalCode.isBlank()) household.copy(mahalCode = activeMahalCode.value) else household
        return householdDao.insertHousehold(scoped)
    }

    suspend fun deleteHousehold(household: Household) {
        householdDao.deleteHousehold(household)
    }

    suspend fun deleteTransaction(transaction: CashTransaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun updateProfile(profile: CommunityProfile) {
        profileDao.updateProfile(profile)
    }

    // Super-Admin: Register New Mahal Community
    suspend fun registerMahalCommunity(community: MahalCommunity) {
        mahalDao.insertCommunity(community)
    }

    suspend fun updateMahalCommunity(community: MahalCommunity) {
        mahalDao.updateCommunity(community)
    }

    suspend fun deleteMahalCommunity(community: MahalCommunity) {
        mahalDao.deleteCommunity(community)
    }

    // Sub-Admin Auth
    suspend fun authenticateSubAdmin(username: String, password: String): MahalCommunity? {
        return mahalDao.authenticateSubAdmin(username.trim(), password.trim())
    }

    // Member Phone Number Lookup for OTP login
    suspend fun findHouseholdByPhone(phone: String): Pair<Household, MahalCommunity?>? {
        val cleanPhone = phone.trim().replace("+91", "").replace(" ", "").replace("-", "")
        val household = householdDao.getHouseholdByPhone(cleanPhone)
            ?: householdDao.searchHousehold(cleanPhone).firstOrNull()
            ?: return null

        val community = mahalDao.getCommunityByCode(household.mahalCode)
        return Pair(household, community)
    }

    suspend fun getCommunityByCode(code: String): MahalCommunity? {
        return mahalDao.getCommunityByCode(code)
    }

    // Additional Collections (Special Funds)
    val activeAdditionalCollections: Flow<List<com.example.data.model.AdditionalCollection>> = activeMahalCode.flatMapLatest { code ->
        additionalDao.getCollectionsByMahal(code)
    }

    val activeAdditionalPayments: Flow<List<com.example.data.model.AdditionalCollectionPayment>> = activeMahalCode.flatMapLatest { code ->
        additionalDao.getAllPaymentsByMahal(code)
    }

    val pendingVerifications: Flow<List<Household>> = activeMahalCode.flatMapLatest { code ->
        householdDao.getPendingVerifications(code)
    }

    suspend fun createAdditionalCollection(collection: com.example.data.model.AdditionalCollection): Long {
        val scoped = if (collection.mahalCode.isBlank()) collection.copy(mahalCode = activeMahalCode.value) else collection
        return additionalDao.insertCollection(scoped)
    }

    suspend fun updateAdditionalCollection(collection: com.example.data.model.AdditionalCollection) {
        additionalDao.updateCollection(collection)
    }

    suspend fun deleteAdditionalCollection(collection: com.example.data.model.AdditionalCollection) {
        additionalDao.deleteCollection(collection)
    }

    suspend fun recordAdditionalPayment(
        collection: com.example.data.model.AdditionalCollection,
        household: Household,
        amount: Double,
        paymentMode: String,
        collectorName: String,
        notes: String
    ): Long {
        val count = java.util.concurrent.atomic.AtomicInteger(1)
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmm", Locale.US).format(Date())
        val receiptNo = "COL-${collection.id}-${household.houseNumber}-$timestamp"

        val payment = com.example.data.model.AdditionalCollectionPayment(
            collectionId = collection.id,
            householdId = household.id,
            mahalCode = collection.mahalCode,
            householdName = household.headOfFamily,
            houseNumber = household.houseNumber,
            amount = amount,
            paymentDate = System.currentTimeMillis(),
            receiptNumber = receiptNo,
            paymentMode = paymentMode,
            collectorName = collectorName,
            notes = notes
        )
        return additionalDao.insertPayment(payment)
    }

    fun getPaymentsForCollection(collectionId: Long): Flow<List<com.example.data.model.AdditionalCollectionPayment>> {
        return additionalDao.getPaymentsByCollection(collectionId)
    }

    fun getPaymentsForHousehold(householdId: Long): Flow<List<com.example.data.model.AdditionalCollectionPayment>> {
        return additionalDao.getPaymentsByHousehold(householdId)
    }

    // Sub-Admin Verification and Member Profile Update
    suspend fun confirmAndVerifyHousehold(householdId: Long, subAdminUsername: String) {
        householdDao.updateVerificationStatus(
            householdId = householdId,
            status = "VERIFIED",
            verifiedBy = subAdminUsername,
            verifiedDate = System.currentTimeMillis()
        )
    }

    suspend fun submitHouseholdForVerification(household: Household) {
        val updated = household.copy(
            verificationStatus = "PENDING_VERIFICATION"
        )
        householdDao.insertHousehold(updated)
    }
}
