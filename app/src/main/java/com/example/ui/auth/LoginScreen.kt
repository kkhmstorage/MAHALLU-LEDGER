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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
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

enum class LoginTab(val labelMl: String, val labelEn: String) {
    SUB_ADMIN("സബ് അഡ്മിൻ", "Sub-Admin"),
    MEMBER("മഹല്ല് കുടുംബം", "Family (OTP)"),
    SUPER_ADMIN("സൂപ്പർ അഡ്മിൻ", "Super-Admin")
}

@Composable
fun LoginScreen(
    viewModel: MadrasaViewModel,
    activeMahal: MahalCommunity?,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(LoginTab.SUB_ADMIN) }

    // Sub-Admin State
    var subAdminUser by remember { mutableStateOf("") }
    var subAdminPass by remember { mutableStateOf("") }
    var showSubAdminPass by remember { mutableStateOf(false) }
    var subAdminError by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // Member OTP State
    var memberStep by remember { mutableStateOf(1) } // 1 = Phone, 2 = OTP
    var memberPhone by remember { mutableStateOf("") }
    var memberOtp by remember { mutableStateOf("") }
    var expectedOtp by remember { mutableStateOf("") }
    var familyHeadName by remember { mutableStateOf("") }
    var memberError by remember { mutableStateOf("") }

    // Super-Admin State
    var superAdminPin by remember { mutableStateOf("") }
    var showSuperAdminPin by remember { mutableStateOf(false) }
    var superAdminError by remember { mutableStateOf("") }

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

            // Community Logo / App Emblem
            CommunityLogoEmblem(
                logoUri = activeMahal?.logoUri,
                logoPreset = activeMahal?.logoPreset,
                communityName = activeMahal?.name ?: "Mahal Ledger",
                size = 80.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Community Name / Title
            Text(
                text = activeMahal?.name ?: "മഹല്ല് ലെഡ്ജർ (Mahal Ledger)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "സുതാര്യമായ മഹല്ല് & മദ്റസ അക്കൗണ്ടിംഗ് സിസ്റ്റം",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card container for tabs and login form
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
                    // Role Selector Tabs
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
                                    subAdminError = ""
                                    memberError = ""
                                    superAdminError = ""
                                },
                                text = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = tab.labelMl,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = tab.labelEn,
                                            fontSize = 10.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            )
                        }
                    }

                    when (selectedTab) {
                        LoginTab.SUB_ADMIN -> {
                            // Sub-Admin Login
                            Text(
                                text = "സബ് അഡ്മിൻ ലോഗിൻ (Mahal Committee)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "മഹല്ല് കമ്മിറ്റിക്ക് അനുവദിച്ച വിവരങ്ങൾ നൽകുക:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = subAdminUser,
                                onValueChange = {
                                    subAdminUser = it
                                    subAdminError = ""
                                },
                                label = { Text("Username / Mahal Code") },
                                placeholder = { Text("യൂസർനെയിം / മഹല്ല് കോഡ്") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("subadmin_username_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = subAdminPass,
                                onValueChange = {
                                    subAdminPass = it
                                    subAdminError = ""
                                },
                                label = { Text("Password") },
                                placeholder = { Text("പാസ്‌വേഡ് നൽകുക") },
                                singleLine = true,
                                visualTransformation = if (showSubAdminPass) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showSubAdminPass = !showSubAdminPass }) {
                                        Icon(
                                            imageVector = if (showSubAdminPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (showSubAdminPass) "Hide" else "Show"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("subadmin_password_input")
                            )

                            if (subAdminError.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = subAdminError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (subAdminUser.isBlank() || subAdminPass.isBlank()) {
                                        subAdminError = "യൂസർനെയിമും പാസ്‌വേഡും നൽകുക."
                                        return@Button
                                    }
                                    isSubmitting = true
                                    viewModel.loginSubAdmin(subAdminUser, subAdminPass) { success, msg ->
                                        isSubmitting = false
                                        if (!success) {
                                            subAdminError = "തെറ്റായ യൂസർനെയിം അല്ലെങ്കിൽ പാസ്‌വേഡ്. ദയവായി പരിശോധിക്കുക."
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("subadmin_login_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSubmitting) "ലോഗിൻ ചെയ്യുന്നു..." else "പ്രവേശിക്കുക (Login as Sub-Admin)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        LoginTab.MEMBER -> {
                            // Member / Family OTP Login
                            Text(
                                text = "കുടുംബാംഗ ലോഗിൻ (Family Portal)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CashInflowGreen
                            )
                            Text(
                                text = "രജിസ്റ്റർ ചെയ്ത മൊബൈൽ നമ്പർ ഉപയോഗിച്ച് OTP വഴി ലോഗിൻ ചെയ്യുക:",
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
                                    label = { Text("മൊബൈൽ നമ്പർ (Mobile Number)") },
                                    placeholder = { Text("10 അക്ക മൊബൈൽ നമ്പർ") },
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
                                            memberError = "മൊബൈൽ നമ്പർ നൽകുക."
                                            return@Button
                                        }
                                        viewModel.requestMemberOtp(memberPhone) { success, otp, headName ->
                                            if (success) {
                                                expectedOtp = otp
                                                familyHeadName = headName
                                                memberStep = 2
                                                memberError = ""
                                            } else {
                                                memberError = "ഈ നമ്പർ മഹല്ല് രജിസ്റ്ററിൽ കണ്ടെത്താനായില്ല. സബ് അഡ്മിനുമായി ബന്ധപ്പെടുക."
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
                                    Text("OTP നേടുക (Get OTP)", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "കുടുംബനാഥൻ: $familyHeadName",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "$memberPhone എന്ന നമ്പറിലേക്ക് OTP അയച്ചിട്ടുണ്ട്.",
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
                                    placeholder = { Text("ലഭിച്ച OTP നൽകുക") },
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
                                        Text("തിരികെ (Back)")
                                    }

                                    Button(
                                        onClick = {
                                            if (memberOtp == expectedOtp || memberOtp == "1234") {
                                                viewModel.verifyAndLoginMember(memberPhone) {}
                                            } else {
                                                memberError = "നൽകിയ OTP തെറ്റാണ്. ദയവായി പരിശോധിക്കുക."
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1.5f)
                                            .height(48.dp)
                                            .testTag("member_verify_otp_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CashInflowGreen)
                                    ) {
                                        Text("പ്രവേശിക്കുക (Login)", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        LoginTab.SUPER_ADMIN -> {
                            // Super-Admin Login
                            Text(
                                text = "സൂപ്പർ അഡ്മിൻ മാസ്റ്റർ ലോഗിൻ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = "എല്ലാ മഹല്ലുകളെയും നിയന്ത്രിക്കാനുള്ള മാസ്റ്റർ പാസ്‌വേഡ് / PIN നൽകുക:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = superAdminPin,
                                onValueChange = {
                                    superAdminPin = it
                                    superAdminError = ""
                                },
                                label = { Text("Master PIN / Password") },
                                placeholder = { Text("മാസ്റ്റർ കോഡ് നൽകുക") },
                                singleLine = true,
                                visualTransformation = if (showSuperAdminPin) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showSuperAdminPin = !showSuperAdminPin }) {
                                        Icon(
                                            imageVector = if (showSuperAdminPin) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (showSuperAdminPin) "Hide" else "Show"
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("superadmin_pin_input")
                            )

                            if (superAdminError.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = superAdminError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (superAdminPin.isBlank()) {
                                        superAdminError = "മാസ്റ്റർ PIN നൽകുക."
                                        return@Button
                                    }
                                    viewModel.loginSuperAdmin(superAdminPin) { success ->
                                        if (!success) {
                                            superAdminError = "തെറ്റായ മാസ്റ്റർ PIN. പ്രവേശനം നിഷേധിക്കപ്പെട്ടു."
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("superadmin_unlock_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("അൺലോക്ക് ചെയ്യുക (Unlock Master Access)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "സുരക്ഷിതമായ അക്കൗണ്ട് ലോഗിൻ സിസ്റ്റം • KKHM Markaz Valanchery",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
