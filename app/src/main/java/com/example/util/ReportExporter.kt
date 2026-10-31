package com.example.util

import android.content.Context
import android.content.Intent
import com.example.data.model.CashTransaction
import com.example.data.model.HouseholdSubscriptionSummary
import com.example.data.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportExporter {

    // 1. Generate CSV format for Excel/Spreadsheets of all households and dues
    fun generateHouseholdsCsv(
        communityName: String,
        year: Int,
        summaries: List<HouseholdSubscriptionSummary>
    ): String {
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val sb = StringBuilder()

        sb.appendLine("sep=,")
        sb.appendLine("\"# COMMUNITY: $communityName\"")
        sb.appendLine("\"# FINANCIAL YEAR: $year\"")
        sb.appendLine("\"# EXPORT TIMESTAMP: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\"")
        sb.appendLine()

        // CSV Header
        sb.append("House No,Family Head,Phone Number,Ward,Monthly Fee,Paid Months,Pending Dues,Total Paid (INR)")
        monthNames.forEach { m -> sb.append(",$m") }
        sb.appendLine()

        // Rows
        summaries.forEach { summary ->
            val hh = summary.household
            sb.append("\"${hh.houseNumber}\",")
            sb.append("\"${hh.headOfFamily.replace("\"", "\"\"")}\",")
            sb.append("\"${hh.phoneNumber}\",")
            sb.append("\"${hh.wardNumber}\",")
            sb.append("${hh.monthlyFee},")
            sb.append("${summary.paidMonthsCount},")
            sb.append("${summary.totalPendingDue},")
            sb.append("${summary.totalPaid}")

            summary.monthsStatus.forEach { ms ->
                sb.append(",")
                if (ms.isPaid) sb.append("PAID (${ms.receiptNumber ?: "YES"})")
                else sb.append("DUE")
            }
            sb.appendLine()
        }

        // Summary row
        val totalCollected = summaries.sumOf { it.totalPaid }
        val totalDues = summaries.sumOf { it.totalPendingDue }
        sb.appendLine()
        sb.appendLine("TOTALS,,,,,\"Total Dues:\",$totalDues,\"Total Collected:\",$totalCollected")

        return sb.toString()
    }

    // 2. Generate CSV for General Ledger Income and Expenses
    fun generateLedgerCsv(
        communityName: String,
        transactions: List<CashTransaction>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("sep=,")
        sb.appendLine("\"# COMMUNITY: $communityName - GENERAL LEDGER\"")
        sb.appendLine("\"# EXPORT TIMESTAMP: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\"")
        sb.appendLine()

        sb.appendLine("Voucher No,Date,Type,Category,Title,Description,Payment Mode,Amount (INR)")

        transactions.forEach { tx ->
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(tx.transactionDate))
            sb.append("\"${tx.voucherNumber}\",")
            sb.append("\"$dateStr\",")
            sb.append("\"${if (tx.type == TransactionType.INCOME) "INCOME" else "EXPENSE"}\",")
            sb.append("\"${tx.category}\",")
            sb.append("\"${tx.title.replace("\"", "\"\"")}\",")
            sb.append("\"${tx.description.replace("\"", "\"\"")}\",")
            sb.append("\"${tx.paymentMode}\",")
            sb.append("${if (tx.type == TransactionType.INCOME) tx.amount else -tx.amount}")
            sb.appendLine()
        }

        val totalInc = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExp = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val balance = totalInc - totalExp

        sb.appendLine()
        sb.appendLine("TOTAL INCOME:,$totalInc")
        sb.appendLine("TOTAL EXPENSES:,$totalExp")
        sb.appendLine("NET CASH BALANCE:,$balance")

        return sb.toString()
    }

    // 3. Generate Complete Database Snapshot JSON for Google Drive Backup
    fun generateFullDatabaseSnapshot(
        communityName: String,
        mahalCode: String,
        summaries: List<HouseholdSubscriptionSummary>,
        transactions: List<CashTransaction>
    ): String {
        val timeNow = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        val sb = StringBuilder()
        sb.appendLine("{")
        sb.appendLine("  \"app\": \"Mahal Ledger\",")
        sb.appendLine("  \"version\": \"2.0\",")
        sb.appendLine("  \"communityName\": \"$communityName\",")
        sb.appendLine("  \"mahalCode\": \"$mahalCode\",")
        sb.appendLine("  \"backupDate\": \"$timeNow\",")
        sb.appendLine("  \"totalHouseholds\": ${summaries.size},")
        sb.appendLine("  \"totalTransactions\": ${transactions.size},")
        sb.appendLine("  \"households\": [")

        summaries.forEachIndexed { index, s ->
            val h = s.household
            sb.append("    {")
            sb.append("\"houseNumber\": \"${h.houseNumber}\", ")
            sb.append("\"headOfFamily\": \"${h.headOfFamily.replace("\"", "\\\"")}\", ")
            sb.append("\"phone\": \"${h.phoneNumber}\", ")
            sb.append("\"monthlyFee\": ${h.monthlyFee}, ")
            sb.append("\"totalPaid\": ${s.totalPaid}, ")
            sb.append("\"totalPendingDue\": ${s.totalPendingDue}, ")
            sb.append("\"paidMonths\": ${s.paidMonthsCount}")
            sb.append("}")
            if (index < summaries.size - 1) sb.append(",")
            sb.appendLine()
        }

        sb.appendLine("  ],")
        sb.appendLine("  \"transactions\": [")

        transactions.forEachIndexed { index, tx ->
            sb.append("    {")
            sb.append("\"voucher\": \"${tx.voucherNumber}\", ")
            sb.append("\"type\": \"${tx.type}\", ")
            sb.append("\"category\": \"${tx.category}\", ")
            sb.append("\"title\": \"${tx.title.replace("\"", "\\\"")}\", ")
            sb.append("\"amount\": ${tx.amount}")
            sb.append("}")
            if (index < transactions.size - 1) sb.append(",")
            sb.appendLine()
        }

        sb.appendLine("  ]")
        sb.appendLine("}")
        return sb.toString()
    }

    // 4. Share File via Android Share Sheet
    fun shareCsvReport(context: Context, title: String, csvContent: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, csvContent)
            type = "text/csv"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share or Save Spreadsheet ($title)"))
    }
}
