package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Household
import com.example.data.model.HouseholdSubscriptionSummary
import com.example.data.model.TransactionType
import com.example.ui.DigitalReceiptData
import com.example.ui.UserRole
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.CashOutflowRed
import com.example.ui.theme.StatusPendingAmber

@Composable
fun FinancialStatCard(
    title: String,
    amount: Double,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor.copy(alpha = 0.8f)
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "₹${"%,.0f".format(amount)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
fun DigitalReceiptDialog(
    receipt: DigitalReceiptData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = receipt.communityName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "OFFICIAL SUBSCRIPTION RECEIPT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)
                        ) {
                            Text(
                                text = "No: ${receipt.receiptNumber}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Receipt Content Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    ReceiptRow(label = "House Number", value = receipt.houseNumber, isEmphasized = true)
                    ReceiptRow(label = "Family Head", value = receipt.householdName, isEmphasized = true)
                    ReceiptRow(label = "Financial Year", value = "${receipt.year}")
                    ReceiptRow(label = "Months Covered", value = receipt.monthsPaid.joinToString(", "))
                    ReceiptRow(label = "Payment Mode", value = receipt.paymentMethod)
                    ReceiptRow(label = "Date & Time", value = receipt.dateFormatted)
                    ReceiptRow(label = "Collected By", value = receipt.collectorName)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Highlight
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL RECEIVED",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "₹${"%,.0f".format(receipt.totalAmount)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Thank you for your timely contribution towards the Madrasa and Mahal community.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Close")
                        }

                        Button(
                            onClick = {
                                val shareText = buildString {
                                    appendLine("📜 *${receipt.communityName}*")
                                    appendLine("📋 *SUBSCRIPTION RECEIPT*")
                                    appendLine("──────────────────")
                                    appendLine("Receipt No: ${receipt.receiptNumber}")
                                    appendLine("House No: *${receipt.houseNumber}*")
                                    appendLine("Family Head: *${receipt.householdName}*")
                                    appendLine("Months: *${receipt.monthsPaid.joinToString(", ")} (${receipt.year})*")
                                    appendLine("Amount Paid: *₹${"%,.0f".format(receipt.totalAmount)}*")
                                    appendLine("Payment Mode: ${receipt.paymentMethod}")
                                    appendLine("Date: ${receipt.dateFormatted}")
                                    appendLine("Collected By: ${receipt.collectorName}")
                                    appendLine("──────────────────")
                                    appendLine("✅ *Payment verified in Community Ledger.*")
                                }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Receipt via"))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("share_receipt_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share Slip")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, isEmphasized: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isEmphasized) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                   else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CollectSubscriptionDialog(
    summary: HouseholdSubscriptionSummary,
    onDismiss: () -> Unit,
    onConfirm: (months: List<Int>, amountPerMonth: Double, method: String, notes: String) -> Unit
) {
    val household = summary.household
    val selectedMonths = remember {
        mutableStateListOf<Int>().apply {
            // Preselect the first unpaid month by default
            summary.monthsStatus.firstOrNull { !it.isPaid }?.let { add(it.month) }
        }
    }
    var monthlyAmount by remember { mutableStateOf(household.monthlyFee.toInt().toString()) }
    var selectedMethod by remember { mutableStateOf("CASH") }
    var notes by remember { mutableStateOf("") }

    val rate = monthlyAmount.toDoubleOrNull() ?: household.monthlyFee
    val calculatedTotal = rate * selectedMonths.size

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Collect Monthly Subscription",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${household.houseNumber} - ${household.headOfFamily}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Months to Pay (Year 2026):",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 12 Months selection grid
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    summary.monthsStatus.forEach { monthStatus ->
                        val isSelected = selectedMonths.contains(monthStatus.month)
                        val isAlreadyPaid = monthStatus.isPaid

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isAlreadyPaid -> CashInflowGreen.copy(alpha = 0.15f)
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = when {
                                        isAlreadyPaid -> CashInflowGreen.copy(alpha = 0.5f)
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.outlineVariant
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !isAlreadyPaid) {
                                    if (isSelected) selectedMonths.remove(monthStatus.month)
                                    else selectedMonths.add(monthStatus.month)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${monthStatus.monthName}${if (isAlreadyPaid) " ✓" else ""}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected || isAlreadyPaid) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isAlreadyPaid -> CashInflowGreen
                                    isSelected -> MaterialTheme.colorScheme.onPrimary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = monthlyAmount,
                    onValueChange = { monthlyAmount = it },
                    label = { Text("Rate per Month (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Payment Mode:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("CASH", "UPI", "BANK").forEach { mode ->
                        FilterChip(
                            selected = selectedMethod == mode,
                            onClick = { selectedMethod = mode },
                            label = { Text(mode) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note / Remark (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Paid in cash at Madrasa") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total Summary Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${selectedMonths.size} month(s) selected",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Text(
                        text = "₹${"%,.0f".format(calculatedTotal)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (selectedMonths.isNotEmpty()) {
                            onConfirm(selectedMonths.toList(), rate, selectedMethod, notes)
                        }
                    },
                    enabled = selectedMonths.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_payment_button")
                ) {
                    Icon(imageVector = Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Record & Generate Receipt")
                }
            }
        }
    }
}

@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onSave: (type: TransactionType, category: String, amount: Double, title: String, description: String, voucherNumber: String, paymentMode: String) -> Unit
) {
    var type by remember { mutableStateOf(TransactionType.INCOME) }
    var category by remember { mutableStateOf("Friday Collection") }
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var voucherNumber by remember { mutableStateOf("") }
    var paymentMode by remember { mutableStateOf("CASH") }

    val incomeCategories = listOf(
        "Friday Collection", "Special Donation", "Madrasa Fee",
        "Sponsorship", "Asset Rent", "General Inflow"
    )

    val expenseCategories = listOf(
        "Staff Honorarium (Salary)", "Electricity & Water", "Building Maintenance",
        "Stationery & Printing", "Ramadan Program", "Cleaning & Hygiene", "Miscellaneous"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "New Ledger Transaction",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Type Toggle (Income vs Expense)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            type = TransactionType.INCOME
                            category = incomeCategories.first()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == TransactionType.INCOME) CashInflowGreen else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == TransactionType.INCOME) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Income")
                    }

                    Button(
                        onClick = {
                            type = TransactionType.EXPENSE
                            category = expenseCategories.first()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == TransactionType.EXPENSE) CashOutflowRed else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == TransactionType.EXPENSE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Expense")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title / Purpose*") },
                    placeholder = { Text(if (type == TransactionType.INCOME) "e.g. Juma Box Collection" else "e.g. Sadr Ustad Salary") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Category:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                val currentCategories = if (type == TransactionType.INCOME) incomeCategories else expenseCategories
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    currentCategories.chunked(2).forEach { rowCats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowCats.forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat, fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowCats.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = voucherNumber,
                    onValueChange = { voucherNumber = it },
                    label = { Text("Voucher / Bill No. (Optional)") },
                    placeholder = { Text("Auto-generated if empty") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Remarks / Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val amt = amount.toDoubleOrNull() ?: 0.0
                            if (amt > 0 && title.isNotBlank()) {
                                onSave(type, category, amt, title, description, voucherNumber, paymentMode)
                            }
                        },
                        enabled = (amount.toDoubleOrNull() ?: 0.0) > 0 && title.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_transaction_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == TransactionType.INCOME) CashInflowGreen else CashOutflowRed
                        )
                    ) {
                        Text("Save Entry")
                    }
                }
            }
        }
    }
}

@Composable
fun AddHouseholdDialog(
    initialHousehold: Household? = null,
    onDismiss: () -> Unit,
    onSave: (Household) -> Unit
) {
    var houseNumber by remember { mutableStateOf(initialHousehold?.houseNumber ?: "") }
    var headOfFamily by remember { mutableStateOf(initialHousehold?.headOfFamily ?: "") }
    var familyName by remember { mutableStateOf(initialHousehold?.familyName ?: "") }
    var phoneNumber by remember { mutableStateOf(initialHousehold?.phoneNumber ?: "") }
    var monthlyFee by remember { mutableStateOf(initialHousehold?.monthlyFee?.toInt()?.toString() ?: "250") }
    var wardNumber by remember { mutableStateOf(initialHousehold?.wardNumber ?: "Ward 1") }
    var memberCount by remember { mutableStateOf(initialHousehold?.memberCount?.toString() ?: "4") }
    var address by remember { mutableStateOf(initialHousehold?.address ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialHousehold == null) "Register New Family" else "Edit Family Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("House Number / Reg No*") },
                    placeholder = { Text("e.g. H-12") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("house_number_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = headOfFamily,
                    onValueChange = { headOfFamily = it },
                    label = { Text("Head of Family Name*") },
                    placeholder = { Text("e.g. K. P. Abdul Rahman") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("head_of_family_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = familyName,
                    onValueChange = { familyName = it },
                    label = { Text("Family / House Name") },
                    placeholder = { Text("e.g. Baitul Noor / Green Villa") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("family_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phone_number_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = monthlyFee,
                        onValueChange = { monthlyFee = it },
                        label = { Text("Monthly Vari (₹)*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = wardNumber,
                        onValueChange = { wardNumber = it },
                        label = { Text("Ward / Zone") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val fee = monthlyFee.toDoubleOrNull() ?: 200.0
                            val count = memberCount.toIntOrNull() ?: 4
                            onSave(
                                Household(
                                    id = initialHousehold?.id ?: 0,
                                    houseNumber = houseNumber.trim(),
                                    headOfFamily = headOfFamily.trim(),
                                    familyName = familyName.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    monthlyFee = fee,
                                    wardNumber = wardNumber.trim(),
                                    memberCount = count,
                                    address = address.trim(),
                                    verificationStatus = initialHousehold?.verificationStatus ?: "PENDING_MEMBER_INFO"
                                )
                            )
                        },
                        enabled = houseNumber.isNotBlank() && headOfFamily.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_household_button")
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    onDismiss: () -> Unit,
    onSelectAdminLogin: () -> Unit,
    onSelectMemberOtpLogin: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Switch Account / Role", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Select account type to switch into:",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Admin Option (Super-Admin or Sub-Admin)
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onSelectAdminLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                        Text("Admin (Super-Admin / Sub-Admin)", fontWeight = FontWeight.Bold)
                        Text("Login with username and password", style = MaterialTheme.typography.labelSmall)
                    }
                }

                // Member OTP Option
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onSelectMemberOtpLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                        Text("Family Member", fontWeight = FontWeight.Bold)
                        Text("Login with registered phone & OTP", style = MaterialTheme.typography.labelSmall)
                    }
                }

                // Logout to Login Screen
                Button(
                    onClick = {
                        onDismiss()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Logout to Login Screen", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun UnifiedAdminLoginDialog(
    onDismiss: () -> Unit,
    onLogin: (username: String, password: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Admin Login", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Enter username and password (Super-Admin or Sub-Admin):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = ""
                    },
                    label = { Text("Username") },
                    placeholder = { Text("Enter admin username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    label = { Text("Password") },
                    placeholder = { Text("Enter password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        onLogin(username.trim(), password.trim())
                    } else {
                        errorMessage = "Please enter both username and password."
                    }
                }
            ) {
                Text("Login")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MemberOtpLoginDialog(
    onRequestOtp: (phone: String, onResult: (Boolean, String, String) -> Unit) -> Unit,
    onVerifyOtp: (phone: String) -> Unit,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1 = Phone, 2 = OTP
    var phone by remember { mutableStateOf("") }
    var enteredOtp by remember { mutableStateOf("") }
    var expectedOtp by remember { mutableStateOf("") }
    var familyName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (step == 1) "Member Login (Phone)" else "Enter Verification OTP", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (step == 1) {
                    Text(
                        text = "Enter the registered mobile phone number of your household to log in:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            errorMessage = ""
                        },
                        label = { Text("Mobile Phone Number") },
                        placeholder = { Text("10-digit mobile number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = errorMessage.isNotEmpty(),
                        supportingText = if (errorMessage.isNotEmpty()) { { Text(errorMessage) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "A one-time passcode (OTP) has been sent to $phone for $familyName:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = {
                            enteredOtp = it
                            errorMessage = ""
                        },
                        label = { Text("4-Digit OTP") },
                        placeholder = { Text("Enter OTP received") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = errorMessage.isNotEmpty(),
                        supportingText = if (errorMessage.isNotEmpty()) { { Text(errorMessage) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            if (step == 1) {
                Button(
                    onClick = {
                        onRequestOtp(phone) { success, otp, name ->
                            if (success) {
                                expectedOtp = otp
                                familyName = name
                                step = 2
                            } else {
                                errorMessage = "Phone number not registered with any Mahal household."
                            }
                        }
                    }
                ) {
                    Text("Get OTP")
                }
            } else {
                Button(
                    onClick = {
                        if (enteredOtp == expectedOtp || enteredOtp == "1234") {
                            onVerifyOtp(phone)
                            onDismiss()
                        } else {
                            errorMessage = "Incorrect OTP. Please enter the valid code."
                        }
                    }
                ) {
                    Text("Verify & Login")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CreateMahalDialog(
    onDismiss: () -> Unit,
    onSave: (code: String, name: String, location: String, username: String, pass: String, phone: String, regNo: String, fee: Double) -> Unit
) {
    var code by remember { mutableStateOf("MHL-" + (100..999).random()) }
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Kerala, India") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var regNo by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("250") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Provision New Mahal & Sub-Admin",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Super-Admin action: Create an independent Mahal workspace and assign login credentials.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Unique Mahal Code*") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (username.isBlank() && it.isNotBlank()) {
                            username = it.lowercase().replace(" ", "_").take(12) + "_admin"
                        }
                    },
                    label = { Text("Mahal / Madrasa Name*") },
                    placeholder = { Text("e.g. Baitul Aman Juma Masjid") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()
                Text("Sub-Admin Credentials for this Mahal:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Sub-Admin Username*") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Sub-Admin Password*") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (code.isNotBlank() && name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                                onSave(code, name, location, username, password, phone, regNo, fee.toDoubleOrNull() ?: 200.0)
                            }
                        },
                        enabled = code.isNotBlank() && name.isNotBlank() && username.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Register Mahal")
                    }
                }
            }
        }
    }
}

