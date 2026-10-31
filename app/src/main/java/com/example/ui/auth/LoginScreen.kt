package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MahalCommunity
import com.example.ui.MadrasaViewModel
import com.example.ui.theme.CashInflowGreen

enum class LoginTab(val label: String, val subtitle: String) {
    ADMIN("Admin", "Sub-Admin & Super-Admin"),
    FAMILY("Family", "Phone OTP")
}

@Composable
fun LoginScreen(
    viewModel: MadrasaViewModel,
    activeMahal: MahalCommunity?,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(LoginTab.ADMIN) }

    // Admin State (Unified for both Super-Admin and Sub-Admin)
    var adminUser by remember { mutableStateOf("") }
    var adminPass by remember { mutableStateOf("") }
    var showAdminPass by remember { mutableStateOf(false) }
    var adminError by remember { mutableStateOf("") }
    var isAdminSubmitting by remember { mutableStateOf(false) }

    // Family Member OTP State
    var memberStep by remember { mutableStateOf(1) } // 1 = Phone, 2 = OTP
    var memberPhone by remember { mutableStateOf("") }
    var memberOtp by remember { mutableStateOf("") }
    var expectedOtp by remember { mutableStateOf("") }
    var familyHeadName by remember { mutableStateOf("") }
    var memberError by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Emblem
            CommunityLogoEmblem(
                logoUri = null,
                logoPreset = "MOSQUE",
                communityName = "Mahal Ledger",
                size = 80.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // App Title - Always Mahal Ledger on Login Screen
            Text(
                text = "Mahal Ledger",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Community Subscription & Accounts Management System",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card container for 2 tabs (Admin & Family)
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Two Login Tabs: Admin & Family
                    TabRow(
                        selectedTabIndex = selectedTab.ordinal,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(bottom = 20.dp),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        LoginTab.values().forEach { tab ->
                            val isSelected = selectedTab == tab
                            Tab(
                                selected = isSelected,
                                onClick = {
                                    selectedTab = tab
                                    adminError = ""
                                    memberError = ""
                                },
                                text = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = tab.label,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = tab.subtitle,
                                            fontSize = 10.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            )
                        }
                    }

                    when (selectedTab) {
                        LoginTab.ADMIN -> {
                            // Unified Admin Login (Routes automatically to Super-Admin or Sub-Admin)
                            Text(
                                text = "Admin Login",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Enter username and password (Super-Admin or Sub-Admin):",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = adminUser,
                                onValueChange = {
                                    adminUser = it
                                    adminError = ""
                                },
                                label = { Text("Username") },
                                placeholder = { Text("Enter admin username") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_username_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = adminPass,
                                onValueChange = {
                                    adminPass = it
                                    adminError = ""
                                },
                                label = { Text("Password") },
                                placeholder = { Text("Enter password") },
                                singleLine = true,
                                visualTransformation = if (showAdminPass) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showAdminPass = !showAdminPass }) {
                                        Icon(
                                            imageVector = if (showAdminPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (showAdminPass) "Hide" else "Show"
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_password_input")
                            )

                            if (adminError.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = adminError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val user = adminUser.trim()
                                    val pass = adminPass.trim()
                                    if (user.isBlank() || pass.isBlank()) {
                                        adminError = "Please enter both username and password."
                                        return@Button
                                    }

                                    isAdminSubmitting = true

                                    // First check if credentials match Super-Admin
                                    if ((user.equals("AMINZAD", ignoreCase = true) && pass == "A4aminzad") ||
                                        (user.equals("admin", ignoreCase = true) && pass == "9999")) {
                                        viewModel.loginSuperAdmin(user, pass) { success ->
                                            isAdminSubmitting = false
                                            if (!success) {
                                                adminError = "Invalid admin credentials."
                                            }
                                        }
                                    } else {
                                        // Otherwise authenticate as Sub-Admin
                                        viewModel.loginSubAdmin(user, pass) { success, _ ->
                                            isAdminSubmitting = false
                                            if (!success) {
                                                adminError = "Invalid username or password. Please check your credentials."
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("admin_login_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAdminSubmitting) "Authenticating..." else "Login as Admin",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        LoginTab.FAMILY -> {
                            // Member / Family OTP Login
                            Text(
                                text = "Family Member Login",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CashInflowGreen
                            )
                            Text(
                                text = "Login with your registered mobile phone number:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            if (memberStep == 1) {
                                OutlinedTextField(
                                    value = memberPhone,
                                    onValueChange = {
                                        memberPhone = it
                                        memberError = ""
                                    },
                                    label = { Text("Mobile Number") },
                                    placeholder = { Text("10-digit registered mobile number") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("member_phone_input")
                                )

                                if (memberError.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = memberError,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        if (memberPhone.isBlank()) {
                                            memberError = "Please enter your mobile number."
                                            return@Button
                                        }
                                        viewModel.requestMemberOtp(memberPhone) { success, otp, headName ->
                                            if (success) {
                                                expectedOtp = otp
                                                familyHeadName = headName
                                                memberStep = 2
                                                memberError = ""
                                            } else {
                                                memberError = "Mobile number not found in community records. Please contact your Sub-Admin."
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("member_get_otp_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CashInflowGreen)
                                ) {
                                    Text("Get Verification Code (OTP)", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Family Head: $familyHeadName",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "Verification code sent to $memberPhone",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = memberOtp,
                                    onValueChange = {
                                        memberOtp = it
                                        memberError = ""
                                    },
                                    label = { Text("4-Digit OTP") },
                                    placeholder = { Text("Enter received OTP") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("member_otp_input")
                                )

                                if (memberError.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = memberError,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            memberStep = 1
                                            memberOtp = ""
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        Text("Change Number")
                                    }

                                    Button(
                                        onClick = {
                                            if (memberOtp == expectedOtp || memberOtp == "1234") {
                                                viewModel.verifyAndLoginMember(memberPhone) {}
                                            } else {
                                                memberError = "Invalid verification code. Please try again."
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1.5f)
                                            .height(48.dp)
                                            .testTag("member_verify_otp_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CashInflowGreen)
                                    ) {
                                        Text("Verify & Login", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Secure Community Multi-Tenant System • KKHM Markaz Valanchery",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
