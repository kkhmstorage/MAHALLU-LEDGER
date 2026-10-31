package com.example.ui.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdditionalCollection
import com.example.data.model.AdditionalCollectionPayment
import com.example.data.model.Household
import com.example.data.model.HouseholdSubscriptionSummary
import com.example.ui.MadrasaViewModel
import com.example.ui.UserRole
import com.example.ui.components.AddHouseholdDialog
import com.example.ui.components.CollectSubscriptionDialog
import com.example.ui.components.CreateAdditionalCollectionDialog
import com.example.ui.components.EditHouseholdDialog
import com.example.ui.components.MemberProfileCompletionDialog
import com.example.ui.components.MonthlyNotificationDialog
import com.example.ui.components.PendingVerificationDialog
import com.example.ui.components.RecordAdditionalPaymentDialog
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.CashOutflowContainer
import com.example.ui.theme.CashOutflowRed
import com.example.ui.theme.StatusPendingAmber

@Composable
fun SubscriptionsScreen(
    viewModel: MadrasaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val summaries by viewModel.filteredHouseholdSummaries.collectAsState()
    val allSummaries by viewModel.householdSummaries.collectAsState()
    val searchQuery by viewModel.householdSearchQuery.collectAsState()
    val filterMode by viewModel.householdFilterMode.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val activeMember by viewModel.activeMemberHousehold.collectAsState()
    val additionalCollections by viewModel.additionalCollections.collectAsState()
    val additionalPayments by viewModel.additionalPayments.collectAsState()
    val pendingVerifications by viewModel.pendingVerifications.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Monthly Vari, 1: Additional Collections

    // Dialog States
    var activeSummaryForCollection by remember { mutableStateOf<HouseholdSubscriptionSummary?>(null) }
    var activeHouseholdForEdit by remember { mutableStateOf<Household?>(null) }
    var showAddHouseholdDialog by remember { mutableStateOf(false) }
    var showMonthlyNotificationDialog by remember { mutableStateOf(false) }
    var showPendingVerificationsDialog by remember { mutableStateOf(false) }
    var showMemberProfileDialog by remember { mutableStateOf(false) }
    var showCreateCollectionDialog by remember { mutableStateOf(false) }
    var activeCollectionForPayment by remember { mutableStateOf<AdditionalCollection?>(null) }

    val pendingDueSummaries = remember(allSummaries) {
        allSummaries.filter { it.totalPendingDue > 0 }
    }

    Scaffold(
        floatingActionButton = {
            if (role != UserRole.MEMBER) {
                FloatingActionButton(
                    onClick = {
                        if (selectedTab == 0) {
                            showAddHouseholdDialog = true
                        } else {
                            showCreateCollectionDialog = true
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("add_item_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = if (selectedTab == 0) "Add Family" else "Create Special Collection"
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Title
            item {
                Column {
                    Text(
                        text = "Mahal Collections & Subscriptions (വരിസംഖ്യ)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Monthly Vari, special funds, automated reminders & member profiles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Tab Navigation: Monthly Vari vs Additional Collections
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "പ്രതിമാസ വരിസംഖ്യ (${summaries.size})",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "അഡീഷണൽ കളക്ഷൻ (${additionalCollections.size})",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            // Member Self-Completion Alert Banner (For Member Login)
            if (role == UserRole.MEMBER && activeMember != null) {
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (activeMember?.verificationStatus == "VERIFIED") CashInflowContainer else Color(0xFFFFF8E1)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showMemberProfileDialog = true }
                            .testTag("member_profile_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (activeMember?.verificationStatus == "VERIFIED") CashInflowGreen else StatusPendingAmber),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (activeMember?.verificationStatus == "VERIFIED") Icons.Default.CheckCircle else Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (activeMember?.verificationStatus == "VERIFIED") "Verified Family Profile ✓" else "കുടുംബ വിവരങ്ങൾ പൂർത്തിയാക്കുക",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (activeMember?.verificationStatus == "VERIFIED") CashInflowGreen else Color(0xFF8D6E63)
                                    )
                                    Text(
                                        text = if (activeMember?.verificationStatus == "VERIFIED") "Data verified by Sub-Admin" else "Click to add/update family members & details",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = { showMemberProfileDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activeMember?.verificationStatus == "VERIFIED") CashInflowGreen else MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Update", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Sub-Admin Action Quick-Bar (Notification trigger & Pending Verifications)
            if (role != UserRole.MEMBER) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Monthly Reminder Button
                        Button(
                            onClick = { showMonthlyNotificationDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("open_monthly_notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "നോട്ടിഫിക്കേഷൻ (${pendingDueSummaries.size})",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }

                        // Verifications Button
                        OutlinedButton(
                            onClick = { showPendingVerificationsDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("open_verifications_button")
                        ) {
                            Icon(
                                imageVector = if (pendingVerifications.isNotEmpty()) Icons.Default.Warning else Icons.Default.Verified,
                                contentDescription = null,
                                tint = if (pendingVerifications.isNotEmpty()) StatusPendingAmber else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "സ്ഥിരീകരണം (${pendingVerifications.size})",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Tab 0: Monthly Vari View
            if (selectedTab == 0) {
                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setHouseholdSearchQuery(it) },
                        placeholder = { Text("Search by House No, Family or Phone...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setHouseholdSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("household_search_field")
                    )
                }

                // Filter Chips
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "ALL" to "All Families",
                            "WITH_DUES" to "Pending Dues",
                            "FULLY_PAID" to "Fully Cleared",
                            "PENDING_VERIFICATION" to "Pending Review"
                        ).forEach { (mode, label) ->
                            FilterChip(
                                selected = filterMode == mode,
                                onClick = { viewModel.setHouseholdFilterMode(mode) },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // List of Households
                if (summaries.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No households found matching your criteria",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(summaries, key = { it.household.id }) { summary ->
                        HouseholdCard(
                            summary = summary,
                            isTreasurer = role != UserRole.MEMBER,
                            onCollectClick = { activeSummaryForCollection = summary },
                            onEditClick = { activeHouseholdForEdit = summary.household },
                            onDeleteClick = { viewModel.deleteHousehold(summary.household) },
                            onSendReminder = {
                                viewModel.sendMonthlyWhatsAppReminder(
                                    context = context,
                                    household = summary.household,
                                    totalPendingDue = summary.totalPendingDue,
                                    pendingMonths = summary.pendingMonthsCount
                                )
                            }
                        )
                    }
                }
            } else {
                // Tab 1: Additional Collections View
                if (additionalCollections.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Handshake,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "അഡീഷണൽ കളക്ഷനുകൾ നിലവിലില്ല",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (role != UserRole.MEMBER) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(onClick = { showCreateCollectionDialog = true }) {
                                        Text("+ Create Special Collection")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    items(additionalCollections, key = { it.id }) { collection ->
                        val collectionPayments = remember(additionalPayments, collection.id) {
                            additionalPayments.filter { it.collectionId == collection.id }
                        }
                        val totalCollected = remember(collectionPayments) {
                            collectionPayments.sumOf { it.amount }
                        }

                        AdditionalCollectionCard(
                            collection = collection,
                            totalCollected = totalCollected,
                            payments = collectionPayments,
                            isSubAdmin = role != UserRole.MEMBER,
                            onRecordPayment = { activeCollectionForPayment = collection },
                            onDelete = { viewModel.deleteAdditionalCollection(collection) }
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    activeSummaryForCollection?.let { summary ->
        CollectSubscriptionDialog(
            summary = summary,
            onDismiss = { activeSummaryForCollection = null },
            onConfirm = { months, amountPerMonth, method, notes ->
                viewModel.recordPayment(
                    household = summary.household,
                    months = months,
                    amountPerMonth = amountPerMonth,
                    paymentMethod = method,
                    collectorName = "Treasurer",
                    notes = notes
                )
                activeSummaryForCollection = null
            }
        )
    }

    if (showAddHouseholdDialog) {
        AddHouseholdDialog(
            onDismiss = { showAddHouseholdDialog = false },
            onSave = { newHousehold ->
                viewModel.saveHousehold(newHousehold)
                showAddHouseholdDialog = false
            }
        )
    }

    // Sub-Admin Full Edit Household Dialog
    activeHouseholdForEdit?.let { household ->
        EditHouseholdDialog(
            household = household,
            onDismiss = { activeHouseholdForEdit = null },
            onSave = { updated ->
                viewModel.saveHousehold(updated)
                activeHouseholdForEdit = null
            },
            onVerifyAndSave = { verifiedHousehold ->
                viewModel.saveHousehold(verifiedHousehold)
                viewModel.confirmAndVerifyHousehold(verifiedHousehold)
                activeHouseholdForEdit = null
            }
        )
    }

    // Member Self-Completion Dialog
    if (showMemberProfileDialog) {
        val memberFamily = activeMember ?: summaries.firstOrNull()?.household
        memberFamily?.let { h ->
            MemberProfileCompletionDialog(
                household = h,
                onDismiss = { showMemberProfileDialog = false },
                onSubmit = { updated ->
                    viewModel.submitMemberProfileCompletion(updated)
                    showMemberProfileDialog = false
                }
            )
        }
    }

    // Monthly Notification & Reminder Dialog
    if (showMonthlyNotificationDialog) {
        MonthlyNotificationDialog(
            pendingSummaries = pendingDueSummaries,
            onDismiss = { showMonthlyNotificationDialog = false },
            onSendSingleReminder = { summary ->
                viewModel.sendMonthlyWhatsAppReminder(
                    context = context,
                    household = summary.household,
                    totalPendingDue = summary.totalPendingDue,
                    pendingMonths = summary.pendingMonthsCount
                )
            },
            onSendBatchBroadcast = {
                viewModel.sendBatchWhatsAppBroadcast(context)
            }
        )
    }

    // Pending Verifications Queue Dialog for Sub-Admin
    if (showPendingVerificationsDialog) {
        PendingVerificationDialog(
            pendingHouseholds = pendingVerifications,
            onDismiss = { showPendingVerificationsDialog = false },
            onConfirmVerification = { household ->
                viewModel.confirmAndVerifyHousehold(household)
            },
            onEditHousehold = { household ->
                showPendingVerificationsDialog = false
                activeHouseholdForEdit = household
            }
        )
    }

    // Create Additional Collection Dialog
    if (showCreateCollectionDialog) {
        CreateAdditionalCollectionDialog(
            onDismiss = { showCreateCollectionDialog = false },
            onSave = { title, desc, perFamily, totalFund, category, dueDate ->
                viewModel.createAdditionalCollection(title, desc, perFamily, totalFund, category, dueDate)
                showCreateCollectionDialog = false
            }
        )
    }

    // Record Payment towards an Additional Collection
    activeCollectionForPayment?.let { collection ->
        val householdList = allSummaries.map { it.household }
        RecordAdditionalPaymentDialog(
            collection = collection,
            households = householdList,
            preselectedHousehold = null,
            onDismiss = { activeCollectionForPayment = null },
            onSavePayment = { household, amount, mode, notes ->
                viewModel.recordAdditionalPayment(collection, household, amount, mode, notes)
                activeCollectionForPayment = null
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HouseholdCard(
    summary: HouseholdSubscriptionSummary,
    isTreasurer: Boolean,
    onCollectClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSendReminder: () -> Unit
) {
    val household = summary.household
    var menuExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("household_card_${household.houseNumber}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Row: House# Badge, Family Head, Family Name & Options Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = household.houseNumber,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = household.headOfFamily,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (household.verificationStatus == "VERIFIED") {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = CashInflowGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        if (household.familyName.isNotBlank()) {
                            Text(
                                text = "കുടുംബം: ${household.familyName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "${household.wardNumber} • ₹${household.monthlyFee.toInt()}/month",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isTreasurer) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit Family (എഡിറ്റ് ചെയ്യുക)") },
                                onClick = {
                                    menuExpanded = false
                                    onEditClick()
                                }
                            )
                            if (summary.totalPendingDue > 0) {
                                DropdownMenuItem(
                                    text = { Text("Send WhatsApp Reminder") },
                                    onClick = {
                                        menuExpanded = false
                                        onSendReminder()
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Delete Family", color = CashOutflowRed) },
                                onClick = {
                                    menuExpanded = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Phone and Member count pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (household.phoneNumber.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = household.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Demographics quick pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "അംഗങ്ങൾ: ${household.memberCount}" + if (household.rationCategory.isNotBlank()) " • ${household.rationCategory}" else "",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 12-Month Matrix Visual Grid
            Text(
                text = "Year 2026 Subscription Grid:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                summary.monthsStatus.forEach { monthStatus ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (monthStatus.isPaid) CashInflowContainer else CashOutflowContainer
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = monthStatus.monthName,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = if (monthStatus.isPaid) FontWeight.Bold else FontWeight.Normal,
                            color = if (monthStatus.isPaid) CashInflowGreen else CashOutflowRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Financial Status Bar and Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (summary.totalPendingDue > 0) {
                    Column {
                        Text(
                            text = "Due: ₹${"%,.0f".format(summary.totalPendingDue)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CashOutflowRed
                        )
                        Text(
                            text = "${summary.pendingMonthsCount} month(s) pending",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusPendingAmber
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CashInflowContainer
                    ) {
                        Text(
                            text = "All Cleared ✓",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = CashInflowGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isTreasurer && summary.totalPendingDue > 0) {
                        IconButton(
                            onClick = onSendReminder,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CashInflowContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send WhatsApp Reminder",
                                tint = CashInflowGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    if (isTreasurer) {
                        Button(
                            onClick = onCollectClick,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("collect_button_${household.houseNumber}")
                        ) {
                            Icon(imageVector = Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Collect Vari", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdditionalCollectionCard(
    collection: AdditionalCollection,
    totalCollected: Double,
    payments: List<AdditionalCollectionPayment>,
    isSubAdmin: Boolean,
    onRecordPayment: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (collection.targetTotalFund > 0) {
        (totalCollected / collection.targetTotalFund).toFloat().coerceIn(0f, 1f)
    } else {
        0.5f
    }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = collection.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Category: ${collection.category}" + if (collection.description.isNotBlank()) " • ${collection.description}" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "₹${collection.targetAmountPerFamily.toInt()}/family",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = CashInflowGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ശേഖരിച്ചത്: ₹${"%,.0f".format(totalCollected)}",
                    fontWeight = FontWeight.Bold,
                    color = CashInflowGreen,
                    fontSize = 13.sp
                )
                Text(
                    text = "ടാർഗറ്റ്: ₹${"%,.0f".format(collection.targetTotalFund)} (${payments.size} contributors)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSubAdmin) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onRecordPayment,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Record Payment", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
