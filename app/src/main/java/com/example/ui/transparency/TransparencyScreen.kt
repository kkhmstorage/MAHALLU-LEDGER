package com.example.ui.transparency

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DigitalReceiptData
import com.example.ui.MadrasaViewModel
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.CashOutflowContainer
import com.example.ui.theme.CashOutflowRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransparencyScreen(
    viewModel: MadrasaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsState()
    val netBalance by viewModel.netCashInHand.collectAsState()
    val subTotal by viewModel.currentYearSubscriptionTotal.collectAsState()
    val generalIncome by viewModel.totalGeneralIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val householdSummaries by viewModel.householdSummaries.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val verificationQuery by viewModel.verificationSearchQuery.collectAsState()
    val verifiedHousehold by viewModel.verifiedHouseholdResult.collectAsState()

    val totalHouseholds = householdSummaries.size
    val clearedHouseholds = householdSummaries.count { it.totalPendingDue <= 0 }
    val totalDues = householdSummaries.sumOf { it.totalPendingDue }

    // Category breakdown of expenses
    val expensesByCategory = allTransactions
        .filter { it.type == com.example.data.model.TransactionType.EXPENSE }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Transparency Header Banner
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Community Transparency Portal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Open audit ledger for every Mahal & Madrasa community member",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Section 1: Household Self-Verification Lookup
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Verify My Family's Subscription",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enter your House No (e.g. H-01) or Mobile Number to verify paid months & receipts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = verificationQuery,
                        onValueChange = { viewModel.setVerificationSearchQuery(it) },
                        placeholder = { Text("e.g. H-01 or 9846011223") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            if (verificationQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setVerificationSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verification_search_field")
                    )

                    if (verifiedHousehold != null) {
                        val vh = verifiedHousehold!!
                        val hh = vh.household

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${hh.houseNumber} • ${hh.headOfFamily}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${hh.wardNumber} • Monthly Rate: ₹${hh.monthlyFee.toInt()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (vh.totalPendingDue <= 0) CashInflowContainer else CashOutflowContainer
                            ) {
                                Text(
                                    text = if (vh.totalPendingDue <= 0) "All Paid ✓" else "₹${vh.totalPendingDue.toInt()} Due",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (vh.totalPendingDue <= 0) CashInflowGreen else CashOutflowRed,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "2026 Monthly Breakdown:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            vh.monthsStatus.forEach { monthStatus ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (monthStatus.isPaid) CashInflowContainer else CashOutflowContainer)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${monthStatus.monthName}: ${if (monthStatus.isPaid) "Paid ✓" else "Pending"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (monthStatus.isPaid) CashInflowGreen else CashOutflowRed
                                    )
                                }
                            }
                        }
                    } else if (verificationQuery.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No household found with '$verificationQuery'. Please check house number or contact the treasurer.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Section 2: Full Community Balance Sheet
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Year 2026 Financial Balance Sheet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Published by ${profile?.name ?: "Madrasa Committee"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    BalanceSheetRow(
                        label = "1. Mahal Subscriptions Collected",
                        amount = subTotal,
                        isPositive = true
                    )
                    BalanceSheetRow(
                        label = "2. General Donations & Juma Box",
                        amount = generalIncome,
                        isPositive = true
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(6.dp))

                    BalanceSheetRow(
                        label = "TOTAL COMMUNITY INFLOW",
                        amount = subTotal + generalIncome,
                        isPositive = true,
                        isBold = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BalanceSheetRow(
                        label = "3. Total Operational Expenses",
                        amount = totalExpenses,
                        isPositive = false
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Verified Net Balance
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CashInflowContainer)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NET AUDITED BALANCE",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CashInflowGreen
                        )
                        Text(
                            text = "₹${"%,.0f".format(netBalance)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = CashInflowGreen
                        )
                    }
                }
            }
        }

        // Section 3: Expense Category Breakdown
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Expenditure Breakdown by Purpose",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "How community funds are allocated for Madrasa and Mahal operations",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    expensesByCategory.forEach { (category, amount) ->
                        val percent = if (totalExpenses > 0) ((amount / totalExpenses) * 100).toInt() else 0

                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "₹${"%,.0f".format(amount)} ($percent%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CashOutflowRed
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(percent / 100f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Share Statement Action
        item {
            Button(
                onClick = {
                    val report = buildString {
                        appendLine("🏛️ *${profile?.name ?: "Madrasa & Mahal Committee"}*")
                        appendLine("📜 *TRANSPARENT FINANCIAL REPORT (2026)*")
                        appendLine("─────────────────────────")
                        appendLine("👥 Total Registered Families: $totalHouseholds")
                        appendLine("✅ Fully Cleared Families: $clearedHouseholds")
                        appendLine("─────────────────────────")
                        appendLine("💰 *INCOME SUMMARY*")
                        appendLine("• Mahal Subscriptions (Vari): ₹${"%,.0f".format(subTotal)}")
                        appendLine("• Donations & Friday Collections: ₹${"%,.0f".format(generalIncome)}")
                        appendLine("• *Total Income:* ₹${"%,.0f".format(subTotal + generalIncome)}")
                        appendLine("─────────────────────────")
                        appendLine("💸 *TOTAL EXPENSES:* ₹${"%,.0f".format(totalExpenses)}")
                        expensesByCategory.take(4).forEach { (cat, amt) ->
                            appendLine("  - $cat: ₹${"%,.0f".format(amt)}")
                        }
                        appendLine("─────────────────────────")
                        appendLine("🌟 *NET CASH BALANCE IN HAND:* ₹${"%,.0f".format(netBalance)}")
                        appendLine("─────────────────────────")
                        appendLine("Verified and published for transparent community audit.")
                    }
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, report)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Community Statement"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("share_statement_button")
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Transparent Statement (WhatsApp / SMS)")
            }
        }
    }
}

@Composable
private fun BalanceSheetRow(
    label: String,
    amount: Double,
    isPositive: Boolean,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                   else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${if (isPositive) "+" else "-"}₹${"%,.0f".format(amount)}",
            style = if (isBold) MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                   else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (isPositive) CashInflowGreen else CashOutflowRed
        )
    }
}
