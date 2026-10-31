package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Household
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen
import com.example.ui.theme.StatusPendingAmber

@Composable
fun EditHouseholdDialog(
    household: Household,
    onDismiss: () -> Unit,
    onSave: (Household) -> Unit,
    onVerifyAndSave: ((Household) -> Unit)? = null
) {
    var houseNumber by remember { mutableStateOf(household.houseNumber) }
    var headOfFamily by remember { mutableStateOf(household.headOfFamily) }
    var familyName by remember { mutableStateOf(household.familyName) }
    var phoneNumber by remember { mutableStateOf(household.phoneNumber) }
    var monthlyFee by remember { mutableStateOf(household.monthlyFee.toInt().toString()) }
    var wardNumber by remember { mutableStateOf(household.wardNumber) }
    var memberCount by remember { mutableStateOf(household.memberCount.toString()) }
    var maleCount by remember { mutableStateOf(if (household.maleCount > 0) household.maleCount.toString() else "") }
    var femaleCount by remember { mutableStateOf(if (household.femaleCount > 0) household.femaleCount.toString() else "") }
    var childrenCount by remember { mutableStateOf(if (household.childrenCount > 0) household.childrenCount.toString() else "") }
    var rationCategory by remember { mutableStateOf(household.rationCategory) }
    var madrasaStudentCount by remember { mutableStateOf(if (household.madrasaStudentCount > 0) household.madrasaStudentCount.toString() else "") }
    var bloodGroups by remember { mutableStateOf(household.bloodGroups) }
    var address by remember { mutableStateOf(household.address) }
    var notes by remember { mutableStateOf(household.notes) }
    var verificationStatus by remember { mutableStateOf(household.verificationStatus) }

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
                            text = "Edit Family Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sub-Admin Authority Control",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (verificationStatus == "VERIFIED") CashInflowContainer else Color(0xFFFFF3E0)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (verificationStatus == "VERIFIED") Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (verificationStatus == "VERIFIED") CashInflowGreen else StatusPendingAmber,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = if (verificationStatus == "VERIFIED") "Verified" else "Pending Review",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (verificationStatus == "VERIFIED") CashInflowGreen else StatusPendingAmber
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Basic Identification
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = houseNumber,
                        onValueChange = { houseNumber = it },
                        label = { Text("House / Reg #*") },
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
                    value = headOfFamily,
                    onValueChange = { headOfFamily = it },
                    label = { Text("Head of Family Name*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = familyName,
                    onValueChange = { familyName = it },
                    label = { Text("Family / House Name") },
                    placeholder = { Text("e.g. Baitul Aman / Melethil") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.weight(1.2f)
                    )

                    OutlinedTextField(
                        value = monthlyFee,
                        onValueChange = { monthlyFee = it },
                        label = { Text("Monthly Vari (₹)*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Family Demographics & Profile Details",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = memberCount,
                        onValueChange = { memberCount = it },
                        label = { Text("Total Members") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = maleCount,
                        onValueChange = { maleCount = it },
                        label = { Text("Males") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = femaleCount,
                        onValueChange = { femaleCount = it },
                        label = { Text("Females") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = childrenCount,
                        onValueChange = { childrenCount = it },
                        label = { Text("Children (<15)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = madrasaStudentCount,
                        onValueChange = { madrasaStudentCount = it },
                        label = { Text("Madrasa Students") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ration Category Chips
                Text(
                    text = "Ration Card Category:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("BPL", "APL", "AAY", "Non-Priority").forEach { cat ->
                        FilterChip(
                            selected = rationCategory == cat,
                            onClick = { rationCategory = if (rationCategory == cat) "" else cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bloodGroups,
                    onValueChange = { bloodGroups = it },
                    label = { Text("Blood Groups in Family") },
                    placeholder = { Text("e.g. O+ve, B+ve") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Detailed Address / Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Special Notes / Remarks") },
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
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val fee = monthlyFee.toDoubleOrNull() ?: 200.0
                            val totalCount = memberCount.toIntOrNull() ?: 4
                            val mCount = maleCount.toIntOrNull() ?: 0
                            val fCount = femaleCount.toIntOrNull() ?: 0
                            val cCount = childrenCount.toIntOrNull() ?: 0
                            val mStudents = madrasaStudentCount.toIntOrNull() ?: 0

                            val updated = household.copy(
                                houseNumber = houseNumber.trim(),
                                headOfFamily = headOfFamily.trim(),
                                familyName = familyName.trim(),
                                phoneNumber = phoneNumber.trim(),
                                monthlyFee = fee,
                                wardNumber = wardNumber.trim(),
                                memberCount = totalCount,
                                maleCount = mCount,
                                femaleCount = fCount,
                                childrenCount = cCount,
                                rationCategory = rationCategory.trim(),
                                madrasaStudentCount = mStudents,
                                bloodGroups = bloodGroups.trim(),
                                address = address.trim(),
                                notes = notes.trim()
                            )
                            onSave(updated)
                        },
                        enabled = houseNumber.isNotBlank() && headOfFamily.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_household_button")
                    ) {
                        Text("Save Details")
                    }
                }

                // If pending verification, show direct "Confirm & Verify" button for Sub-Admin
                if (verificationStatus != "VERIFIED" && onVerifyAndSave != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val fee = monthlyFee.toDoubleOrNull() ?: 200.0
                            val totalCount = memberCount.toIntOrNull() ?: 4
                            val mCount = maleCount.toIntOrNull() ?: 0
                            val fCount = femaleCount.toIntOrNull() ?: 0
                            val cCount = childrenCount.toIntOrNull() ?: 0
                            val mStudents = madrasaStudentCount.toIntOrNull() ?: 0

                            val verifiedHousehold = household.copy(
                                houseNumber = houseNumber.trim(),
                                headOfFamily = headOfFamily.trim(),
                                familyName = familyName.trim(),
                                phoneNumber = phoneNumber.trim(),
                                monthlyFee = fee,
                                wardNumber = wardNumber.trim(),
                                memberCount = totalCount,
                                maleCount = mCount,
                                femaleCount = fCount,
                                childrenCount = cCount,
                                rationCategory = rationCategory.trim(),
                                madrasaStudentCount = mStudents,
                                bloodGroups = bloodGroups.trim(),
                                address = address.trim(),
                                notes = notes.trim(),
                                verificationStatus = "VERIFIED",
                                verifiedDate = System.currentTimeMillis()
                            )
                            onVerifyAndSave(verifiedHousehold)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CashInflowGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verify_and_confirm_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Confirm & Verify Family Data")
                    }
                }
            }
        }
    }
}
