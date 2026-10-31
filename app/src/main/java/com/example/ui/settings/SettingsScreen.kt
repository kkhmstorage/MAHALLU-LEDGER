package com.example.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MahalCommunity
import com.example.ui.MadrasaViewModel
import com.example.ui.UserRole
import com.example.ui.components.CreateMahalDialog
import com.example.ui.components.MemberOtpLoginDialog
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.components.SubAdminLoginDialog
import com.example.ui.components.SuperAdminLoginDialog
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.CashOutflowContainer
import com.example.ui.theme.CashOutflowRed
import com.example.util.DriveBackupState

import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Image
import com.example.ui.auth.CommunityLogoEmblem
import com.example.ui.settings.CommunityLogoDialog

@Composable
fun SettingsScreen(
    viewModel: MadrasaViewModel,
    modifier: Modifier = Modifier
) {
    val role by viewModel.currentRole.collectAsState()
    val activeMahal by viewModel.activeMahal.collectAsState()
    val activeMember by viewModel.activeMemberHousehold.collectAsState()
    val allCommunities by viewModel.allCommunities.collectAsState()

    var showRoleSwitcherDialog by remember { mutableStateOf(false) }
    var showSubAdminLoginDialog by remember { mutableStateOf(false) }
    var showSuperAdminLoginDialog by remember { mutableStateOf(false) }
    var showMemberOtpDialog by remember { mutableStateOf(false) }
    var showCreateMahalDialog by remember { mutableStateOf(false) }
    var showCommunityLogoDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Control Center & Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Super-Admin control, Sub-Admin management, and app distribution",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Active Session Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (role) {
                                            UserRole.SUPER_ADMIN -> MaterialTheme.colorScheme.tertiaryContainer
                                            UserRole.SUB_ADMIN -> MaterialTheme.colorScheme.primaryContainer
                                            UserRole.MEMBER -> CashInflowContainer
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (role) {
                                        UserRole.SUPER_ADMIN -> Icons.Default.Lock
                                        UserRole.SUB_ADMIN -> Icons.Default.Security
                                        UserRole.MEMBER -> Icons.Default.Person
                                    },
                                    contentDescription = null,
                                    tint = when (role) {
                                        UserRole.SUPER_ADMIN -> MaterialTheme.colorScheme.onTertiaryContainer
                                        UserRole.SUB_ADMIN -> MaterialTheme.colorScheme.primary
                                        UserRole.MEMBER -> CashInflowGreen
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = when (role) {
                                        UserRole.SUPER_ADMIN -> "Super-Admin Mode"
                                        UserRole.SUB_ADMIN -> "Sub-Admin: ${activeMahal?.subAdminUsername ?: "Treasurer"}"
                                        UserRole.MEMBER -> "Member: ${activeMember?.headOfFamily ?: "Family"}"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = when (role) {
                                        UserRole.SUPER_ADMIN -> "Platform Owner • All Mahals Access"
                                        UserRole.SUB_ADMIN -> "${activeMahal?.name ?: "Mahal"} (${activeMahal?.mahalCode ?: ""})"
                                        UserRole.MEMBER -> "House: ${activeMember?.houseNumber} • ${activeMahal?.name ?: ""}"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showRoleSwitcherDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Switch Account")
                        }

                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Logout")
                        }
                    }
                }
            }
        }

        // Section A: Super-Admin Control Panel (Visible in Super-Admin mode)
        if (role == UserRole.SUPER_ADMIN) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Onboarded Mahals & Sub-Admins",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage community workspaces, sub-admin accounts, and status",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showCreateMahalDialog = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Mahal")
                    }
                }
            }

            items(allCommunities, key = { it.mahalCode }) { community ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = community.mahalCode,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = community.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${community.location} • Phone: ${community.contactPhone}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = community.isActive,
                                onCheckedChange = { viewModel.toggleMahalStatus(community) }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Sub-Admin Account:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "User: ${community.subAdminUsername}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.switchActiveMahal(community)
                                    viewModel.setRole(UserRole.SUB_ADMIN)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Open Workspace", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Section B: Sub-Admin View (Community Profile & Logo Upload)
        if (role == UserRole.SUB_ADMIN) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                    text = "കമ്മ്യൂണിറ്റി ലോഗോ & ബ്രാൻഡിംഗ്",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "ആപ്പ് തുറക്കുമ്പോൾ സൂം ഔട്ട് ആയി കാണിക്കുന്ന ലോഗോ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            activeMahal?.let { mahal ->
                                CommunityLogoEmblem(
                                    logoUri = mahal.logoUri,
                                    logoPreset = mahal.logoPreset,
                                    communityName = mahal.name,
                                    size = 50.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { showCommunityLogoDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("കമ്മ്യൂണിറ്റി ലോഗോ അപ്‌ലോഡ് ചെയ്യുക (Upload Logo)")
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Your Assigned Mahal Workspace",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        DetailRow(label = "Mahal Name", value = activeMahal?.name ?: "Al-Noor Mahal")
                        DetailRow(label = "Unique Mahal Code", value = activeMahal?.mahalCode ?: "ALNOOR")
                        DetailRow(label = "Sub-Admin Username", value = activeMahal?.subAdminUsername ?: "alnoor_admin")
                        DetailRow(label = "Location", value = activeMahal?.location ?: "Kerala")
                        DetailRow(label = "Helpline Contact", value = activeMahal?.contactPhone ?: "")
                        DetailRow(label = "Status", value = if (activeMahal?.isActive == true) "Active & Verified" else "Suspended")
                    }
                }
            }
        }

        // Section: Google Drive Cloud Backup
        item {
            val context = androidx.compose.ui.platform.LocalContext.current
            val driveStatus by viewModel.driveBackupStatus.collectAsState()

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = CashInflowGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Google Drive Cloud Backup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CashInflowContainer
                        ) {
                            Text(
                                text = if (driveStatus.state == DriveBackupState.SUCCESS) "Backed Up ✓" else "OAuth Linked ✓",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = CashInflowGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Automated zero-loss cloud backup to your authorized Google Drive account. Generates full JSON snapshots of all families, subscriptions, and financial vouchers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    DetailRow(label = "Destination Folder", value = driveStatus.backupFolder)
                    DetailRow(label = "Last Backup", value = driveStatus.lastBackupDate)
                    DetailRow(label = "Status", value = driveStatus.statusMessage)

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.backupToGoogleDrive(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_backup_drive_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (driveStatus.state == DriveBackupState.UPLOADING) "Uploading to Google Drive..." else "Backup to Google Drive Now",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section C: How to Distribute & Scale (ആളുകളിലേക്ക് എത്തിക്കാനുള്ള വഴി)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Distribution & Rollout Strategy",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Single Mobile App on Play Store: Upload this one app to Google Play Store. Any community member across any Mahal can download it.\n\n" +
                               "2. Super-Admin Controlled Provisioning: When a new Mahal contacts you, open Super-Admin mode (Master PIN: 9999), tap 'Add Mahal', and generate their Sub-Admin credentials.\n\n" +
                               "3. Sub-Admin Data Entry: The Sub-Admin logs in with their credentials and registers their Mahal's households with house numbers and phone numbers.\n\n" +
                               "4. Member Self-Service with Phone OTP: Any family member enters their mobile number, verifies with OTP, and immediately sees their verified payments and dues without needing to remember complicated passwords.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleSwitcherDialog) {
        RoleSwitcherDialog(
            currentRole = role,
            onDismiss = { showRoleSwitcherDialog = false },
            onSelectSubAdminLogin = { showSubAdminLoginDialog = true },
            onSelectMemberOtpLogin = { showMemberOtpDialog = true },
            onSelectSuperAdminLogin = { showSuperAdminLoginDialog = true }
        )
    }

    if (showSubAdminLoginDialog) {
        SubAdminLoginDialog(
            onDismiss = { showSubAdminLoginDialog = false },
            onLogin = { username, password ->
                viewModel.loginSubAdmin(username, password) { success, _ ->
                    if (success) showSubAdminLoginDialog = false
                }
            }
        )
    }

    if (showSuperAdminLoginDialog) {
        SuperAdminLoginDialog(
            onDismiss = { showSuperAdminLoginDialog = false },
            onLogin = { pin ->
                viewModel.loginSuperAdmin(pin) { success ->
                    if (success) showSuperAdminLoginDialog = false
                }
            }
        )
    }

    if (showMemberOtpDialog) {
        MemberOtpLoginDialog(
            onRequestOtp = { phone, callback ->
                viewModel.requestMemberOtp(phone, callback)
            },
            onVerifyOtp = { phone ->
                viewModel.verifyAndLoginMember(phone) {
                    showMemberOtpDialog = false
                }
            },
            onDismiss = { showMemberOtpDialog = false }
        )
    }

    if (showCreateMahalDialog) {
        CreateMahalDialog(
            onDismiss = { showCreateMahalDialog = false },
            onSave = { code, name, location, username, pass, phone, regNo, fee ->
                viewModel.createNewMahal(code, name, location, username, pass, phone, regNo, fee) {
                    showCreateMahalDialog = false
                }
            }
        )
    }

    if (showCommunityLogoDialog && activeMahal != null) {
        CommunityLogoDialog(
            activeMahal = activeMahal!!,
            onDismiss = { showCommunityLogoDialog = false },
            onSave = { uri, preset ->
                viewModel.updateCommunityLogo(uri, preset)
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
