package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MadrasaViewModel
import com.example.ui.UserRole
import com.example.ui.components.DigitalReceiptDialog
import com.example.ui.components.MemberOtpLoginDialog
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.components.SubAdminLoginDialog
import com.example.ui.components.SuperAdminLoginDialog
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.ledger.LedgerScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.subscriptions.SubscriptionsScreen
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.MahalLedgerTheme
import com.example.ui.transparency.TransparencyScreen
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.IconButton
import com.example.ui.auth.AnimatedSplashScreen
import com.example.ui.auth.CommunityLogoEmblem
import com.example.ui.auth.LoginScreen
import kotlinx.coroutines.flow.collectLatest

enum class MainTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD("Overview", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_dashboard"),
    SUBSCRIPTIONS("Vari (വരി)", Icons.Filled.Payments, Icons.Outlined.Payments, "tab_subscriptions"),
    LEDGER("Ledger", Icons.Filled.AccountBalance, Icons.Outlined.AccountBalance, "tab_ledger"),
    TRANSPARENCY("Audit", Icons.Filled.VerifiedUser, Icons.Outlined.VerifiedUser, "tab_transparency"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "tab_settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahalLedgerTheme {
                MahalLedgerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahalLedgerApp(
    viewModel: MadrasaViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainTab.DASHBOARD) }
    var showRoleSwitcherDialog by remember { mutableStateOf(false) }
    var showSubAdminLoginDialog by remember { mutableStateOf(false) }
    var showSuperAdminLoginDialog by remember { mutableStateOf(false) }
    var showMemberOtpDialog by remember { mutableStateOf(false) }

    val role by viewModel.currentRole.collectAsState()
    val activeMahal by viewModel.activeMahal.collectAsState()
    val activeMember by viewModel.activeMemberHousehold.collectAsState()
    val activeReceipt by viewModel.activeReceipt.collectAsState()
    val isSplashCompleted by viewModel.isSplashCompleted.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (!isSplashCompleted) {
        AnimatedSplashScreen(
            activeMahal = activeMahal,
            onSplashComplete = { viewModel.completeSplash() }
        )
        return
    }

    if (!isAuthenticated) {
        LoginScreen(
            viewModel = viewModel,
            activeMahal = activeMahal
        )
        return
    }

    BackHandler(enabled = selectedTab != MainTab.DASHBOARD) {
        selectedTab = MainTab.DASHBOARD
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CommunityLogoEmblem(
                            logoUri = activeMahal?.logoUri,
                            logoPreset = activeMahal?.logoPreset,
                            communityName = activeMahal?.name ?: "Mahal Ledger",
                            size = 36.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = activeMahal?.name ?: "Mahal Ledger",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = when (role) {
                                    UserRole.SUPER_ADMIN -> "Super-Admin Control • Master Access"
                                    UserRole.SUB_ADMIN -> "Sub-Admin: ${activeMahal?.subAdminUsername ?: "Treasurer"}"
                                    UserRole.MEMBER -> "Member: ${activeMember?.headOfFamily ?: "Family"} (${activeMember?.houseNumber ?: ""})"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    val syncState by viewModel.syncState.collectAsState()

                    // Cloud Sync Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { viewModel.triggerSync() }
                            .testTag("cloud_sync_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (syncState == com.example.util.SyncState.SYNCING) Icons.Default.Sync else Icons.Default.CloudDone,
                                contentDescription = "Sync",
                                tint = if (syncState == com.example.util.SyncState.SYNCING) MaterialTheme.colorScheme.primary else CashInflowGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (syncState == com.example.util.SyncState.SYNCING) "Syncing..." else "Backed Up",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Role Switcher Chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when (role) {
                            UserRole.SUPER_ADMIN -> MaterialTheme.colorScheme.tertiaryContainer
                            UserRole.SUB_ADMIN -> MaterialTheme.colorScheme.primaryContainer
                            UserRole.MEMBER -> CashInflowContainer
                        },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { showRoleSwitcherDialog = true }
                            .testTag("role_switcher_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
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
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (role) {
                                    UserRole.SUPER_ADMIN -> "Super-Admin"
                                    UserRole.SUB_ADMIN -> "Sub-Admin"
                                    UserRole.MEMBER -> "Member"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (role) {
                                    UserRole.SUPER_ADMIN -> MaterialTheme.colorScheme.onTertiaryContainer
                                    UserRole.SUB_ADMIN -> MaterialTheme.colorScheme.onPrimaryContainer
                                    UserRole.MEMBER -> CashInflowGreen
                                }
                            )
                        }
                    }

                    // Logout Action Button
                    IconButton(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.testTag("app_bar_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToSubscriptions = { selectedTab = MainTab.SUBSCRIPTIONS },
                    onNavigateToLedger = { selectedTab = MainTab.LEDGER },
                    onNavigateToTransparency = { selectedTab = MainTab.TRANSPARENCY }
                )
                MainTab.SUBSCRIPTIONS -> SubscriptionsScreen(
                    viewModel = viewModel
                )
                MainTab.LEDGER -> LedgerScreen(
                    viewModel = viewModel
                )
                MainTab.TRANSPARENCY -> TransparencyScreen(
                    viewModel = viewModel
                )
                MainTab.SETTINGS -> SettingsScreen(
                    viewModel = viewModel
                )
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

    // Digital Receipt Overlay Dialog
    activeReceipt?.let { receipt ->
        DigitalReceiptDialog(
            receipt = receipt,
            onDismiss = { viewModel.dismissReceipt() }
        )
    }
}
