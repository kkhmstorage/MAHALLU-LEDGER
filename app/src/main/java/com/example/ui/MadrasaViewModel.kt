package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AdditionalCollection
import com.example.data.model.AdditionalCollectionPayment
import com.example.data.model.CashTransaction
import com.example.data.model.CommunityProfile
import com.example.data.model.Household
import com.example.data.model.HouseholdSubscriptionSummary
import com.example.data.model.MahalCommunity
import com.example.data.model.SubscriptionPayment
import com.example.data.model.TransactionType
import com.example.data.repository.MadrasaRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    SUPER_ADMIN,
    SUB_ADMIN,
    MEMBER
}

data class DigitalReceiptData(
    val receiptNumber: String,
    val householdName: String,
    val houseNumber: String,
    val monthsPaid: List<String>,
    val year: Int,
    val totalAmount: Double,
    val paymentMethod: String,
    val dateFormatted: String,
    val collectorName: String,
    val communityName: String
)

class MadrasaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = MadrasaRepository(database)

    val cloudSyncManager = com.example.util.CloudSyncManager(viewModelScope)
    val syncState: StateFlow<com.example.util.SyncState> = cloudSyncManager.syncState
    val lastSyncTime: StateFlow<Long> = cloudSyncManager.lastSyncTime
    val pendingSyncCount: StateFlow<Int> = cloudSyncManager.pendingSyncCount

    val driveBackupManager = com.example.util.GoogleDriveBackupManager(viewModelScope)
    val driveBackupStatus: StateFlow<com.example.util.DriveBackupStatus> = driveBackupManager.backupStatus

    // Current Role
    private val _currentRole = MutableStateFlow(UserRole.SUB_ADMIN)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Active Tenant Community
    private val _activeMahal = MutableStateFlow<MahalCommunity?>(null)
    val activeMahal: StateFlow<MahalCommunity?> = _activeMahal.asStateFlow()

    val profile: StateFlow<CommunityProfile?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Logged in Member Household (when in Member role)
    private val _activeMemberHousehold = MutableStateFlow<Household?>(null)
    val activeMemberHousehold: StateFlow<Household?> = _activeMemberHousehold.asStateFlow()

    // All Communities (for Super-Admin management)
    val allCommunities: StateFlow<List<MahalCommunity>> = repository.allCommunities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Financial Year
    private val _selectedYear = MutableStateFlow(2026)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    // Financial Totals (scoped to active Mahal)
    val netCashInHand: StateFlow<Double> = repository.netCashInHand
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentYearSubscriptionTotal: StateFlow<Double> = repository.currentYearSubscriptionTotal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGeneralIncome: StateFlow<Double> = repository.totalGeneralIncome
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = repository.totalExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Household Summaries
    val householdSummaries: StateFlow<List<HouseholdSubscriptionSummary>> =
        repository.getHouseholdSubscriptionSummaries(2026)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Additional Collections & Special Funds
    val additionalCollections: StateFlow<List<AdditionalCollection>> = repository.activeAdditionalCollections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val additionalPayments: StateFlow<List<AdditionalCollectionPayment>> = repository.activeAdditionalPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingVerifications: StateFlow<List<Household>> = repository.pendingVerifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Transactions
    val allTransactions: StateFlow<List<CashTransaction>> = repository.activeTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentPayments: StateFlow<List<SubscriptionPayment>> = repository.recentPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Filters
    private val _householdSearchQuery = MutableStateFlow("")
    val householdSearchQuery: StateFlow<String> = _householdSearchQuery.asStateFlow()

    private val _householdFilterMode = MutableStateFlow("ALL") // "ALL", "WITH_DUES", "FULLY_PAID", "PENDING_VERIFICATION"
    val householdFilterMode: StateFlow<String> = _householdFilterMode.asStateFlow()

    val filteredHouseholdSummaries: StateFlow<List<HouseholdSubscriptionSummary>> = combine(
        householdSummaries,
        _householdSearchQuery,
        _householdFilterMode,
        _currentRole,
        _activeMemberHousehold
    ) { summaries, query, filter, role, activeMember ->
        val baseList = if (role == UserRole.MEMBER && activeMember != null) {
            summaries.filter { it.household.id == activeMember.id }
        } else {
            summaries
        }

        baseList.filter { summary ->
            val matchesQuery = query.isBlank() ||
                    summary.household.houseNumber.contains(query, ignoreCase = true) ||
                    summary.household.headOfFamily.contains(query, ignoreCase = true) ||
                    summary.household.familyName.contains(query, ignoreCase = true) ||
                    summary.household.phoneNumber.contains(query) ||
                    summary.household.wardNumber.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "WITH_DUES" -> summary.totalPendingDue > 0
                "FULLY_PAID" -> summary.totalPendingDue <= 0
                "PENDING_VERIFICATION" -> summary.household.verificationStatus == "PENDING_VERIFICATION"
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Transactions Filter
    private val _transactionTypeFilter = MutableStateFlow<TransactionType?>(null)
    val transactionTypeFilter: StateFlow<TransactionType?> = _transactionTypeFilter.asStateFlow()

    private val _transactionSearchQuery = MutableStateFlow("")
    val transactionSearchQuery: StateFlow<String> = _transactionSearchQuery.asStateFlow()

    val filteredTransactions: StateFlow<List<CashTransaction>> = combine(
        allTransactions,
        _transactionTypeFilter,
        _transactionSearchQuery
    ) { transactions, typeFilter, query ->
        transactions.filter { tx ->
            val matchesType = typeFilter == null || tx.type == typeFilter
            val matchesQuery = query.isBlank() ||
                    tx.title.contains(query, ignoreCase = true) ||
                    tx.category.contains(query, ignoreCase = true) ||
                    tx.voucherNumber.contains(query, ignoreCase = true) ||
                    tx.description.contains(query, ignoreCase = true)

            matchesType && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Generated Receipt for Dialog Display
    private val _activeReceipt = MutableStateFlow<DigitalReceiptData?>(null)
    val activeReceipt: StateFlow<DigitalReceiptData?> = _activeReceipt.asStateFlow()

    // Snackbar notifications
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    // Transparency Household Verification Search
    private val _verificationSearchQuery = MutableStateFlow("")
    val verificationSearchQuery: StateFlow<String> = _verificationSearchQuery.asStateFlow()

    val verifiedHouseholdResult: StateFlow<HouseholdSubscriptionSummary?> = combine(
        householdSummaries,
        _verificationSearchQuery,
        _activeMemberHousehold
    ) { summaries, query, loggedMember ->
        if (loggedMember != null) {
            summaries.firstOrNull { it.household.id == loggedMember.id }
        } else if (query.trim().isBlank()) {
            null
        } else {
            val q = query.trim()
            summaries.firstOrNull {
                it.household.houseNumber.equals(q, ignoreCase = true) ||
                it.household.phoneNumber.contains(q)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Load default Mahal community on startup
        viewModelScope.launch {
            val initial = repository.getCommunityByCode("ALNOOR")
            _activeMahal.value = initial
        }
    }

    // Role and Community Switching
    fun setRole(role: UserRole) {
        _currentRole.value = role
        viewModelScope.launch {
            _snackbarEvent.emit("Switched to ${role.name.replace("_", " ")} mode")
        }
    }

    fun switchActiveMahal(community: MahalCommunity) {
        _activeMahal.value = community
        repository.setActiveMahal(community.mahalCode)
        viewModelScope.launch {
            _snackbarEvent.emit("Switched to ${community.name}")
        }
    }

    // Sub-Admin Authentication
    fun loginSubAdmin(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val community = repository.authenticateSubAdmin(username, password)
            if (community != null) {
                _activeMahal.value = community
                repository.setActiveMahal(community.mahalCode)
                _currentRole.value = UserRole.SUB_ADMIN
                _activeMemberHousehold.value = null
                _snackbarEvent.emit("Welcome, Sub-Admin of ${community.name}")
                onResult(true, "Success")
            } else {
                onResult(false, "Invalid Sub-Admin username or password")
            }
        }
    }

    // Super-Admin Authentication (Master Password)
    fun loginSuperAdmin(masterPin: String, onResult: (Boolean) -> Unit) {
        if (masterPin == "9999" || masterPin == "admin123") {
            _currentRole.value = UserRole.SUPER_ADMIN
            _activeMemberHousehold.value = null
            viewModelScope.launch {
                _snackbarEvent.emit("Logged into Super-Admin Control Panel")
            }
            onResult(true)
        } else {
            onResult(false)
        }
    }

    // Member Phone + OTP Login
    fun requestMemberOtp(phone: String, onResult: (Boolean, String, String) -> Unit) {
        viewModelScope.launch {
            val match = repository.findHouseholdByPhone(phone)
            if (match != null) {
                val (household, community) = match
                // Generate a 4-digit verification OTP
                val generatedOtp = (1000..9999).random().toString()
                onResult(true, generatedOtp, household.headOfFamily)
            } else {
                onResult(false, "", "No registered family found with phone: $phone")
            }
        }
    }

    fun verifyAndLoginMember(phone: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val match = repository.findHouseholdByPhone(phone)
            if (match != null) {
                val (household, community) = match
                community?.let {
                    _activeMahal.value = it
                    repository.setActiveMahal(it.mahalCode)
                }
                _activeMemberHousehold.value = household
                _currentRole.value = UserRole.MEMBER
                _snackbarEvent.emit("Welcome, ${household.headOfFamily} (${household.houseNumber})")
                onComplete()
            }
        }
    }

    // Super Admin: Create a New Mahal with Sub-Admin Credentials
    fun createNewMahal(
        code: String,
        name: String,
        location: String,
        subAdminUsername: String,
        subAdminPassword: String,
        contactPhone: String,
        regNumber: String,
        monthlyFee: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val newCommunity = MahalCommunity(
                mahalCode = code.trim().uppercase(),
                name = name.trim(),
                location = location.trim(),
                subAdminUsername = subAdminUsername.trim(),
                subAdminPassword = subAdminPassword.trim(),
                contactPhone = contactPhone.trim(),
                regNumber = regNumber.trim(),
                defaultMonthlyFee = monthlyFee,
                isActive = true
            )
            repository.registerMahalCommunity(newCommunity)
            _snackbarEvent.emit("New Mahal '${name}' registered with Sub-Admin credentials!")
            onSuccess()
        }
    }

    fun toggleMahalStatus(community: MahalCommunity) {
        viewModelScope.launch {
            val updated = community.copy(isActive = !community.isActive)
            repository.updateMahalCommunity(updated)
            _snackbarEvent.emit("${community.name} is now ${if (updated.isActive) "Active" else "Suspended"}")
        }
    }

    fun setHouseholdSearchQuery(query: String) {
        _householdSearchQuery.value = query
    }

    fun setHouseholdFilterMode(mode: String) {
        _householdFilterMode.value = mode
    }

    fun setTransactionTypeFilter(type: TransactionType?) {
        _transactionTypeFilter.value = type
    }

    fun setTransactionSearchQuery(query: String) {
        _transactionSearchQuery.value = query
    }

    fun setVerificationSearchQuery(query: String) {
        _verificationSearchQuery.value = query
    }

    fun dismissReceipt() {
        _activeReceipt.value = null
    }

    fun showReceipt(receipt: DigitalReceiptData) {
        _activeReceipt.value = receipt
    }

    fun recordPayment(
        household: Household,
        months: List<Int>,
        amountPerMonth: Double,
        paymentMethod: String,
        collectorName: String,
        notes: String
    ) {
        if (months.isEmpty()) return
        viewModelScope.launch {
            val payments = repository.recordSubscriptionPayment(
                householdId = household.id,
                months = months,
                year = _selectedYear.value,
                amountPerMonth = amountPerMonth,
                paymentMethod = paymentMethod,
                collectedBy = collectorName.ifBlank { "Treasurer" },
                notes = notes
            )

            val monthNamesList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            val monthsText = months.sorted().map { monthNamesList[it - 1] }
            val totalAmount = amountPerMonth * months.size
            val primaryReceiptNo = payments.firstOrNull()?.receiptNumber ?: "REC-${System.currentTimeMillis()}"

            val receipt = DigitalReceiptData(
                receiptNumber = primaryReceiptNo,
                householdName = household.headOfFamily,
                houseNumber = household.houseNumber,
                monthsPaid = monthsText,
                year = _selectedYear.value,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                dateFormatted = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
                collectorName = collectorName.ifBlank { "Treasurer" },
                communityName = activeMahal.value?.name ?: "Madrasa & Mahal Committee"
            )

            _activeReceipt.value = receipt
            _snackbarEvent.emit("Payment of ₹${totalAmount.toInt()} recorded successfully!")
            cloudSyncManager.markLocalMutation()
        }
    }

    fun addTransaction(
        type: TransactionType,
        category: String,
        amount: Double,
        title: String,
        description: String,
        voucherNumber: String,
        paymentMode: String
    ) {
        viewModelScope.launch {
            repository.addCashTransaction(
                type = type,
                category = category,
                amount = amount,
                title = title,
                description = description,
                voucherNumber = voucherNumber,
                paymentMode = paymentMode
            )
            _snackbarEvent.emit("${if (type == TransactionType.INCOME) "Income" else "Expense"} of ₹${amount.toInt()} logged!")
            cloudSyncManager.markLocalMutation()
        }
    }

    fun saveHousehold(household: Household) {
        viewModelScope.launch {
            val currentCode = activeMahal.value?.mahalCode ?: "ALNOOR"
            val toSave = household.copy(mahalCode = currentCode)
            repository.addOrUpdateHousehold(toSave)
            _snackbarEvent.emit("Household ${household.houseNumber} saved!")
            cloudSyncManager.markLocalMutation()
        }
    }

    fun deleteHousehold(household: Household) {
        viewModelScope.launch {
            repository.deleteHousehold(household)
            _snackbarEvent.emit("Household ${household.houseNumber} removed")
            cloudSyncManager.markLocalMutation()
        }
    }

    fun deleteTransaction(transaction: CashTransaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            _snackbarEvent.emit("Transaction removed")
            cloudSyncManager.markLocalMutation()
        }
    }

    fun triggerSync() {
        cloudSyncManager.triggerSync { msg ->
            viewModelScope.launch {
                _snackbarEvent.emit(msg)
            }
        }
    }

    fun exportHouseholdsCsv(context: android.content.Context) {
        val mahalName = activeMahal.value?.name ?: "Mahal"
        val csv = com.example.util.ReportExporter.generateHouseholdsCsv(mahalName, _selectedYear.value, householdSummaries.value)
        com.example.util.ReportExporter.shareCsvReport(context, "$mahalName - Subscriptions ${_selectedYear.value}", csv)
    }

    fun exportLedgerCsv(context: android.content.Context) {
        val mahalName = activeMahal.value?.name ?: "Mahal"
        val csv = com.example.util.ReportExporter.generateLedgerCsv(mahalName, allTransactions.value)
        com.example.util.ReportExporter.shareCsvReport(context, "$mahalName - Accounts Ledger", csv)
    }

    fun backupToGoogleDrive(context: android.content.Context) {
        val mahalName = activeMahal.value?.name ?: "Mahal"
        val mahalCode = activeMahal.value?.mahalCode ?: "ALNOOR"
        val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
        val fileName = "${mahalCode}_Mahal_Ledger_Backup_$timestamp.json"

        val snapshotJson = com.example.util.ReportExporter.generateFullDatabaseSnapshot(
            communityName = mahalName,
            mahalCode = mahalCode,
            summaries = householdSummaries.value,
            transactions = allTransactions.value
        )

        driveBackupManager.uploadBackupToDrive(
            context = context,
            mahalName = mahalName,
            backupContent = snapshotJson,
            fileName = fileName
        ) { _, msg ->
            viewModelScope.launch {
                _snackbarEvent.emit(msg)
            }
        }
    }

    // Additional Collections (Special Funds) Management
    fun createAdditionalCollection(
        title: String,
        description: String,
        targetAmountPerFamily: Double,
        targetTotalFund: Double,
        category: String,
        dueDate: Long
    ) {
        viewModelScope.launch {
            val collection = AdditionalCollection(
                mahalCode = activeMahal.value?.mahalCode ?: "ALNOOR",
                title = title.trim(),
                description = description.trim(),
                targetAmountPerFamily = targetAmountPerFamily,
                targetTotalFund = targetTotalFund,
                category = category.trim(),
                dueDate = dueDate
            )
            repository.createAdditionalCollection(collection)
            _snackbarEvent.emit("Special collection created: $title")
        }
    }

    fun recordAdditionalPayment(
        collection: AdditionalCollection,
        household: Household,
        amount: Double,
        paymentMode: String,
        notes: String
    ) {
        viewModelScope.launch {
            val collector = if (currentRole.value == UserRole.SUB_ADMIN) {
                activeMahal.value?.subAdminUsername ?: "Sub-Admin"
            } else {
                "Collector"
            }
            repository.recordAdditionalPayment(
                collection = collection,
                household = household,
                amount = amount,
                paymentMode = paymentMode,
                collectorName = collector,
                notes = notes
            )
            _snackbarEvent.emit("Payment of ₹${amount.toInt()} recorded for ${household.headOfFamily}")
        }
    }

    fun deleteAdditionalCollection(collection: AdditionalCollection) {
        viewModelScope.launch {
            repository.deleteAdditionalCollection(collection)
            _snackbarEvent.emit("Collection deleted: ${collection.title}")
        }
    }

    // Household Verification Flow
    fun confirmAndVerifyHousehold(household: Household) {
        viewModelScope.launch {
            val subAdminName = activeMahal.value?.subAdminUsername ?: "Sub-Admin"
            repository.confirmAndVerifyHousehold(household.id, subAdminName)
            _snackbarEvent.emit("Family details confirmed & verified for ${household.headOfFamily} (${household.houseNumber})")
        }
    }

    // Member Self-Completion Flow
    fun submitMemberProfileCompletion(updatedHousehold: Household) {
        viewModelScope.launch {
            repository.submitHouseholdForVerification(updatedHousehold)
            _activeMemberHousehold.value = updatedHousehold
            _snackbarEvent.emit("Family details submitted for Sub-Admin verification!")
        }
    }

    // Monthly Vari Notification Helper
    fun generateMonthlyReminderText(household: Household, totalPendingDue: Double, pendingMonths: Int): String {
        val mahalName = activeMahal.value?.name ?: "Mahal Madrasa Committee"
        val houseDisplayName = if (household.familyName.isNotBlank()) "${household.headOfFamily} (${household.familyName})" else household.headOfFamily
        return """
            അസ്സലാമു അലൈകും $houseDisplayName,
            
            $mahalName-ൽ നിന്നുള്ള പ്രതിമാസ വരിസംഖ്യാ ഓർമ്മപ്പെടുത്തൽ:
            • വീട്ടു നമ്പർ: ${household.houseNumber}
            • വാർഡ്: ${household.wardNumber}
            • കുടിശ്ശിക തുക: ₹${totalPendingDue.toInt()} ($pendingMonths മാസങ്ങൾ)
            
            ദയവായി വരിസംഖ്യ മഹല്ല് ഓഫീസിലോ വരിസംഖ്യ കളക്ടർ മുഖേനയോ അടയ്ക്കണമെന്ന് താല്പര്യപ്പെടുന്നു. 
            (Digital Receipt will be generated upon payment).
            ജസാക്കല്ലാഹു ഖൈർ!
        """.trimIndent()
    }

    fun sendMonthlyWhatsAppReminder(
        context: Context,
        household: Household,
        totalPendingDue: Double,
        pendingMonths: Int
    ) {
        try {
            val message = generateMonthlyReminderText(household, totalPendingDue, pendingMonths)
            val cleanPhone = household.phoneNumber.trim().replace("+", "").replace(" ", "").replace("-", "")
            val fullPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
            val url = "https://api.whatsapp.com/send?phone=$fullPhone&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to generic share
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, generateMonthlyReminderText(household, totalPendingDue, pendingMonths))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Vari Reminder"))
        }
    }

    fun generateBatchBroadcastSummary(): String {
        val mahalName = activeMahal.value?.name ?: "Mahal Madrasa Committee"
        val pendingList = householdSummaries.value.filter { it.totalPendingDue > 0 }
        val totalDue = pendingList.sumOf { it.totalPendingDue }
        val currentMonth = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.ENGLISH).format(java.util.Date())

        return """
            📢 *${mahalName} - പ്രതിമാസ വരിസംഖ്യാ നോട്ടിഫിക്കേഷൻ ($currentMonth)*
            
            മാന്യ മഹല്ല് നിവാസികളുടെ ശ്രദ്ധയ്ക്ക്,
            ഈ മാസത്തെ മദ്റസ / മഹല്ല് വരിസംഖ്യ ശേഖരണം പുരോഗമിക്കുകയാണ്. 
            
            • ആകെ കുടിശ്ശികയുള്ള കുടുംബങ്ങൾ: ${pendingList.size}
            • ആകെ ബാക്കി തുക: ₹${totalDue.toInt()}
            
            വരിസംഖ്യ ഇതുവരെ അടയ്ക്കാത്ത കുടുംബങ്ങൾ എത്രയും വേഗം മഹല്ല് ഓഫീസിലോ ചുമതലപ്പെട്ട കളക്ടർമാർ വഴിയോ അടച്ചു രസീത് കൈപ്പറ്റണമെന്ന് അഭ്യർത്ഥിക്കുന്നു.
            
            സുതാര്യമായ വരവു-ചെലവ് വിവരങ്ങൾക്കും രസീതുകൾക്കും മഹല്ല് ആപ്പിൽ മൊബൈൽ നമ്പർ ഉപയോഗിച്ച് ലോഗിൻ ചെയ്യാവുന്നതാണ്.
            
            - സെക്രട്ടറി / ട്രഷറർ, $mahalName
        """.trimIndent()
    }

    fun sendBatchWhatsAppBroadcast(context: Context) {
        val summaryText = generateBatchBroadcastSummary()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summaryText)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(Intent.createChooser(intent, "Share Mahal Vari Broadcast"))
    }
}
