package com.example.data.local

import com.example.data.model.CashTransaction
import com.example.data.model.CommunityProfile
import com.example.data.model.Household
import com.example.data.model.MahalCommunity
import com.example.data.model.SubscriptionPayment
import com.example.data.model.TransactionType

object DatabaseInitializer {
    suspend fun seedDatabase(database: AppDatabase) {
        val profileDao = database.communityProfileDao()
        val householdDao = database.householdDao()
        val paymentDao = database.subscriptionPaymentDao()
        val transactionDao = database.cashTransactionDao()
        val mahalDao = database.mahalCommunityDao()

        // 1. Initial Multi-Tenant Communities (Super-Admin created Mahals)
        val initialCommunities = listOf(
            MahalCommunity(
                mahalCode = "ALNOOR",
                name = "Al-Noor Madrasa & Mahal Committee",
                location = "Malappuram, Kerala",
                subAdminUsername = "alnoor_admin",
                subAdminPassword = "password123",
                contactPhone = "+91 98471 23456",
                regNumber = "REG-KL/2016/0942",
                defaultMonthlyFee = 250.0,
                isActive = true
            ),
            MahalCommunity(
                mahalCode = "DARUSALAM",
                name = "Darussalam Juma Masjid & Madrasa Mahal",
                location = "Kozhikode, Kerala",
                subAdminUsername = "darussalam_admin",
                subAdminPassword = "password123",
                contactPhone = "+91 98460 55443",
                regNumber = "REG-KL/2018/1120",
                defaultMonthlyFee = 300.0,
                isActive = true
            ),
            MahalCommunity(
                mahalCode = "BAITULHUDA",
                name = "Baitul Huda Islamic Centre & Mahal",
                location = "Ernakulam, Kerala",
                subAdminUsername = "baitulhuda_admin",
                subAdminPassword = "password123",
                contactPhone = "+91 98472 99887",
                regNumber = "REG-KL/2021/0432",
                defaultMonthlyFee = 200.0,
                isActive = true
            )
        )
        mahalDao.insertAll(initialCommunities)

        // 2. Initial Active Community Profile
        profileDao.insertProfile(
            CommunityProfile(
                id = 1,
                name = "Al-Noor Madrasa & Mahal Committee",
                registrationNumber = "REG-KL/2016/0942",
                location = "Malappuram, Kerala",
                phone = "+91 98471 23456",
                email = "contact@alnoormahal.org",
                currencySymbol = "₹",
                defaultMonthlyFee = 250.0,
                adminPin = "1234",
                currentFinancialYear = 2026
            )
        )

        // 3. Initial Households for ALNOOR
        val sampleHouseholds = listOf(
            Household(
                id = 1,
                mahalCode = "ALNOOR",
                houseNumber = "H-01",
                headOfFamily = "K. P. Abdul Rahman",
                familyName = "Baitul Noor",
                phoneNumber = "9846011223",
                monthlyFee = 300.0,
                wardNumber = "Ward 1 (East)",
                memberCount = 5,
                maleCount = 2,
                femaleCount = 3,
                childrenCount = 1,
                rationCategory = "APL",
                madrasaStudentCount = 1,
                bloodGroups = "O+ve, A+ve",
                address = "Baitul Noor, Near Juma Masjid",
                verificationStatus = "VERIFIED",
                notes = "Patron family"
            ),
            Household(
                id = 2,
                mahalCode = "ALNOOR",
                houseNumber = "H-02",
                headOfFamily = "M. C. Moideen Kutty",
                familyName = "Al-Falah Villa",
                phoneNumber = "9847122334",
                monthlyFee = 250.0,
                wardNumber = "Ward 1 (East)",
                memberCount = 4,
                maleCount = 2,
                femaleCount = 2,
                childrenCount = 1,
                rationCategory = "APL",
                madrasaStudentCount = 1,
                address = "Al-Falah Villa, Hospital Road",
                verificationStatus = "VERIFIED",
                notes = ""
            ),
            Household(
                id = 3,
                mahalCode = "ALNOOR",
                houseNumber = "H-03",
                headOfFamily = "V. K. Hamza Haji",
                familyName = "Darussalam",
                phoneNumber = "9846233445",
                monthlyFee = 500.0,
                wardNumber = "Ward 1 (East)",
                memberCount = 6,
                maleCount = 3,
                femaleCount = 3,
                childrenCount = 2,
                rationCategory = "APL",
                madrasaStudentCount = 2,
                address = "Darussalam, Post Office Junction",
                verificationStatus = "VERIFIED",
                notes = "Executive Committee Member"
            ),
            Household(
                id = 4,
                mahalCode = "ALNOOR",
                houseNumber = "H-04",
                headOfFamily = "P. K. Usman",
                familyName = "Madeena Manzil",
                phoneNumber = "9847344556",
                monthlyFee = 200.0,
                wardNumber = "Ward 1 (East)",
                memberCount = 4,
                maleCount = 2,
                femaleCount = 2,
                childrenCount = 2,
                rationCategory = "BPL",
                madrasaStudentCount = 2,
                bloodGroups = "O+ve, B+ve",
                address = "Madeena Manzil, School Lane",
                verificationStatus = "PENDING_VERIFICATION",
                notes = "Updated details by member via OTP"
            ),
            Household(
                id = 5,
                mahalCode = "ALNOOR",
                houseNumber = "H-05",
                headOfFamily = "T. Sayed Mohammed",
                phoneNumber = "9846455667",
                monthlyFee = 250.0,
                wardNumber = "Ward 1 (East)",
                memberCount = 4,
                address = "Rahmat Manzil, Bypass",
                notes = ""
            ),
            Household(
                id = 6,
                mahalCode = "ALNOOR",
                houseNumber = "H-06",
                headOfFamily = "K. T. Musthafa",
                phoneNumber = "9847566778",
                monthlyFee = 250.0,
                wardNumber = "Ward 2 (West)",
                memberCount = 5,
                address = "Bustan Villa, River View",
                notes = ""
            ),
            Household(
                id = 7,
                mahalCode = "ALNOOR",
                houseNumber = "H-07",
                headOfFamily = "C. H. Basheer",
                phoneNumber = "9846677889",
                monthlyFee = 200.0,
                wardNumber = "Ward 2 (West)",
                memberCount = 3,
                address = "Anugraha, Market Road",
                notes = ""
            ),
            Household(
                id = 8,
                mahalCode = "ALNOOR",
                houseNumber = "H-08",
                headOfFamily = "A. P. Faisal",
                phoneNumber = "9847788990",
                monthlyFee = 350.0,
                wardNumber = "Ward 2 (West)",
                memberCount = 4,
                address = "Al-Madeena, Hill View",
                notes = "Gulf family"
            ),
            Household(
                id = 9,
                mahalCode = "ALNOOR",
                houseNumber = "H-09",
                headOfFamily = "N. K. Shamsudheen",
                phoneNumber = "9846899001",
                monthlyFee = 250.0,
                wardNumber = "Ward 2 (West)",
                memberCount = 6,
                address = "Shams Manzil, Railway Gate",
                notes = ""
            ),
            Household(
                id = 10,
                mahalCode = "ALNOOR",
                houseNumber = "H-10",
                headOfFamily = "E. K. Rasheed",
                phoneNumber = "9847900112",
                monthlyFee = 200.0,
                wardNumber = "Ward 2 (West)",
                memberCount = 4,
                address = "Kausar Villa, Canal Road",
                notes = ""
            ),
            // Household for DARUSALAM community
            Household(
                id = 11,
                mahalCode = "DARUSALAM",
                houseNumber = "D-01",
                headOfFamily = "P. Mohammedali",
                phoneNumber = "9846111222",
                monthlyFee = 300.0,
                wardNumber = "Ward North",
                memberCount = 5,
                address = "Kozhikode Town",
                notes = ""
            )
        )
        householdDao.insertAll(sampleHouseholds)

        // 4. Subscription Payments for Year 2026
        val now = System.currentTimeMillis()
        val oneDay = 86400000L
        val payments = mutableListOf<SubscriptionPayment>()

        for (m in 1..10) {
            payments.add(
                SubscriptionPayment(
                    householdId = 1,
                    month = m,
                    year = 2026,
                    amountPaid = 300.0,
                    paymentDate = now - ((11 - m) * 30L * oneDay),
                    paymentMethod = if (m % 2 == 0) "UPI" else "CASH",
                    receiptNumber = "REC-2026-%03d".format(m),
                    collectedBy = "Treasurer - M. K. Basheer",
                    notes = "Monthly Vari"
                )
            )
        }

        for (m in 1..9) {
            payments.add(
                SubscriptionPayment(
                    householdId = 3,
                    month = m,
                    year = 2026,
                    amountPaid = 500.0,
                    paymentDate = now - ((10 - m) * 30L * oneDay),
                    paymentMethod = "BANK_TRANSFER",
                    receiptNumber = "REC-2026-%03d".format(20 + m),
                    collectedBy = "Treasurer - M. K. Basheer",
                    notes = "Direct Account Transfer"
                )
            )
        }

        for (m in 1..7) {
            payments.add(
                SubscriptionPayment(
                    householdId = 2,
                    month = m,
                    year = 2026,
                    amountPaid = 250.0,
                    paymentDate = now - ((11 - m) * 30L * oneDay),
                    paymentMethod = "CASH",
                    receiptNumber = "REC-2026-%03d".format(40 + m),
                    collectedBy = "Collector - Salam",
                    notes = ""
                )
            )
        }

        for (m in 1..10) {
            payments.add(
                SubscriptionPayment(
                    householdId = 8,
                    month = m,
                    year = 2026,
                    amountPaid = 350.0,
                    paymentDate = now - ((12 - m) * 28L * oneDay),
                    paymentMethod = "UPI",
                    receiptNumber = "REC-2026-%03d".format(60 + m),
                    collectedBy = "Treasurer - M. K. Basheer",
                    notes = "UPI Payment GPay"
                )
            )
        }

        for (m in 1..5) {
            payments.add(
                SubscriptionPayment(
                    householdId = 6,
                    month = m,
                    year = 2026,
                    amountPaid = 250.0,
                    paymentDate = now - ((10 - m) * 30L * oneDay),
                    paymentMethod = "CASH",
                    receiptNumber = "REC-2026-%03d".format(80 + m),
                    collectedBy = "Collector - Salam",
                    notes = ""
                )
            )
        }

        paymentDao.insertPayments(payments)

        // 5. Sample Cash Transactions for ALNOOR
        val transactions = listOf(
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.INCOME,
                category = "Friday Juma Collection",
                amount = 14250.0,
                transactionDate = now - (3 * oneDay),
                title = "Juma Box Collection (Week 43)",
                description = "Box opened in presence of President & Secretary",
                voucherNumber = "V-INC-104",
                paymentMode = "CASH"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.INCOME,
                category = "Special Donation",
                amount = 25000.0,
                transactionDate = now - (10 * oneDay),
                title = "Madrasa Renovation Sponsorship",
                description = "Donated by P. K. Brothers in memory of Late Kunhalan Kutty",
                voucherNumber = "V-INC-103",
                paymentMode = "BANK_TRANSFER"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.INCOME,
                category = "Friday Juma Collection",
                amount = 12800.0,
                transactionDate = now - (17 * oneDay),
                title = "Juma Box Collection (Week 42)",
                description = "Counted and deposited to committee cash box",
                voucherNumber = "V-INC-102",
                paymentMode = "CASH"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.INCOME,
                category = "Madrasa Admission & Book Fee",
                amount = 8600.0,
                transactionDate = now - (25 * oneDay),
                title = "Supplementary Syllabus & Textbooks",
                description = "Collected from newly enrolled batch",
                voucherNumber = "V-INC-101",
                paymentMode = "CASH"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.EXPENSE,
                category = "Staff Honorarium (Salary)",
                amount = 22000.0,
                transactionDate = now - (5 * oneDay),
                title = "Sadr Ustad & Teachers Monthly Allowance",
                description = "Distributed to 4 Madrasa teachers for current month",
                voucherNumber = "V-EXP-088",
                paymentMode = "BANK_TRANSFER"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.EXPENSE,
                category = "Electricity & Water",
                amount = 3450.0,
                transactionDate = now - (8 * oneDay),
                title = "KSEB Electricity Bill - Oct 2026",
                description = "Consumer # 115201402390 paid online",
                voucherNumber = "V-EXP-087",
                paymentMode = "UPI"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.EXPENSE,
                category = "Building Maintenance",
                amount = 4800.0,
                transactionDate = now - (14 * oneDay),
                title = "Classroom Fan Repairs & Painting Touchup",
                description = "Work done by Master Electricals (Receipt attached)",
                voucherNumber = "V-EXP-086",
                paymentMode = "CASH"
            ),
            CashTransaction(
                mahalCode = "ALNOOR",
                type = TransactionType.EXPENSE,
                category = "Stationery & Printing",
                amount = 1650.0,
                transactionDate = now - (21 * oneDay),
                title = "Exam Question Papers & Attendance Registers",
                description = "Printed at Ideal Offset Press",
                voucherNumber = "V-EXP-085",
                paymentMode = "CASH"
            )
        )
        transactionDao.insertAll(transactions)

        // 6. Initial Additional Collections (Special Funds)
        val additionalDao = database.additionalCollectionDao()
        val reliefFundId = additionalDao.insertCollection(
            com.example.data.model.AdditionalCollection(
                id = 1,
                mahalCode = "ALNOOR",
                title = "Medical Relief Fund",
                description = "Emergency medical aid fund drive for community members",
                targetAmountPerFamily = 500.0,
                targetTotalFund = 100000.0,
                category = "Relief",
                dueDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
            )
        )

        val buildingFundId = additionalDao.insertCollection(
            com.example.data.model.AdditionalCollection(
                id = 2,
                mahalCode = "ALNOOR",
                title = "Madrasa Renovation & Building Fund",
                description = "Classroom renovation and smart board installation",
                targetAmountPerFamily = 1000.0,
                targetTotalFund = 250000.0,
                category = "Construction",
                dueDate = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000)
            )
        )

        // Sample payment
        additionalDao.insertPayment(
            com.example.data.model.AdditionalCollectionPayment(
                collectionId = 1,
                householdId = 1,
                mahalCode = "ALNOOR",
                householdName = "K. P. Abdul Rahman",
                houseNumber = "H-01",
                amount = 500.0,
                receiptNumber = "RCP-COL-001",
                paymentMode = "UPI",
                collectorName = "Sub-Admin",
                notes = "Medical relief contribution"
            )
        )
    }
}
