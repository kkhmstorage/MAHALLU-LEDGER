/**
 * Mahal Ledger - Web Application Core Engine
 * Full Multi-Tenant Accounting & Subscriptions System for Web & Desktop
 */

// 1. Initial Default Seed Data
const DEFAULT_DATA = {
    activeMahalCode: "ALNOOR",
    currentYear: 2026,
    communities: [
        {
            mahalCode: "ALNOOR",
            name: "Al-Noor Madrasa & Mahal Committee",
            location: "Malappuram, Kerala",
            subAdminUsername: "alnoor_admin",
            subAdminPassword: "password123",
            contactPhone: "+91 98471 23456",
            regNumber: "REG-KL/2016/0942",
            defaultMonthlyFee: 250,
            bankName: "State Bank of India (Valanchery)",
            accountNumber: "67324419082",
            ifscCode: "SBIN0070188",
            upiId: "alnoormahal@sbi",
            isActive: true,
            expiryDate: "2027-12-31"
        },
        {
            mahalCode: "DARUSALAM",
            name: "Darussalam Juma Masjid & Madrasa Mahal",
            location: "Kozhikode, Kerala",
            subAdminUsername: "darussalam_admin",
            subAdminPassword: "password123",
            contactPhone: "+91 98460 55443",
            regNumber: "REG-KL/2018/1120",
            defaultMonthlyFee: 300,
            bankName: "Federal Bank",
            accountNumber: "1482010023419",
            ifscCode: "FDRL0001482",
            upiId: "darussalammahal@fbl",
            isActive: true,
            expiryDate: "2027-10-31"
        },
        {
            mahalCode: "BAITULHUDA",
            name: "Baitul Huda Islamic Centre & Mahal",
            location: "Ernakulam, Kerala",
            subAdminUsername: "baitulhuda_admin",
            subAdminPassword: "password123",
            contactPhone: "+91 98472 99887",
            regNumber: "REG-KL/2021/0432",
            defaultMonthlyFee: 200,
            bankName: "Canara Bank",
            accountNumber: "109822300412",
            ifscCode: "CNRB0001098",
            upiId: "baitulhudamahal@cnrb",
            isActive: true,
            expiryDate: "2027-08-15"
        }
    ],
    households: [
        {
            id: 1,
            mahalCode: "ALNOOR",
            houseNumber: "H-01",
            headOfFamily: "K. P. Abdul Rahman",
            familyName: "Baitul Noor",
            phoneNumber: "9846011223",
            monthlyFee: 300,
            wardNumber: "Ward 1 (East)",
            memberCount: 5,
            maleCount: 2,
            femaleCount: 3,
            childrenCount: 1,
            rationCategory: "APL",
            madrasaStudentCount: 1,
            bloodGroups: "O+ve, A+ve",
            address: "Baitul Noor, Near Juma Masjid",
            verificationStatus: "VERIFIED",
            notes: "Patron family"
        },
        {
            id: 2,
            mahalCode: "ALNOOR",
            houseNumber: "H-02",
            headOfFamily: "M. C. Moideen Kutty",
            familyName: "Al-Falah Villa",
            phoneNumber: "9847122334",
            monthlyFee: 250,
            wardNumber: "Ward 1 (East)",
            memberCount: 4,
            maleCount: 2,
            femaleCount: 2,
            childrenCount: 1,
            rationCategory: "APL",
            madrasaStudentCount: 1,
            bloodGroups: "B+ve",
            address: "Al-Falah Villa, Hospital Road",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 3,
            mahalCode: "ALNOOR",
            houseNumber: "H-03",
            headOfFamily: "V. K. Hamza Haji",
            familyName: "Darussalam",
            phoneNumber: "9846233445",
            monthlyFee: 500,
            wardNumber: "Ward 1 (East)",
            memberCount: 6,
            maleCount: 3,
            femaleCount: 3,
            childrenCount: 2,
            rationCategory: "APL",
            madrasaStudentCount: 2,
            bloodGroups: "A+ve, AB+ve",
            address: "Darussalam, Post Office Junction",
            verificationStatus: "VERIFIED",
            notes: "Executive Committee Member"
        },
        {
            id: 4,
            mahalCode: "ALNOOR",
            houseNumber: "H-04",
            headOfFamily: "P. K. Usman",
            familyName: "Madeena Manzil",
            phoneNumber: "9847344556",
            monthlyFee: 200,
            wardNumber: "Ward 1 (East)",
            memberCount: 4,
            maleCount: 2,
            femaleCount: 2,
            childrenCount: 2,
            rationCategory: "BPL",
            madrasaStudentCount: 2,
            bloodGroups: "O+ve, B+ve",
            address: "Madeena Manzil, School Lane",
            verificationStatus: "PENDING_VERIFICATION",
            notes: "Updated details by member via OTP"
        },
        {
            id: 5,
            mahalCode: "ALNOOR",
            houseNumber: "H-05",
            headOfFamily: "T. Sayed Mohammed",
            familyName: "Rahmat Manzil",
            phoneNumber: "9846455667",
            monthlyFee: 250,
            wardNumber: "Ward 1 (East)",
            memberCount: 4,
            maleCount: 2,
            femaleCount: 2,
            childrenCount: 1,
            rationCategory: "APL",
            madrasaStudentCount: 1,
            bloodGroups: "O+ve",
            address: "Rahmat Manzil, Bypass",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 6,
            mahalCode: "ALNOOR",
            houseNumber: "H-06",
            headOfFamily: "K. T. Musthafa",
            familyName: "Bustan Villa",
            phoneNumber: "9847566778",
            monthlyFee: 250,
            wardNumber: "Ward 2 (West)",
            memberCount: 5,
            maleCount: 3,
            femaleCount: 2,
            childrenCount: 2,
            rationCategory: "APL",
            madrasaStudentCount: 2,
            bloodGroups: "A-ve",
            address: "Bustan Villa, River View",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 7,
            mahalCode: "ALNOOR",
            houseNumber: "H-07",
            headOfFamily: "C. H. Basheer",
            familyName: "Anugraha",
            phoneNumber: "9846677889",
            monthlyFee: 200,
            wardNumber: "Ward 2 (West)",
            memberCount: 3,
            maleCount: 1,
            femaleCount: 2,
            childrenCount: 0,
            rationCategory: "BPL",
            madrasaStudentCount: 0,
            bloodGroups: "B+ve",
            address: "Anugraha, Market Road",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 8,
            mahalCode: "ALNOOR",
            houseNumber: "H-08",
            headOfFamily: "A. P. Faisal",
            familyName: "Al-Madeena",
            phoneNumber: "9847788990",
            monthlyFee: 350,
            wardNumber: "Ward 2 (West)",
            memberCount: 4,
            maleCount: 2,
            femaleCount: 2,
            childrenCount: 1,
            rationCategory: "APL",
            madrasaStudentCount: 1,
            bloodGroups: "O+ve",
            address: "Al-Madeena, Hill View",
            verificationStatus: "VERIFIED",
            notes: "Gulf family"
        },
        {
            id: 9,
            mahalCode: "ALNOOR",
            houseNumber: "H-09",
            headOfFamily: "N. K. Shamsudheen",
            familyName: "Shams Manzil",
            phoneNumber: "9846899001",
            monthlyFee: 250,
            wardNumber: "Ward 2 (West)",
            memberCount: 6,
            maleCount: 3,
            femaleCount: 3,
            childrenCount: 2,
            rationCategory: "APL",
            madrasaStudentCount: 2,
            bloodGroups: "AB+ve",
            address: "Shams Manzil, Railway Gate",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 10,
            mahalCode: "ALNOOR",
            houseNumber: "H-10",
            headOfFamily: "E. K. Rasheed",
            familyName: "Kausar Villa",
            phoneNumber: "9847900112",
            monthlyFee: 200,
            wardNumber: "Ward 2 (West)",
            memberCount: 4,
            maleCount: 2,
            femaleCount: 2,
            childrenCount: 1,
            rationCategory: "BPL",
            madrasaStudentCount: 1,
            bloodGroups: "O+ve",
            address: "Kausar Villa, Canal Road",
            verificationStatus: "VERIFIED",
            notes: ""
        },
        {
            id: 11,
            mahalCode: "DARUSALAM",
            houseNumber: "D-01",
            headOfFamily: "P. Mohammedali",
            familyName: "Firdouse",
            phoneNumber: "9846111222",
            monthlyFee: 300,
            wardNumber: "Ward North",
            memberCount: 5,
            maleCount: 2,
            femaleCount: 3,
            childrenCount: 2,
            rationCategory: "APL",
            madrasaStudentCount: 1,
            bloodGroups: "O+ve",
            address: "Kozhikode Town",
            verificationStatus: "VERIFIED",
            notes: ""
        }
    ],
    subscriptionPayments: [
        // Household 1 (Jan to Oct paid)
        ...Array.from({ length: 10 }, (_, i) => ({
            id: 100 + i + 1,
            householdId: 1,
            month: i + 1,
            year: 2026,
            amountPaid: 300,
            paymentDate: Date.now() - (10 - i) * 28 * 86400000,
            paymentMethod: (i % 2 === 0) ? "UPI" : "CASH",
            receiptNumber: `REC-2026-${String(i + 1).padStart(3, '0')}`,
            collectedBy: "Treasurer - M. K. Basheer",
            notes: "Monthly Vari"
        })),
        // Household 3 (Jan to Sep paid)
        ...Array.from({ length: 9 }, (_, i) => ({
            id: 120 + i + 1,
            householdId: 3,
            month: i + 1,
            year: 2026,
            amountPaid: 500,
            paymentDate: Date.now() - (9 - i) * 28 * 86400000,
            paymentMethod: "BANK_TRANSFER",
            receiptNumber: `REC-2026-${String(20 + i + 1).padStart(3, '0')}`,
            collectedBy: "Treasurer - M. K. Basheer",
            notes: "Direct Bank Transfer"
        })),
        // Household 2 (Jan to Jul paid)
        ...Array.from({ length: 7 }, (_, i) => ({
            id: 140 + i + 1,
            householdId: 2,
            month: i + 1,
            year: 2026,
            amountPaid: 250,
            paymentDate: Date.now() - (8 - i) * 28 * 86400000,
            paymentMethod: "CASH",
            receiptNumber: `REC-2026-${String(40 + i + 1).padStart(3, '0')}`,
            collectedBy: "Collector - Salam",
            notes: ""
        })),
        // Household 8 (Jan to Oct paid)
        ...Array.from({ length: 10 }, (_, i) => ({
            id: 160 + i + 1,
            householdId: 8,
            month: i + 1,
            year: 2026,
            amountPaid: 350,
            paymentDate: Date.now() - (10 - i) * 26 * 86400000,
            paymentMethod: "UPI",
            receiptNumber: `REC-2026-${String(60 + i + 1).padStart(3, '0')}`,
            collectedBy: "Treasurer - M. K. Basheer",
            notes: "UPI Payment GPay"
        })),
        // Household 6 (Jan to May paid)
        ...Array.from({ length: 5 }, (_, i) => ({
            id: 180 + i + 1,
            householdId: 6,
            month: i + 1,
            year: 2026,
            amountPaid: 250,
            paymentDate: Date.now() - (7 - i) * 28 * 86400000,
            paymentMethod: "CASH",
            receiptNumber: `REC-2026-${String(80 + i + 1).padStart(3, '0')}`,
            collectedBy: "Collector - Salam",
            notes: ""
        }))
    ],
    cashTransactions: [
        {
            id: 1,
            mahalCode: "ALNOOR",
            type: "INCOME",
            category: "Friday Juma Collection",
            amount: 14250,
            transactionDate: Date.now() - 3 * 86400000,
            title: "Juma Box Collection (Week 43)",
            description: "Box opened in presence of President & Secretary",
            voucherNumber: "V-INC-104",
            paymentMode: "CASH"
        },
        {
            id: 2,
            mahalCode: "ALNOOR",
            type: "INCOME",
            category: "Special Donation",
            amount: 25000,
            transactionDate: Date.now() - 10 * 86400000,
            title: "Madrasa Renovation Sponsorship",
            description: "Donated by P. K. Brothers in memory of Late Kunhalan Kutty",
            voucherNumber: "V-INC-103",
            paymentMode: "BANK_TRANSFER"
        },
        {
            id: 3,
            mahalCode: "ALNOOR",
            type: "INCOME",
            category: "Friday Juma Collection",
            amount: 12800,
            transactionDate: Date.now() - 17 * 86400000,
            title: "Juma Box Collection (Week 42)",
            description: "Counted and deposited to committee cash box",
            voucherNumber: "V-INC-102",
            paymentMode: "CASH"
        },
        {
            id: 4,
            mahalCode: "ALNOOR",
            type: "INCOME",
            category: "Madrasa Admission & Book Fee",
            amount: 8600,
            transactionDate: Date.now() - 25 * 86400000,
            title: "Supplementary Syllabus & Textbooks",
            description: "Collected from newly enrolled batch",
            voucherNumber: "V-INC-101",
            paymentMode: "CASH"
        },
        {
            id: 5,
            mahalCode: "ALNOOR",
            type: "EXPENSE",
            category: "Staff Honorarium (Salary)",
            amount: 22000,
            transactionDate: Date.now() - 5 * 86400000,
            title: "Sadr Ustad & Teachers Monthly Allowance",
            description: "Distributed to 4 Madrasa teachers for current month",
            voucherNumber: "V-EXP-088",
            paymentMode: "BANK_TRANSFER"
        },
        {
            id: 6,
            mahalCode: "ALNOOR",
            type: "EXPENSE",
            category: "Electricity & Water",
            amount: 3450,
            transactionDate: Date.now() - 8 * 86400000,
            title: "KSEB Electricity Bill - Oct 2026",
            description: "Consumer # 115201402390 paid online",
            voucherNumber: "V-EXP-087",
            paymentMode: "UPI"
        },
        {
            id: 7,
            mahalCode: "ALNOOR",
            type: "EXPENSE",
            category: "Building Maintenance",
            amount: 4800,
            transactionDate: Date.now() - 14 * 86400000,
            title: "Classroom Fan Repairs & Painting Touchup",
            description: "Work done by Master Electricals (Receipt attached)",
            voucherNumber: "V-EXP-086",
            paymentMode: "CASH"
        },
        {
            id: 8,
            mahalCode: "ALNOOR",
            type: "EXPENSE",
            category: "Stationery & Printing",
            amount: 1650,
            transactionDate: Date.now() - 21 * 86400000,
            title: "Exam Question Papers & Attendance Registers",
            description: "Printed at Ideal Offset Press",
            voucherNumber: "V-EXP-085",
            paymentMode: "CASH"
        }
    ],
    additionalCollections: [
        {
            id: 1,
            mahalCode: "ALNOOR",
            title: "Medical Relief Fund",
            description: "Emergency medical aid fund drive for community members",
            targetAmountPerFamily: 500,
            targetTotalFund: 100000,
            category: "Relief",
            dueDate: Date.now() + 30 * 86400000
        },
        {
            id: 2,
            mahalCode: "ALNOOR",
            title: "Madrasa Renovation & Building Fund",
            description: "Classroom renovation and smart board installation",
            targetAmountPerFamily: 1000,
            targetTotalFund: 250000,
            category: "Construction",
            dueDate: Date.now() + 60 * 86400000
        }
    ],
    additionalPayments: [
        {
            id: 1,
            collectionId: 1,
            householdId: 1,
            mahalCode: "ALNOOR",
            householdName: "K. P. Abdul Rahman",
            houseNumber: "H-01",
            amount: 500,
            receiptNumber: "RCP-COL-001",
            paymentMode: "UPI",
            collectorName: "Sub-Admin",
            date: Date.now() - 4 * 86400000,
            notes: "Medical relief contribution"
        }
    ]
};

const STORAGE_KEY = "MAHAL_LEDGER_DATA_V2_STORAGE";

// 2. State Controller
class MahalLedgerState {
    constructor() {
        this.data = this.loadData();
        this.currentUser = {
            isAuthenticated: false,
            role: "SUB_ADMIN", // 'SUB_ADMIN' | 'SUPER_ADMIN' | 'MEMBER'
            mahalCode: "ALNOOR",
            memberHouseholdId: null,
            username: ""
        };
        this.selectedMonthIndices = []; // For Collect Vari modal
        this.activeHouseholdForCollection = null;
        this.activeHouseholdForEdit = null;
    }

    loadData() {
        try {
            const raw = localStorage.getItem(STORAGE_KEY);
            if (raw) {
                return JSON.parse(raw);
            }
        } catch (e) {
            console.error("Error reading localStorage:", e);
        }
        return JSON.parse(JSON.stringify(DEFAULT_DATA));
    }

    saveData() {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(this.data));
            this.showSyncStatus();
        } catch (e) {
            console.error("Error saving data:", e);
        }
    }

    resetData() {
        this.data = JSON.parse(JSON.stringify(DEFAULT_DATA));
        this.saveData();
    }

    showSyncStatus() {
        const badge = document.getElementById("syncBadge");
        if (badge) {
            badge.innerHTML = "✓ Saved Locally";
            badge.style.opacity = "1";
            setTimeout(() => {
                badge.innerHTML = "☁️ Data Synced";
            }, 1800);
        }
    }

    getActiveCommunity() {
        return this.data.communities.find(c => c.mahalCode === this.data.activeMahalCode) || this.data.communities[0];
    }

    getHouseholdsForActiveMahal() {
        return this.data.households.filter(h => h.mahalCode === this.data.activeMahalCode);
    }

    getPaymentsForHousehold(householdId) {
        return this.data.subscriptionPayments.filter(p => p.householdId === householdId && p.year === this.data.currentYear);
    }

    getTransactionsForActiveMahal() {
        return this.data.cashTransactions.filter(t => t.mahalCode === this.data.activeMahalCode);
    }

    getSpecialFundsForActiveMahal() {
        return this.data.additionalCollections.filter(f => f.mahalCode === this.data.activeMahalCode);
    }

    getSpecialPaymentsForActiveMahal() {
        return this.data.additionalPayments.filter(p => p.mahalCode === this.data.activeMahalCode);
    }

    // Financial Computations
    getFinancialStats() {
        const activeHouseholds = this.getHouseholdsForActiveMahal();
        const activeHouseholdIds = new Set(activeHouseholds.map(h => h.id));

        const payments = this.data.subscriptionPayments.filter(p => activeHouseholdIds.has(p.householdId) && p.year === this.data.currentYear);
        const subscriptionTotal = payments.reduce((sum, p) => sum + p.amountPaid, 0);

        const transactions = this.getTransactionsForActiveMahal();
        const generalIncome = transactions.filter(t => t.type === 'INCOME').reduce((sum, t) => sum + t.amount, 0);
        const totalExpenses = transactions.filter(t => t.type === 'EXPENSE').reduce((sum, t) => sum + t.amount, 0);

        const netBalance = (subscriptionTotal + generalIncome) - totalExpenses;

        // Pending dues (Based on 12 months for 2026)
        let totalPendingDues = 0;
        let clearedCount = 0;
        let pendingCount = 0;

        activeHouseholds.forEach(h => {
            const hPayments = payments.filter(p => p.householdId === h.id);
            const paidMonthsCount = hPayments.length;
            const pendingMonths = Math.max(0, 12 - paidMonthsCount);
            const due = pendingMonths * h.monthlyFee;
            totalPendingDues += due;
            if (due <= 0) {
                clearedCount++;
            } else {
                pendingCount++;
            }
        });

        return {
            netBalance,
            subscriptionTotal,
            generalIncome,
            totalExpenses,
            totalPendingDues,
            clearedCount,
            pendingCount,
            totalHouseholds: activeHouseholds.length
        };
    }
}

const AppState = new MahalLedgerState();

const MONTH_NAMES = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
const MONTH_NAMES_ML = ["ജനു", "ഫെബ്രു", "മാർച്ച്", "ഏപ്രിൽ", "മെയ്", "ജൂൺ", "ജൂലൈ", "ഓഗ", "സെപ്റ്റം", "ഒക്ടോ", "നവം", "ഡിസം"];

// Toast Notification
function showToast(message, type = "success") {
    const container = document.getElementById("toastContainer");
    if (!container) return;
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span>${type === 'success' ? '✓' : '⚠️'}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.remove();
    }, 3500);
}

// 3. UI Navigation & Tabs
function switchMainTab(tabId) {
    document.querySelectorAll(".nav-tab-btn").forEach(btn => {
        btn.classList.toggle("active", btn.dataset.tab === tabId);
    });
    document.querySelectorAll(".tab-view").forEach(view => {
        view.classList.toggle("active", view.id === tabId);
    });

    if (tabId === "view-dashboard") renderDashboard();
    if (tabId === "view-subscriptions") renderSubscriptions();
    if (tabId === "view-ledger") renderLedger();
    if (tabId === "view-transparency") renderTransparency();
    if (tabId === "view-settings") renderSettings();
    if (tabId === "view-superadmin") renderSuperAdmin();
}

// Format Rupee
function formatCurrency(amount) {
    return '₹' + Number(amount || 0).toLocaleString('en-IN', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
}

function formatDate(timestamp) {
    if (!timestamp) return "-";
    const d = new Date(timestamp);
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

// 4. Render Dashboard
function renderDashboard() {
    const stats = AppState.getFinancialStats();
    const comm = AppState.getActiveCommunity();

    // Stats
    document.getElementById("statNetBalance").textContent = formatCurrency(stats.netBalance);
    document.getElementById("statSubscriptionTotal").textContent = formatCurrency(stats.subscriptionTotal);
    document.getElementById("statGeneralIncome").textContent = formatCurrency(stats.generalIncome);
    document.getElementById("statTotalExpenses").textContent = formatCurrency(stats.totalExpenses);
    document.getElementById("statPendingDues").textContent = formatCurrency(stats.totalPendingDues);
    document.getElementById("statPendingSub").textContent = `${stats.pendingCount} കുടുംബങ്ങൾക്ക് കുടിശ്ശികയുണ്ട്`;

    // Recent Subscription Payments
    const recentPaymentsContainer = document.getElementById("dashboardRecentPayments");
    const activeHouseholdIds = new Set(AppState.getHouseholdsForActiveMahal().map(h => h.id));
    const recentPayments = AppState.data.subscriptionPayments
        .filter(p => activeHouseholdIds.has(p.householdId))
        .sort((a, b) => b.paymentDate - a.paymentDate)
        .slice(0, 6);

    if (recentPayments.length === 0) {
        recentPaymentsContainer.innerHTML = `<div style="text-align:center; padding:30px; color:#94a3b8;">ഇടപാടുകൾ ലഭ്യമല്ല</div>`;
    } else {
        recentPaymentsContainer.innerHTML = recentPayments.map(p => {
            const h = AppState.data.households.find(x => x.id === p.householdId);
            return `
                <li class="feed-item">
                    <div class="feed-item-left">
                        <div class="feed-avatar">🕌</div>
                        <div class="feed-info">
                            <div class="title">${h ? h.headOfFamily : 'Unknown'} (${h ? h.houseNumber : '-'})</div>
                            <div class="sub">${MONTH_NAMES[p.month - 1]} 2026 • ${p.paymentMethod} • ${p.receiptNumber}</div>
                        </div>
                    </div>
                    <div class="feed-amount">
                        <div class="amount income">+${formatCurrency(p.amountPaid)}</div>
                        <div class="date">${formatDate(p.paymentDate)}</div>
                        <button class="btn btn-sm btn-outline" style="margin-top:4px;" onclick="viewReceiptByPaymentId(${p.id})">രസീത്</button>
                    </div>
                </li>
            `;
        }).join("");
    }

    // Recent Transactions
    const recentTxContainer = document.getElementById("dashboardRecentTransactions");
    const recentTxs = AppState.getTransactionsForActiveMahal()
        .sort((a, b) => b.transactionDate - a.transactionDate)
        .slice(0, 6);

    if (recentTxs.length === 0) {
        recentTxContainer.innerHTML = `<div style="text-align:center; padding:30px; color:#94a3b8;">ഇടപാടുകൾ ലഭ്യമല്ല</div>`;
    } else {
        recentTxContainer.innerHTML = recentTxs.map(t => {
            const isInc = t.type === 'INCOME';
            return `
                <li class="feed-item">
                    <div class="feed-item-left">
                        <div class="feed-avatar ${isInc ? '' : 'expense'}">${isInc ? '📥' : '📤'}</div>
                        <div class="feed-info">
                            <div class="title">${t.title}</div>
                            <div class="sub">${t.category} • ${t.voucherNumber || '-'} • ${t.paymentMode}</div>
                        </div>
                    </div>
                    <div class="feed-amount">
                        <div class="amount ${isInc ? 'income' : 'expense'}">${isInc ? '+' : '-'}${formatCurrency(t.amount)}</div>
                        <div class="date">${formatDate(t.transactionDate)}</div>
                    </div>
                </li>
            `;
        }).join("");
    }
}

// 5. Render Subscriptions
let currentSubFilter = "ALL";
let currentSubSearch = "";
let currentSubTab = "monthly"; // 'monthly' | 'special'

function renderSubscriptions() {
    if (currentSubTab === "special") {
        document.getElementById("monthlyVariSection").style.display = "none";
        document.getElementById("specialFundsSection").style.display = "block";
        renderSpecialFunds();
        return;
    }

    document.getElementById("monthlyVariSection").style.display = "block";
    document.getElementById("specialFundsSection").style.display = "none";

    const households = AppState.getHouseholdsForActiveMahal();
    const payments = AppState.data.subscriptionPayments.filter(p => p.year === AppState.data.currentYear);

    // Filter
    const filtered = households.filter(h => {
        const hPayments = payments.filter(p => p.householdId === h.id);
        const dueMonths = Math.max(0, 12 - hPayments.length);
        const totalDue = dueMonths * h.monthlyFee;

        if (currentSubFilter === "WITH_DUES" && totalDue <= 0) return false;
        if (currentSubFilter === "FULLY_PAID" && totalDue > 0) return false;
        if (currentSubFilter === "PENDING_VERIFICATION" && h.verificationStatus !== "PENDING_VERIFICATION") return false;

        if (currentSubSearch.trim()) {
            const q = currentSubSearch.toLowerCase();
            const matchName = (h.headOfFamily || '').toLowerCase().includes(q);
            const matchHouse = (h.houseNumber || '').toLowerCase().includes(q);
            const matchPhone = (h.phoneNumber || '').includes(q);
            const matchWard = (h.wardNumber || '').toLowerCase().includes(q);
            return matchName || matchHouse || matchPhone || matchWard;
        }
        return true;
    });

    const tbody = document.getElementById("subscriptionsTableBody");
    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding:30px; color:#94a3b8;">കുടുംബങ്ങൾ കണ്ടെത്താനായില്ല</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map(h => {
        const hPayments = payments.filter(p => p.householdId === h.id);
        const paidMonthSet = new Set(hPayments.map(p => p.month));
        const totalPaid = hPayments.reduce((sum, p) => sum + p.amountPaid, 0);
        const dueMonths = Math.max(0, 12 - paidMonthSet.size);
        const totalDue = dueMonths * h.monthlyFee;

        const monthsHtml = Array.from({ length: 12 }, (_, i) => {
            const m = i + 1;
            const isPaid = paidMonthSet.has(m);
            return `<span class="month-badge ${isPaid ? 'paid' : (dueMonths > 0 ? 'overdue' : 'pending')}">${MONTH_NAMES[i]}</span>`;
        }).join("");

        return `
            <tr>
                <td><strong>${h.houseNumber}</strong></td>
                <td>
                    <div style="font-weight:700; color:var(--text);">${h.headOfFamily}</div>
                    <div style="font-size:0.75rem; color:var(--text-muted);">${h.familyName || ''} • ${h.address || ''}</div>
                </td>
                <td>
                    <div>${h.phoneNumber}</div>
                    <div style="font-size:0.75rem; color:var(--text-muted);">${h.wardNumber || ''}</div>
                </td>
                <td><strong>${formatCurrency(h.monthlyFee)}</strong></td>
                <td>
                    <div class="months-matrix">${monthsHtml}</div>
                </td>
                <td>
                    <div style="font-weight:700; color:${totalDue > 0 ? 'var(--danger)' : 'var(--success)'};">
                        ${totalDue > 0 ? formatCurrency(totalDue) + ' കുടിശ്ശിക' : '✓ ക്ലിയർ'}
                    </div>
                    <div style="font-size:0.72rem; color:var(--text-muted);">${paidMonthSet.size}/12 മാസം അടച്ചു</div>
                </td>
                <td>
                    <div style="display:flex; gap:6px; flex-wrap:wrap;">
                        <button class="btn btn-sm btn-primary" onclick="openCollectSubscriptionModal(${h.id})">💵 വാങ്ങുക</button>
                        <button class="btn btn-sm btn-outline" onclick="openEditHouseholdModal(${h.id})">✏️</button>
                        <button class="btn btn-sm btn-whatsapp" onclick="sendWhatsAppDuesReminder(${h.id})" title="Send WhatsApp Reminder">💬</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

// Special Funds / Additional Collections
function renderSpecialFunds() {
    const funds = AppState.getSpecialFundsForActiveMahal();
    const payments = AppState.getSpecialPaymentsForActiveMahal();
    const container = document.getElementById("specialFundsList");

    if (funds.length === 0) {
        container.innerHTML = `<div style="text-align:center; padding:40px; color:#94a3b8;">പ്രത്യേക ഫണ്ടുകൾ നിലവിലില്ല. പുതിയ ഫണ്ട് തുടങ്ങാൻ മുകളിലെ ബട്ടൺ ഉപയോഗിക്കുക.</div>`;
        return;
    }

    container.innerHTML = funds.map(fund => {
        const fundPayments = payments.filter(p => p.collectionId === fund.id);
        const collectedTotal = fundPayments.reduce((sum, p) => sum + p.amount, 0);
        const progressPct = fund.targetTotalFund > 0 ? Math.min(100, Math.round((collectedTotal / fund.targetTotalFund) * 100)) : 0;

        return `
            <div class="stat-card" style="margin-bottom:16px;">
                <div class="stat-header">
                    <div>
                        <span class="badge-role" style="background:var(--primary-light); color:var(--primary-dark);">${fund.category || 'Special Fund'}</span>
                        <h3 style="font-size:1.15rem; font-weight:800; margin-top:8px;">${fund.title}</h3>
                        <p style="font-size:0.85rem; color:var(--text-muted);">${fund.description}</p>
                    </div>
                    <button class="btn btn-sm btn-primary" onclick="openRecordSpecialPaymentModal(${fund.id})">💵 തുക സ്വീകരിക്കുക</button>
                </div>
                <div style="margin: 16px 0;">
                    <div style="display:flex; justify-content:space-between; font-size:0.85rem; font-weight:700; margin-bottom:6px;">
                        <span>പിരിഞ്ഞ തുക: ${formatCurrency(collectedTotal)}</span>
                        <span>ലക്ഷ്യം: ${formatCurrency(fund.targetTotalFund)} (${progressPct}%)</span>
                    </div>
                    <div style="width:100%; height:8px; background:#e2e8f0; border-radius:999px; overflow:hidden;">
                        <div style="width:${progressPct}%; height:100%; background:var(--primary);"></div>
                    </div>
                </div>
                <div style="display:flex; justify-content:space-between; align-items:center; font-size:0.8rem; color:var(--text-muted); border-top:1px solid var(--border); padding-top:10px;">
                    <span>കുടുംബത്തിന് നിശ്ചയിച്ച തുക: <strong>${formatCurrency(fund.targetAmountPerFamily)}</strong></span>
                    <span>പങ്കെടുത്തവർ: <strong>${fundPayments.length} കുടുംബങ്ങൾ</strong></span>
                </div>
            </div>
        `;
    }).join("");
}

// 6. Render Ledger
let currentLedgerFilter = "ALL";
let currentLedgerSearch = "";

function renderLedger() {
    const transactions = AppState.getTransactionsForActiveMahal();
    const stats = AppState.getFinancialStats();

    document.getElementById("ledgerTotalIncome").textContent = formatCurrency(stats.generalIncome + stats.subscriptionTotal);
    document.getElementById("ledgerTotalExpense").textContent = formatCurrency(stats.totalExpenses);
    document.getElementById("ledgerNetBalance").textContent = formatCurrency(stats.netBalance);

    const filtered = transactions.filter(t => {
        if (currentLedgerFilter === "INCOME" && t.type !== "INCOME") return false;
        if (currentLedgerFilter === "EXPENSE" && t.type !== "EXPENSE") return false;

        if (currentLedgerSearch.trim()) {
            const q = currentLedgerSearch.toLowerCase();
            const matchTitle = (t.title || '').toLowerCase().includes(q);
            const matchCat = (t.category || '').toLowerCase().includes(q);
            const matchVoucher = (t.voucherNumber || '').toLowerCase().includes(q);
            const matchDesc = (t.description || '').toLowerCase().includes(q);
            return matchTitle || matchCat || matchVoucher || matchDesc;
        }
        return true;
    }).sort((a, b) => b.transactionDate - a.transactionDate);

    const tbody = document.getElementById("ledgerTableBody");
    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding:30px; color:#94a3b8;">ഇടപാടുകൾ കണ്ടെത്താനായില്ല</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map(t => {
        const isInc = t.type === 'INCOME';
        return `
            <tr>
                <td>${formatDate(t.transactionDate)}</td>
                <td><strong>${t.voucherNumber || '-'}</strong></td>
                <td>
                    <div style="font-weight:700; color:var(--text);">${t.title}</div>
                    <div style="font-size:0.75rem; color:var(--text-muted);">${t.description || ''}</div>
                </td>
                <td><span class="badge-role" style="background:#f1f5f9; color:var(--text);">${t.category}</span></td>
                <td>${t.paymentMode || 'CASH'}</td>
                <td style="font-weight:800; color:${isInc ? 'var(--success)' : 'var(--danger)'};">
                    ${isInc ? '+' : '-'}${formatCurrency(t.amount)}
                </td>
                <td>
                    <button class="btn btn-sm btn-outline" style="color:var(--danger);" onclick="deleteTransaction(${t.id})">🗑️</button>
                </td>
            </tr>
        `;
    }).join("");
}

// 7. Render Transparency & Audit
function renderTransparency() {
    const stats = AppState.getFinancialStats();
    const comm = AppState.getActiveCommunity();
    const txs = AppState.getTransactionsForActiveMahal();

    document.getElementById("transNetBalance").textContent = formatCurrency(stats.netBalance);
    document.getElementById("transSubscriptionTotal").textContent = formatCurrency(stats.subscriptionTotal);
    document.getElementById("transGeneralIncome").textContent = formatCurrency(stats.generalIncome);
    document.getElementById("transTotalExpenses").textContent = formatCurrency(stats.totalExpenses);
    document.getElementById("transCommunityHeader").textContent = comm.name;

    // Expense Categories breakdown
    const expenses = txs.filter(t => t.type === 'EXPENSE');
    const catMap = {};
    expenses.forEach(e => {
        catMap[e.category] = (catMap[e.category] || 0) + e.amount;
    });

    const catList = Object.entries(catMap).sort((a, b) => b[1] - a[1]);
    const expContainer = document.getElementById("transExpenseCategories");
    if (catList.length === 0) {
        expContainer.innerHTML = `<div style="color:#94a3b8;">ചിലവുകൾ രേഖപ്പെടുത്തിയിട്ടില്ല</div>`;
    } else {
        expContainer.innerHTML = catList.map(([cat, amt]) => {
            const pct = stats.totalExpenses > 0 ? Math.round((amt / stats.totalExpenses) * 100) : 0;
            return `
                <div style="margin-bottom:12px;">
                    <div style="display:flex; justify-content:space-between; font-size:0.85rem; font-weight:700; margin-bottom:4px;">
                        <span>${cat}</span>
                        <span>${formatCurrency(amt)} (${pct}%)</span>
                    </div>
                    <div style="width:100%; height:8px; background:#fee2e2; border-radius:999px; overflow:hidden;">
                        <div style="width:${pct}%; height:100%; background:var(--danger);"></div>
                    </div>
                </div>
            `;
        }).join("");
    }
}

// Public Household Audit Search
function searchHouseholdAudit() {
    const query = document.getElementById("auditSearchInput").value.trim().toLowerCase();
    const resultBox = document.getElementById("auditSearchResult");
    if (!query) {
        resultBox.innerHTML = "";
        return;
    }

    const households = AppState.getHouseholdsForActiveMahal();
    const match = households.find(h =>
        h.houseNumber.toLowerCase() === query ||
        h.phoneNumber.includes(query) ||
        h.headOfFamily.toLowerCase().includes(query)
    );

    if (!match) {
        resultBox.innerHTML = `
            <div class="stat-card" style="background:#fef2f2; border-color:#fca5a5; margin-top:16px;">
                <p style="color:#991b1b; font-weight:700;">കുടുംബം കണ്ടെത്താനായില്ല. വീട്ടുനമ്പറോ ഫോൺ നമ്പറോ കൃത്യമായി നൽകുക.</p>
            </div>
        `;
        return;
    }

    const payments = AppState.getPaymentsForHousehold(match.id);
    const paidMonths = new Set(payments.map(p => p.month));
    const pendingMonths = Math.max(0, 12 - paidMonths.size);
    const dueAmount = pendingMonths * match.monthlyFee;

    const monthsHtml = Array.from({ length: 12 }, (_, i) => {
        const isPaid = paidMonths.has(i + 1);
        return `<span class="month-badge ${isPaid ? 'paid' : 'overdue'}">${MONTH_NAMES[i]}</span>`;
    }).join("");

    resultBox.innerHTML = `
        <div class="stat-card" style="margin-top:16px; border:2px solid var(--primary);">
            <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                <div>
                    <span class="badge-role" style="background:var(--primary-light); color:var(--primary-dark);">${match.houseNumber}</span>
                    <h3 style="font-size:1.2rem; font-weight:800; margin-top:6px;">${match.headOfFamily}</h3>
                    <p style="font-size:0.85rem; color:var(--text-muted);">${match.familyName || ''} • ${match.wardNumber} • ${match.phoneNumber}</p>
                </div>
                <div style="text-align:right;">
                    <div style="font-size:0.8rem; color:var(--text-muted);">മാസ വരിസംഖ്യ</div>
                    <div style="font-size:1.2rem; font-weight:800; color:var(--primary);">${formatCurrency(match.monthlyFee)}</div>
                </div>
            </div>

            <div style="margin:16px 0;">
                <div style="font-size:0.85rem; font-weight:700; margin-bottom:6px;">2026 വർഷത്തെ വരിസംഖ്യാ നില:</div>
                <div class="months-matrix">${monthsHtml}</div>
            </div>

            <div style="display:flex; justify-content:space-between; align-items:center; background:${dueAmount > 0 ? '#fffbeb' : '#f0fdf4'}; padding:12px 16px; border-radius:10px; border:1px solid ${dueAmount > 0 ? '#fde68a' : '#bbf7d0'};">
                <span style="font-weight:700; color:${dueAmount > 0 ? '#b45309' : '#15803d'};">
                    ${dueAmount > 0 ? `നിലവിലെ കുടിശ്ശിക: ${formatCurrency(dueAmount)} (${pendingMonths} മാസം)` : '✓ കുടിശ്ശികയില്ല (മുഴുവൻ അടച്ചുതീർത്തു)'}
                </span>
                ${dueAmount > 0 ? `<button class="btn btn-sm btn-primary" onclick="openFamilyDueAlert(${match.id})">UPI വഴി അടയ്ക്കുക</button>` : ''}
            </div>
        </div>
    `;
}

// 8. Render Settings
function renderSettings() {
    const comm = AppState.getActiveCommunity();
    document.getElementById("settingMahalName").value = comm.name;
    document.getElementById("settingLocation").value = comm.location;
    document.getElementById("settingPhone").value = comm.contactPhone;
    document.getElementById("settingRegNo").value = comm.regNumber;
    document.getElementById("settingFee").value = comm.defaultMonthlyFee;
    document.getElementById("settingBank").value = comm.bankName || '';
    document.getElementById("settingAccNo").value = comm.accountNumber || '';
    document.getElementById("settingIfsc").value = comm.ifscCode || '';
    document.getElementById("settingUpi").value = comm.upiId || '';

    // Communities Dropdown
    const select = document.getElementById("settingSwitchMahal");
    select.innerHTML = AppState.data.communities.map(c =>
        `<option value="${c.mahalCode}" ${c.mahalCode === AppState.data.activeMahalCode ? 'selected' : ''}>${c.name} (${c.mahalCode})</option>`
    ).join("");
}

function saveMahalSettings(e) {
    e.preventDefault();
    const comm = AppState.getActiveCommunity();
    comm.name = document.getElementById("settingMahalName").value.trim();
    comm.location = document.getElementById("settingLocation").value.trim();
    comm.contactPhone = document.getElementById("settingPhone").value.trim();
    comm.regNumber = document.getElementById("settingRegNo").value.trim();
    comm.defaultMonthlyFee = Number(document.getElementById("settingFee").value) || 250;
    comm.bankName = document.getElementById("settingBank").value.trim();
    comm.accountNumber = document.getElementById("settingAccNo").value.trim();
    comm.ifscCode = document.getElementById("settingIfsc").value.trim();
    comm.upiId = document.getElementById("settingUpi").value.trim();

    AppState.saveData();
    updateAppHeader();
    showToast("മഹല്ല് വിവരങ്ങൾ വിജയകരമായി സേവ് ചെയ്തു");
}

function switchActiveMahalFromSettings(code) {
    AppState.data.activeMahalCode = code;
    AppState.saveData();
    updateAppHeader();
    renderDashboard();
    showToast(`വർക്ക്‌സ്പേസ് മാറി: ${AppState.getActiveCommunity().name}`);
}

// 9. Render Super Admin Portal
function renderSuperAdmin() {
    const communities = AppState.data.communities;
    const container = document.getElementById("superAdminCommunitiesList");

    container.innerHTML = communities.map(c => `
        <div class="stat-card" style="margin-bottom:16px;">
            <div style="display:flex; justify-content:space-between; align-items:flex-start; flex-wrap:wrap; gap:12px;">
                <div>
                    <span class="badge-role" style="background:var(--primary-light); color:var(--primary-dark);">${c.mahalCode}</span>
                    <h3 style="font-size:1.15rem; font-weight:800; margin-top:6px;">${c.name}</h3>
                    <p style="font-size:0.85rem; color:var(--text-muted);">${c.location} • ${c.contactPhone}</p>
                    <p style="font-size:0.8rem; color:var(--text-light); margin-top:4px;">ലോഗിൻ: <strong>${c.subAdminUsername}</strong> | പാസ്‌വേഡ്: <strong>${c.subAdminPassword}</strong></p>
                </div>
                <div style="display:flex; gap:8px; align-items:center;">
                    <span class="badge-role" style="background:#f0fdf4; color:#15803d; border-color:#86efac;">സബ്‌സ്‌ക്രിപ്ഷൻ: ${c.expiryDate}</span>
                    <button class="btn btn-sm btn-primary" onclick="enterMahalAsSuperAdmin('${c.mahalCode}')">വർക്ക്സ്പേസിൽ കയറുക</button>
                </div>
            </div>
        </div>
    `).join("");
}

function enterMahalAsSuperAdmin(code) {
    AppState.data.activeMahalCode = code;
    AppState.saveData();
    updateAppHeader();
    switchMainTab("view-dashboard");
    showToast(`സൂപ്പർ അഡ്മിൻ മോഡിൽ ${AppState.getActiveCommunity().name} ലേക്ക് പ്രവേശിച്ചു`);
}

// 10. Modals & Business Logic

// Collect Subscription Modal
function openCollectSubscriptionModal(householdId) {
    const h = AppState.data.households.find(x => x.id === householdId);
    if (!h) return;

    AppState.activeHouseholdForCollection = h;
    const payments = AppState.getPaymentsForHousehold(h.id);
    const paidMonths = new Set(payments.map(p => p.month));

    document.getElementById("collectFamilyName").textContent = `${h.headOfFamily} (${h.houseNumber})`;
    document.getElementById("collectMonthlyFee").textContent = formatCurrency(h.monthlyFee);

    AppState.selectedMonthIndices = [];

    const grid = document.getElementById("collectMonthGrid");
    grid.innerHTML = Array.from({ length: 12 }, (_, i) => {
        const monthNum = i + 1;
        const isPaid = paidMonths.has(monthNum);
        return `
            <div class="month-check-card ${isPaid ? 'disabled' : ''}" id="monthCard_${monthNum}" onclick="toggleMonthSelection(${monthNum}, ${isPaid})">
                <div style="font-weight:700;">${MONTH_NAMES[i]}</div>
                <div style="font-size:0.75rem;">${isPaid ? '✓ അടച്ചു' : formatCurrency(h.monthlyFee)}</div>
            </div>
        `;
    }).join("");

    updateCollectAmount();
    document.getElementById("collectModalOverlay").classList.add("active");
}

function toggleMonthSelection(monthNum, isPaid) {
    if (isPaid) return;
    const idx = AppState.selectedMonthIndices.indexOf(monthNum);
    if (idx > -1) {
        AppState.selectedMonthIndices.splice(idx, 1);
        document.getElementById(`monthCard_${monthNum}`).classList.remove("checked");
    } else {
        AppState.selectedMonthIndices.push(monthNum);
        document.getElementById(`monthCard_${monthNum}`).classList.add("checked");
    }
    updateCollectAmount();
}

function selectAllUnpaidMonths() {
    const h = AppState.activeHouseholdForCollection;
    if (!h) return;
    const payments = AppState.getPaymentsForHousehold(h.id);
    const paidMonths = new Set(payments.map(p => p.month));

    AppState.selectedMonthIndices = [];
    for (let m = 1; m <= 12; m++) {
        if (!paidMonths.has(m)) {
            AppState.selectedMonthIndices.push(m);
            const card = document.getElementById(`monthCard_${m}`);
            if (card) card.classList.add("checked");
        }
    }
    updateCollectAmount();
}

function clearMonthSelection() {
    AppState.selectedMonthIndices = [];
    document.querySelectorAll(".month-check-card").forEach(c => {
        if (!c.classList.contains("disabled")) c.classList.remove("checked");
    });
    updateCollectAmount();
}

function updateCollectAmount() {
    const h = AppState.activeHouseholdForCollection;
    const count = AppState.selectedMonthIndices.length;
    const total = count * (h ? h.monthlyFee : 0);
    document.getElementById("collectCalculatedAmount").textContent = formatCurrency(total);
}

function saveSubscriptionPayment() {
    const h = AppState.activeHouseholdForCollection;
    if (!h || AppState.selectedMonthIndices.length === 0) {
        showToast("ദയവായി കുറഞ്ഞത് ഒരു മാസമെങ്കിലും തിരഞ്ഞെടുക്കുക", "error");
        return;
    }

    const mode = document.getElementById("collectPaymentMode").value;
    const collector = document.getElementById("collectCollectorName").value.trim() || "Treasurer";
    const notes = document.getElementById("collectNotes").value.trim();
    const timestamp = Date.now();

    const newPayments = [];
    AppState.selectedMonthIndices.sort((a, b) => a - b).forEach((m, idx) => {
        const paymentId = Date.now() + idx;
        const receiptNo = `REC-2026-${String(AppState.data.subscriptionPayments.length + idx + 1).padStart(3, '0')}`;
        const pObj = {
            id: paymentId,
            householdId: h.id,
            month: m,
            year: AppState.data.currentYear,
            amountPaid: h.monthlyFee,
            paymentDate: timestamp,
            paymentMethod: mode,
            receiptNumber: receiptNo,
            collectedBy: collector,
            notes: notes
        };
        AppState.data.subscriptionPayments.push(pObj);
        newPayments.push(pObj);
    });

    AppState.saveData();
    closeModal("collectModalOverlay");
    renderSubscriptions();
    renderDashboard();

    // Show Digital Receipt
    showDigitalReceipt({
        receiptNumber: newPayments[0].receiptNumber,
        householdName: h.headOfFamily,
        houseNumber: h.houseNumber,
        monthsPaid: AppState.selectedMonthIndices.map(m => MONTH_NAMES[m - 1]),
        year: AppState.data.currentYear,
        totalAmount: AppState.selectedMonthIndices.length * h.monthlyFee,
        paymentMethod: mode,
        dateFormatted: formatDate(timestamp),
        collectorName: collector,
        communityName: AppState.getActiveCommunity().name
    });

    showToast("വരിസംഖ്യ വിജയകരമായി രേഖപ്പെടുത്തി!");
}

// Add Household Modal
function openAddHouseholdModal() {
    document.getElementById("householdForm").reset();
    document.getElementById("householdModalTitle").textContent = "കുടുംബത്തെ ചേർക്കുക";
    document.getElementById("householdIdInput").value = "";
    document.getElementById("householdFeeInput").value = AppState.getActiveCommunity().defaultMonthlyFee || 250;
    document.getElementById("householdModalOverlay").classList.add("active");
}

function openEditHouseholdModal(id) {
    const h = AppState.data.households.find(x => x.id === id);
    if (!h) return;

    document.getElementById("householdModalTitle").textContent = "കുടുംബവിവരങ്ങൾ തിരുത്തുക";
    document.getElementById("householdIdInput").value = h.id;
    document.getElementById("householdNumberInput").value = h.houseNumber;
    document.getElementById("householdHeadInput").value = h.headOfFamily;
    document.getElementById("householdFamilyInput").value = h.familyName || '';
    document.getElementById("householdPhoneInput").value = h.phoneNumber;
    document.getElementById("householdFeeInput").value = h.monthlyFee;
    document.getElementById("householdWardInput").value = h.wardNumber || '';
    document.getElementById("householdMembersInput").value = h.memberCount || 4;
    document.getElementById("householdRationInput").value = h.rationCategory || 'APL';
    document.getElementById("householdAddressInput").value = h.address || '';
    document.getElementById("householdNotesInput").value = h.notes || '';

    document.getElementById("householdModalOverlay").classList.add("active");
}

function saveHousehold(e) {
    e.preventDefault();
    const id = document.getElementById("householdIdInput").value;
    const houseNo = document.getElementById("householdNumberInput").value.trim();
    const head = document.getElementById("householdHeadInput").value.trim();
    const family = document.getElementById("householdFamilyInput").value.trim();
    const phone = document.getElementById("householdPhoneInput").value.trim();
    const fee = Number(document.getElementById("householdFeeInput").value) || 250;
    const ward = document.getElementById("householdWardInput").value.trim();
    const members = Number(document.getElementById("householdMembersInput").value) || 4;
    const ration = document.getElementById("householdRationInput").value;
    const address = document.getElementById("householdAddressInput").value.trim();
    const notes = document.getElementById("householdNotesInput").value.trim();

    if (id) {
        // Edit
        const target = AppState.data.households.find(x => x.id == id);
        if (target) {
            target.houseNumber = houseNo;
            target.headOfFamily = head;
            target.familyName = family;
            target.phoneNumber = phone;
            target.monthlyFee = fee;
            target.wardNumber = ward;
            target.memberCount = members;
            target.rationCategory = ration;
            target.address = address;
            target.notes = notes;
        }
        showToast("കുടുംബവിവരങ്ങൾ അപ്ഡേറ്റ് ചെയ്തു");
    } else {
        // Add
        const newId = Date.now();
        AppState.data.households.push({
            id: newId,
            mahalCode: AppState.data.activeMahalCode,
            houseNumber: houseNo,
            headOfFamily: head,
            familyName: family,
            phoneNumber: phone,
            monthlyFee: fee,
            wardNumber: ward,
            memberCount: members,
            rationCategory: ration,
            address: address,
            verificationStatus: "VERIFIED",
            notes: notes
        });
        showToast("പുതിയ കുടുംബത്തെ വിജയകരമായി ചേർത്തു");
    }

    AppState.saveData();
    closeModal("householdModalOverlay");
    renderSubscriptions();
    renderDashboard();
}

// Add Cash Transaction Modal
function openAddTransactionModal(defaultType = 'INCOME') {
    document.getElementById("transactionForm").reset();
    document.getElementById("txTypeInput").value = defaultType;
    document.getElementById("txVoucherInput").value = `V-${defaultType.slice(0, 3)}-${Math.floor(100 + Math.random() * 900)}`;
    document.getElementById("txDateInput").value = new Date().toISOString().split('T')[0];
    document.getElementById("transactionModalOverlay").classList.add("active");
}

function saveTransaction(e) {
    e.preventDefault();
    const type = document.getElementById("txTypeInput").value;
    const title = document.getElementById("txTitleInput").value.trim();
    const category = document.getElementById("txCategoryInput").value.trim();
    const amount = Number(document.getElementById("txAmountInput").value);
    const voucher = document.getElementById("txVoucherInput").value.trim();
    const mode = document.getElementById("txModeInput").value;
    const dateVal = document.getElementById("txDateInput").value;
    const desc = document.getElementById("txDescInput").value.trim();

    if (!amount || amount <= 0) {
        showToast("ദയവായി സാധുവായ തുക നൽകുക", "error");
        return;
    }

    const newTx = {
        id: Date.now(),
        mahalCode: AppState.data.activeMahalCode,
        type: type,
        category: category,
        amount: amount,
        transactionDate: dateVal ? new Date(dateVal).getTime() : Date.now(),
        title: title,
        description: desc,
        voucherNumber: voucher,
        paymentMode: mode
    };

    AppState.data.cashTransactions.push(newTx);
    AppState.saveData();
    closeModal("transactionModalOverlay");
    renderLedger();
    renderDashboard();
    showToast(`${type === 'INCOME' ? 'വരവ്' : 'ചിലവ്'} ഇടപാട് വിജയകരമായി രേഖപ്പെടുത്തി!`);
}

function deleteTransaction(id) {
    if (!confirm("ഈ ഇടപാട് ലെഡ്ജറിൽ നിന്ന് നീക്കം ചെയ്യണമെന്ന് ഉറപ്പാണോ?")) return;
    AppState.data.cashTransactions = AppState.data.cashTransactions.filter(t => t.id !== id);
    AppState.saveData();
    renderLedger();
    renderDashboard();
    showToast("ഇടപാട് നീക്കം ചെയ്തു");
}

// Special Funds / Collections
function openCreateSpecialFundModal() {
    document.getElementById("specialFundForm").reset();
    document.getElementById("specialFundModalOverlay").classList.add("active");
}

function saveSpecialFund(e) {
    e.preventDefault();
    const title = document.getElementById("fundTitleInput").value.trim();
    const desc = document.getElementById("fundDescInput").value.trim();
    const targetFamily = Number(document.getElementById("fundTargetFamilyInput").value) || 500;
    const targetTotal = Number(document.getElementById("fundTargetTotalInput").value) || 100000;
    const category = document.getElementById("fundCategoryInput").value.trim() || "Relief";

    AppState.data.additionalCollections.push({
        id: Date.now(),
        mahalCode: AppState.data.activeMahalCode,
        title: title,
        description: desc,
        targetAmountPerFamily: targetFamily,
        targetTotalFund: targetTotal,
        category: category,
        dueDate: Date.now() + 60 * 86400000
    });

    AppState.saveData();
    closeModal("specialFundModalOverlay");
    renderSpecialFunds();
    showToast("പുതിയ പ്രത്യേക ഫണ്ട് ആരംഭിച്ചു");
}

function openRecordSpecialPaymentModal(fundId) {
    const fund = AppState.data.additionalCollections.find(f => f.id === fundId);
    if (!fund) return;
    document.getElementById("specialPaymentFundId").value = fund.id;
    document.getElementById("specialPaymentFundTitle").textContent = fund.title;
    document.getElementById("specialPaymentAmount").value = fund.targetAmountPerFamily;

    const select = document.getElementById("specialPaymentHouseholdSelect");
    select.innerHTML = AppState.getHouseholdsForActiveMahal().map(h =>
        `<option value="${h.id}">${h.headOfFamily} (${h.houseNumber})</option>`
    ).join("");

    document.getElementById("specialPaymentModalOverlay").classList.add("active");
}

function saveSpecialFundPayment(e) {
    e.preventDefault();
    const fundId = Number(document.getElementById("specialPaymentFundId").value);
    const householdId = Number(document.getElementById("specialPaymentHouseholdSelect").value);
    const amount = Number(document.getElementById("specialPaymentAmount").value);
    const mode = document.getElementById("specialPaymentMode").value;

    const h = AppState.data.households.find(x => x.id === householdId);
    const fund = AppState.data.additionalCollections.find(x => x.id === fundId);

    AppState.data.additionalPayments.push({
        id: Date.now(),
        collectionId: fundId,
        householdId: householdId,
        mahalCode: AppState.data.activeMahalCode,
        householdName: h ? h.headOfFamily : "Unknown",
        houseNumber: h ? h.houseNumber : "-",
        amount: amount,
        receiptNumber: `RCP-COL-${Math.floor(100 + Math.random() * 900)}`,
        paymentMode: mode,
        collectorName: "Sub-Admin",
        date: Date.now(),
        notes: fund ? fund.title : "Special Fund Payment"
    });

    AppState.saveData();
    closeModal("specialPaymentModalOverlay");
    renderSpecialFunds();
    showToast("ഫണ്ട് തുക വിജയകരമായി രേഖപ്പെടുത്തി!");
}

// Digital Receipt Display
let currentReceiptData = null;

function showDigitalReceipt(data) {
    currentReceiptData = data;
    document.getElementById("receiptNo").textContent = data.receiptNumber;
    document.getElementById("receiptCommunity").textContent = data.communityName;
    document.getElementById("receiptDate").textContent = data.dateFormatted;
    document.getElementById("receiptHousehold").textContent = `${data.householdName} (${data.houseNumber})`;
    document.getElementById("receiptMonths").textContent = data.monthsPaid.join(", ") + ` ${data.year}`;
    document.getElementById("receiptMode").textContent = data.paymentMethod;
    document.getElementById("receiptCollector").textContent = data.collectorName;
    document.getElementById("receiptAmount").textContent = formatCurrency(data.totalAmount);

    document.getElementById("receiptModalOverlay").classList.add("active");
}

function viewReceiptByPaymentId(paymentId) {
    const p = AppState.data.subscriptionPayments.find(x => x.id === paymentId);
    if (!p) return;
    const h = AppState.data.households.find(x => x.id === p.householdId);
    showDigitalReceipt({
        receiptNumber: p.receiptNumber,
        householdName: h ? h.headOfFamily : "Unknown",
        houseNumber: h ? h.houseNumber : "-",
        monthsPaid: [MONTH_NAMES[p.month - 1]],
        year: p.year,
        totalAmount: p.amountPaid,
        paymentMethod: p.paymentMethod,
        dateFormatted: formatDate(p.paymentDate),
        collectorName: p.collectedBy || "Treasurer",
        communityName: AppState.getActiveCommunity().name
    });
}

function printReceipt() {
    window.print();
}

function shareReceiptWhatsApp() {
    if (!currentReceiptData) return;
    const text = encodeURIComponent(
        `*${currentReceiptData.communityName}*\n` +
        `ഡിജിറ്റൽ വരിസംഖ്യാ രസീത്\n` +
        `----------------------------\n` +
        `രസീത് നമ്പർ: ${currentReceiptData.receiptNumber}\n` +
        `തീയതി: ${currentReceiptData.dateFormatted}\n` +
        `കുടുംബനാഥൻ: ${currentReceiptData.householdName} (${currentReceiptData.houseNumber})\n` +
        `മാസങ്ങൾ: ${currentReceiptData.monthsPaid.join(", ")} ${currentReceiptData.year}\n` +
        `അടച്ച തുക: ₹${currentReceiptData.totalAmount}\n` +
        `രീതി: ${currentReceiptData.paymentMethod}\n` +
        `സ്വീകരിച്ചത്: ${currentReceiptData.collectorName}\n` +
        `----------------------------\n` +
        `മഹല്ല് ലെഡ്ജർ മുഖേന സുതാര്യമായി രേഖപ്പെടുത്തിയത്.`
    );
    window.open(`https://api.whatsapp.com/send?text=${text}`, '_blank');
}

// WhatsApp Payment Reminder
function sendWhatsAppDuesReminder(householdId) {
    const h = AppState.data.households.find(x => x.id === householdId);
    if (!h) return;

    const payments = AppState.getPaymentsForHousehold(h.id);
    const paidMonths = new Set(payments.map(p => p.month));
    const pendingMonthsCount = Math.max(0, 12 - paidMonths.size);
    const dueAmount = pendingMonthsCount * h.monthlyFee;
    const comm = AppState.getActiveCommunity();

    const unpaidMonthsList = [];
    for (let m = 1; m <= 12; m++) {
        if (!paidMonths.has(m)) unpaidMonthsList.push(MONTH_NAMES_ML[m - 1]);
    }

    const text = encodeURIComponent(
        `അസ്സലാമു അലൈക്കും,\n` +
        `*${comm.name}*\n` +
        `------------------------------------\n` +
        `ബഹുമാനപ്പെട്ട ${h.headOfFamily} (${h.houseNumber}),\n\n` +
        `നിങ്ങളുടെ കുടുംബത്തിന്റെ ${AppState.data.currentYear} വർഷത്തെ മഹല്ല് പ്രതിമാസ വരിസംഖ്യയിൽ *${pendingMonthsCount} മാസത്തെ* കുടിശ്ശിക (${formatCurrency(dueAmount)}) നിലനിൽക്കുന്നുണ്ട്.\n` +
        `കുടിശ്ശികയുള്ള മാസങ്ങൾ: ${unpaidMonthsList.join(", ")}\n\n` +
        `മഹല്ല് കമ്മിറ്റിയുടെ പ്രവർത്തനങ്ങൾ സുഗമമായി നടത്തുന്നതിനായി ഈ തുക എത്രയും വേഗം താഴെ പറയുന്ന UPI വഴിയോ കമ്മറ്റി ഓഫീസിലോ എത്തിക്കണമെന്ന് അഭ്യർത്ഥിക്കുന്നു.\n\n` +
        `🔹 *UPI ID:* ${comm.upiId || 'alnoormahal@sbi'}\n` +
        `🔹 *ബാങ്ക്:* ${comm.bankName || 'SBI Valanchery'}\n` +
        `🔹 *A/c No:* ${comm.accountNumber || '67324419082'}\n` +
        `🔹 *IFSC:* ${comm.ifscCode || 'SBIN0070188'}\n\n` +
        `നന്ദി,\nസെക്രട്ടറി / ട്രഷറർ`
    );

    const cleanPhone = (h.phoneNumber || '').replace(/\D/g, '');
    const phoneParam = cleanPhone.length === 10 ? `91${cleanPhone}` : cleanPhone;
    window.open(`https://api.whatsapp.com/send?phone=${phoneParam}&text=${text}`, '_blank');
}

// Family Due Alert & UPI Payment Modal
function openFamilyDueAlert(householdId) {
    const h = AppState.data.households.find(x => x.id === householdId);
    if (!h) return;

    const payments = AppState.getPaymentsForHousehold(h.id);
    const paidMonths = new Set(payments.map(p => p.month));
    const pendingMonthsCount = Math.max(0, 12 - paidMonths.size);
    const dueAmount = pendingMonthsCount * h.monthlyFee;
    const comm = AppState.getActiveCommunity();

    document.getElementById("dueAlertFamilyHead").textContent = `${h.headOfFamily} (${h.houseNumber})`;
    document.getElementById("dueAlertMonthsCount").textContent = `${pendingMonthsCount} മാസത്തെ കുടിശ്ശിക`;
    document.getElementById("dueAlertAmount").textContent = formatCurrency(dueAmount);

    const upiId = comm.upiId || 'alnoormahal@sbi';
    const upiLink = `upi://pay?pa=${upiId}&pn=${encodeURIComponent(comm.name)}&am=${dueAmount}&cu=INR&tn=${encodeURIComponent(`Mahal Vari - ${h.houseNumber}`)}`;

    // QR Code Image
    const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=${encodeURIComponent(upiLink)}`;
    document.getElementById("dueAlertQrCode").src = qrUrl;
    document.getElementById("dueAlertUpiId").textContent = upiId;

    const payBtn = document.getElementById("dueAlertPayUpiBtn");
    payBtn.href = upiLink;

    document.getElementById("familyDueAlertModalOverlay").classList.add("active");
}

// 11. Authentication
function handleAdminLogin(e) {
    e.preventDefault();
    const user = document.getElementById("loginUsername").value.trim();
    const pass = document.getElementById("loginPassword").value.trim();

    // Check Super-Admin
    if ((user.toUpperCase() === "AMINZAD" && pass === "A4aminzad") ||
        (user === "admin" && pass === "9999") ||
        (pass === "9999" && !user)) {
        AppState.currentUser = {
            isAuthenticated: true,
            role: "SUPER_ADMIN",
            mahalCode: AppState.data.activeMahalCode,
            memberHouseholdId: null,
            username: "AMINZAD"
        };
        loginSuccess("Welcome Super-Admin AMINZAD! Master Control Active");
        return;
    }

    // Check Sub-Admin against communities
    const match = AppState.data.communities.find(c =>
        c.subAdminUsername.toLowerCase() === user.toLowerCase() && c.subAdminPassword === pass
    );

    if (match) {
        AppState.data.activeMahalCode = match.mahalCode;
        AppState.saveData();
        AppState.currentUser = {
            isAuthenticated: true,
            role: "SUB_ADMIN",
            mahalCode: match.mahalCode,
            memberHouseholdId: null,
            username: user
        };
        loginSuccess(`Welcome Sub-Admin of ${match.name}`);
        return;
    }

    showToast("Invalid Username or Password!", "error");
}

function handleMemberOtpRequest() {
    const phone = document.getElementById("loginMemberPhone").value.trim();
    const match = AppState.data.households.find(h => h.phoneNumber.replace(/\D/g, '') === phone.replace(/\D/g, ''));

    if (!match) {
        showToast("ഈ ഫോൺ നമ്പറിൽ കുടുംബം രജിസ്റ്റർ ചെയ്തിട്ടില്ല", "error");
        return;
    }

    document.getElementById("otpSentMessage").textContent = `✓ OTP ${match.phoneNumber} ലേക്ക് അയച്ചു (ടെസ്റ്റിംഗ് OTP: 1234)`;
    document.getElementById("otpSentMessage").style.display = "block";
    document.getElementById("memberOtpStep").style.display = "block";
    document.getElementById("loginMemberOtp").value = "1234";
    showToast(`OTP അയച്ചു: ${match.headOfFamily}`);
}

function handleMemberLogin() {
    const phone = document.getElementById("loginMemberPhone").value.trim();
    const otp = document.getElementById("loginMemberOtp").value.trim();
    const match = AppState.data.households.find(h => h.phoneNumber.replace(/\D/g, '') === phone.replace(/\D/g, ''));

    if (match && otp === "1234") {
        AppState.data.activeMahalCode = match.mahalCode;
        AppState.saveData();
        AppState.currentUser = {
            isAuthenticated: true,
            role: "MEMBER",
            mahalCode: match.mahalCode,
            memberHouseholdId: match.id,
            username: match.headOfFamily
        };
        loginSuccess(`സ്വാഗതം, ${match.headOfFamily} (${match.houseNumber})`);
    } else {
        showToast("തെറ്റായ OTP അല്ലെങ്കിൽ ഫോൺ നമ്പർ", "error");
    }
}

function quickFillLogin(username, password) {
    document.getElementById("loginUsername").value = username;
    document.getElementById("loginPassword").value = password;
    document.getElementById("adminLoginForm").requestSubmit();
}

function quickFillMemberPhone(phone) {
    document.getElementById("loginMemberPhone").value = phone;
    handleMemberOtpRequest();
}

function loginSuccess(msg) {
    document.getElementById("loginView").style.display = "none";
    document.getElementById("appMainView").style.display = "block";
    updateAppHeader();
    renderDashboard();
    showToast(msg);
}

function logout() {
    AppState.currentUser.isAuthenticated = false;
    AppState.currentUser.memberHouseholdId = null;
    document.getElementById("loginView").style.display = "block";
    document.getElementById("appMainView").style.display = "none";
    showToast("Logged out successfully");
}

function updateAppHeader() {
    const comm = AppState.getActiveCommunity();
    document.getElementById("headerCommunityName").textContent = comm.name;
    document.getElementById("headerLocation").textContent = `${comm.location} • Reg: ${comm.regNumber}`;

    const roleLabel = AppState.currentUser.role === 'SUPER_ADMIN' ? '👑 സൂപ്പർ അഡ്മിൻ' :
        AppState.currentUser.role === 'MEMBER' ? `👨‍👩‍👧‍👦 ${AppState.currentUser.username}` :
            '🕌 സബ് അഡ്മിൻ';
    document.getElementById("headerUserRole").textContent = roleLabel;

    // Show/hide Super Admin tab
    const superTab = document.querySelector('[data-tab="view-superadmin"]');
    if (superTab) {
        superTab.style.display = AppState.currentUser.role === 'SUPER_ADMIN' ? 'inline-flex' : 'none';
    }
}

// 12. Backup, Restore & CSV Export
function exportBackupJSON() {
    const jsonStr = JSON.stringify(AppState.data, null, 2);
    const blob = new Blob([jsonStr], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `MahalLedger_Backup_${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
    showToast("ഡാറ്റ ബാക്കപ്പ് ഫയൽ ഡൗൺലോഡ് ആയി!");
}

function importBackupJSON(event) {
    const file = event.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function (e) {
        try {
            const imported = JSON.parse(e.target.result);
            if (imported.communities && imported.households) {
                AppState.data = imported;
                AppState.saveData();
                updateAppHeader();
                renderDashboard();
                showToast("ബാക്കപ്പ് വിജയകരമായി റീസ്റ്റോർ ചെയ്തു!");
            } else {
                showToast("അസാധുവായ ബാക്കപ്പ് ഫയൽ", "error");
            }
        } catch (err) {
            showToast("ഫയൽ വായിക്കുന്നതിൽ പിശക്", "error");
        }
    };
    reader.readAsText(file);
}

function exportHouseholdsCSV() {
    const households = AppState.getHouseholdsForActiveMahal();
    const payments = AppState.data.subscriptionPayments.filter(p => p.year === AppState.data.currentYear);

    let csv = "House Number,Head of Family,Family Name,Phone,Monthly Fee,Paid Months,Pending Dues,Ward\n";
    households.forEach(h => {
        const hPayments = payments.filter(p => p.householdId === h.id);
        const dueMonths = Math.max(0, 12 - hPayments.length);
        const due = dueMonths * h.monthlyFee;
        csv += `"${h.houseNumber}","${h.headOfFamily}","${h.familyName || ''}","${h.phoneNumber}",${h.monthlyFee},${hPayments.length},${due},"${h.wardNumber || ''}"\n`;
    });

    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `Mahal_Members_Report_${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    URL.revokeObjectURL(url);
    showToast("Excel/CSV റിപ്പോർട്ട് ഡൗൺലോഡ് ആയി!");
}

// 13. Modal Utility
function closeModal(overlayId) {
    const el = document.getElementById(overlayId);
    if (el) el.classList.remove("active");
}

function openApkDownloadModal() {
    document.getElementById("apkModalOverlay").classList.add("active");
}

// Event Listeners Initialization
window.addEventListener("DOMContentLoaded", () => {
    // Nav tabs click
    document.querySelectorAll(".nav-tab-btn").forEach(btn => {
        btn.addEventListener("click", () => switchMainTab(btn.dataset.tab));
    });

    // Subscriptions search & filters
    const subSearch = document.getElementById("subSearchInput");
    if (subSearch) {
        subSearch.addEventListener("input", (e) => {
            currentSubSearch = e.target.value;
            renderSubscriptions();
        });
    }

    document.querySelectorAll(".sub-filter-chip").forEach(chip => {
        chip.addEventListener("click", () => {
            document.querySelectorAll(".sub-filter-chip").forEach(c => c.classList.remove("active"));
            chip.classList.add("active");
            currentSubFilter = chip.dataset.filter;
            renderSubscriptions();
        });
    });

    // Subscriptions sub-tabs (Monthly vs Special)
    const tabMonthly = document.getElementById("tabSubMonthly");
    const tabSpecial = document.getElementById("tabSubSpecial");
    if (tabMonthly && tabSpecial) {
        tabMonthly.addEventListener("click", () => {
            tabMonthly.classList.add("active");
            tabSpecial.classList.remove("active");
            currentSubTab = "monthly";
            renderSubscriptions();
        });
        tabSpecial.addEventListener("click", () => {
            tabSpecial.classList.add("active");
            tabMonthly.classList.remove("active");
            currentSubTab = "special";
            renderSubscriptions();
        });
    }

    // Ledger search & filters
    const ledgerSearch = document.getElementById("ledgerSearchInput");
    if (ledgerSearch) {
        ledgerSearch.addEventListener("input", (e) => {
            currentLedgerSearch = e.target.value;
            renderLedger();
        });
    }

    document.querySelectorAll(".ledger-filter-chip").forEach(chip => {
        chip.addEventListener("click", () => {
            document.querySelectorAll(".ledger-filter-chip").forEach(c => c.classList.remove("active"));
            chip.classList.add("active");
            currentLedgerFilter = chip.dataset.filter;
            renderLedger();
        });
    });

    // Login tab switcher
    const loginAdminTab = document.getElementById("loginTabAdmin");
    const loginFamilyTab = document.getElementById("loginTabFamily");
    if (loginAdminTab && loginFamilyTab) {
        loginAdminTab.addEventListener("click", () => {
            loginAdminTab.classList.add("active");
            loginFamilyTab.classList.remove("active");
            document.getElementById("adminLoginForm").style.display = "flex";
            document.getElementById("familyLoginForm").style.display = "none";
        });
        loginFamilyTab.addEventListener("click", () => {
            loginFamilyTab.classList.add("active");
            loginAdminTab.classList.remove("active");
            document.getElementById("adminLoginForm").style.display = "none";
            document.getElementById("familyLoginForm").style.display = "flex";
        });
    }

    // Click outside modal to close
    document.querySelectorAll(".modal-overlay").forEach(overlay => {
        overlay.addEventListener("click", (e) => {
            if (e.target === overlay) overlay.classList.remove("active");
        });
    });

    // Initial Login View Setup
    document.getElementById("loginView").style.display = "block";
    document.getElementById("appMainView").style.display = "none";
});

