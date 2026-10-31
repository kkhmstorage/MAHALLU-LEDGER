# Cloud Data Persistence, Zero-Data-Loss Architecture & Multi-Tenant Management

A robust, enterprise-grade cloud data persistence and disaster recovery architecture for **Mahal Ledger**, guaranteeing that community subscription records, financial accounts, and member registries are never lost, remain operational offline, and sync seamlessly across devices.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following requirements were confirmed during clarification:
> - **Primary Storage Engine**: High-durability Cloud Database (**Firebase Firestore** with built-in offline disk persistence). Replaces purely device-local storage so that data can never be lost if a phone is lost, damaged, formatted, or replaced.
> - **Offline Collection Capability**: Treasurers must be able to collect subscriptions (*Varisankhya*) house-to-house without internet; transactions are saved instantly to local cache and automatically committed to the cloud once network connectivity resumes.
> - **Secondary Failsafe Backups**: Automated monthly **Excel / CSV spreadsheet exports** and **PDF Statement generation**, allowing committees to archive offline copies to WhatsApp, Google Drive, or physical files.
> - **Multi-Tenant Role Hierarchy**:
>   1. **Super-Admin**: Platform owner registers and controls all Mahals, sets Sub-Admin credentials, and can activate/suspend Mahals.
>   2. **Sub-Admin (Mahal Treasurer)**: Enters assigned username & password to manage their Mahal's households, dues, and accounts.
>   3. **Community Member**: Enters mobile number, receives OTP, and accesses personal receipts and transparent accounts.

---

## 1. Overview & Data Safety Strategy

### Where Is the Data Stored?
- **Cloud Layer (Primary)**: Stored in **Google Cloud Firestore**. Data is redundantly replicated across multiple Google data centers with 99.999% durability.
- **On-Device Cache (Secondary)**: SQLite/LRU disk cache on the phone. Enables instantaneous read/write speeds (<5ms) and uninterrupted offline collection.
- **Local Document Storage (Archive)**: Monthly exported CSV and PDF reports saved in the device's Downloads directory and shareable via Android Share Sheet.

### Will Any Data Ever Be Lost?
**No, because of a 3-layer protection protocol:**
1. **Device-Independent Cloud Recovery**: If a treasurer loses their phone, buys a new device, or deletes the app, logging back in immediately restores all families, past receipts, vouchers, and cash balances within seconds.
2. **Transactional Offline Queuing**: If a collection is recorded with zero mobile data, it is written to the persistent local write queue. It cannot be lost even if the phone reboots; Android triggers background synchronization as soon as cellular data or Wi-Fi reconnects.
3. **Monthly Offline Snapshot Exports**: Committees can generate one-click Excel/CSV spreadsheets and PDF audit summaries at the end of every calendar month for offline archiving.

---

## 2. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                              Mobile Clients                            │
│  ┌───────────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │   Super-Admin Console │  │ Sub-Admin Console│  │ Member OTP View  │ │
│  │ (Provision Mahals/PWD)│  │ (Collect/Ledger) │  │(Statements & Rec)│ │
│  └───────────┬───────────┘  └────────┬─────────┘  └────────┬─────────┘ │
└──────────────┼───────────────────────┼─────────────────────┼───────────┘
               │                       │                     │
               ▼                       ▼                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                Offline-First Repository & Sync Engine                  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ Local Persistence Cache (Instant UI + Zero-Network Collection)   │  │
│  │ • Pending Sync Write Queue                                       │  │
│  │ • Local Receipt & Voucher Serializer                             │  │
│  └───────────────────────────────────┬──────────────────────────────┘  │
└──────────────────────────────────────┼─────────────────────────────────┘
                                       │ Realtime Two-Way Sync
                                       ▼
┌────────────────────────────────────────────────────────────────────────┐
│              Google Cloud Firestore (Enterprise Backend)               │
│                                                                        │
│  📁 /communities/{mahalCode}/                                          │
│     ├── 📄 metadata (Name, RegNo, SubAdmin Username & Password, State) │
│     ├── 📁 /households/{houseId} (House#, Head, Phone, MonthlyRate)   │
│     ├── 📁 /payments/{paymentId} (Month, Year, Amount, Receipt#, Mode) │
│     └── 📁 /transactions/{txId} (Income/Expense, Voucher#, Category)   │
│                                                                        │
│  📁 /system/                                                           │
│     └── 📄 super_admin_directory (Global Mahal indexing & status)      │
└────────────────────────────────────────────────────────────────────────┘
```

### Cloud Schema Breakdown
1. **`communities/{mahalCode}`**:
   - `mahalCode: String` (e.g., `"ALNOOR"`, `"MHL-104"`)
   - `name: String` (e.g., `"Al-Noor Madrasa & Mahal Committee"`)
   - `subAdminUsername: String`
   - `subAdminPassword: String`
   - `contactPhone: String`
   - `location: String`
   - `isActive: Boolean` (Super-Admin toggle)
   - `createdAt: Timestamp`

2. **`communities/{mahalCode}/households/{householdId}`**:
   - `houseNumber: String` (e.g., `"H-01"`)
   - `headOfFamily: String` (e.g., `"K. P. Abdul Rahman"`)
   - `phoneNumber: String` (e.g., `"9846011223"`)
   - `monthlyFee: Double` (e.g., `250.0`)
   - `wardNumber: String`
   - `memberCount: Int`
   - `address: String`

3. **`communities/{mahalCode}/payments/{paymentId}`**:
   - `householdId: String`
   - `month: Int` (1..12)
   - `year: Int` (e.g., 2026)
   - `amountPaid: Double`
   - `paymentMethod: String` (`"CASH"`, `"UPI"`, `"BANK_TRANSFER"`)
   - `receiptNumber: String` (e.g., `"REC-2026-10-H01"`)
   - `collectedBy: String`
   - `timestamp: Timestamp`

4. **`communities/{mahalCode}/transactions/{transactionId}`**:
   - `type: String` (`"INCOME"` or `"EXPENSE"`)
   - `category: String`
   - `amount: Double`
   - `title: String`
   - `description: String`
   - `voucherNumber: String`
   - `timestamp: Timestamp`

---

## 3. Key Product Decisions & Execution Phases

### Decision 1: Hybrid Offline-First Sync Architecture
- **Approach**: Combine Room Local Cache with Cloud Firestore snapshot listeners and synchronized transactions.
- **Why**: Allows instant app launch, zero UI stutter, and full functionality even without cellular reception inside rural wards or masjid basements. When online, changes replicate to the cloud within milliseconds.

### Decision 2: Secondary Failsafe: Monthly Excel (CSV) & PDF Export
- **Approach**: Built-in export engine in the Settings/Audit screen that generates standard `.csv` (Excel compatible) sheets and formatted `.txt` / printable audit statements of all households and transactions.
- **Why**: Committee elders and auditors value having a tangible spreadsheet file that can be kept in a folder or shared on WhatsApp committee groups every month.

### Decision 3: Member OTP Authentication Flow
- **Approach**: Members enter their 10-digit mobile phone number. The system verifies if that phone is registered under any household. A secure 4-digit verification code is generated to validate session access.
- **Why**: Eliminates forgotten passwords for village elders while preventing random outsiders from accessing personal family dues.

---

## 4. Execution & Rollout Steps

1. **Phase 1: Multi-Tenant Cloud Provisioning & Service Configuration**:
   - Provision Firestore database via `set_up_firebase` tool and configure security rules ensuring sub-admins only read/write their own Mahal subcollections.
2. **Phase 2: Offline-First Synchronized Repository**:
   - Implement `FirestoreMahalRepository` with bidirectional sync: Local Room database acts as the high-speed local cache, syncing seamlessly with Firestore collections.
   - Add network state monitoring with a live "Cloud Sync Status" badge (🟢 *Synced* / 🟡 *Syncing*).
3. **Phase 3: Failsafe Export Engine (CSV/Excel & PDF Generator)**:
   - Implement `ReportExporter` utility that formats all household payment records and ledger vouchers into downloadable `.csv` and shareable WhatsApp text summaries.
4. **Phase 4: Super-Admin & Sub-Admin Cloud Controls**:
   - Connect Super-Admin Mahal provisioning dialog directly to the cloud backend so new Mahals created by the Super-Admin are instantly accessible to their Sub-Admins across any Android device.
5. **Phase 5: Verification & Unit Testing**:
   - Execute test cases verifying offline payment queuing, cloud synchronization, and spreadsheet generation.
