package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Household
import com.example.ui.theme.CashInflowContainer
import com.example.ui.theme.CashInflowGreen

@Composable
fun MemberProfileCompletionDialog(
    household: Household,
    onDismiss: () -> Unit,
    onSubmit: (Household) -> Unit
) {
    var familyName by remember { mutableStateOf(household.familyName) }
    var memberCount by remember { mutableStateOf(if (household.memberCount > 0) household.memberCount.toString() else "") }
    var maleCount by remember { mutableStateOf(if (household.maleCount > 0) household.maleCount.toString() else "") }
    var femaleCount by remember { mutableStateOf(if (household.femaleCount > 0) household.femaleCount.toString() else "") }
    var childrenCount by remember { mutableStateOf(if (household.childrenCount > 0) household.childrenCount.toString() else "") }
    var rationCategory by remember { mutableStateOf(household.rationCategory) }
    var madrasaStudentCount by remember { mutableStateOf(if (household.madrasaStudentCount > 0) household.madrasaStudentCount.toString() else "") }
    var bloodGroups by remember { mutableStateOf(household.bloodGroups) }
    var address by remember { mutableStateOf(household.address) }
    var notes by remember { mutableStateOf(household.notes) }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Complete Family Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Profile details for Mahal records (Optional)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = household.houseNumber,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Helpful notice banner: Non-mandatory
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 2.dp, end = 8.dp)
                        )
                        Text(
                            text = "This information is used strictly for organizing Mahal records. Fill only the details you wish to share (optional). Submitted details will be verified and approved by the Sub-Admin.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Read-only registered head of family
                Text(
                    text = "Head of Family: ${household.headOfFamily} (${household.wardNumber})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Family Name / House Name
                OutlinedTextField(
                    value = familyName,
                    onValueChange = { familyName = it },
                    label = { Text("Family Name / House Name") },
                    placeholder = { Text("e.g. Baitul Noor / Green Villa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Member Counts
                Text(
                    text = "Family Members Breakdown:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = memberCount,
                        onValueChange = { memberCount = it },
                        label = { Text("Total") },
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

                // Ration Card Category
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

                // Blood Groups
                OutlinedTextField(
                    value = bloodGroups,
                    onValueChange = { bloodGroups = it },
                    label = { Text("Blood Groups in Family") },
                    placeholder = { Text("e.g. O+ve, B+ve, AB+ve") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed address / landmark
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes / needs
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Additional Remarks / Requests") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Later / Skip")
                    }

                    Button(
                        onClick = {
                            val total = memberCount.toIntOrNull() ?: household.memberCount
                            val m = maleCount.toIntOrNull() ?: 0
                            val f = femaleCount.toIntOrNull() ?: 0
                            val c = childrenCount.toIntOrNull() ?: 0
                            val mStudents = madrasaStudentCount.toIntOrNull() ?: 0

                            val updatedHousehold = household.copy(
                                familyName = familyName.trim(),
                                memberCount = total,
                                maleCount = m,
                                femaleCount = f,
                                childrenCount = c,
                                rationCategory = rationCategory.trim(),
                                madrasaStudentCount = mStudents,
                                bloodGroups = bloodGroups.trim(),
                                address = address.trim(),
                                notes = notes.trim(),
                                verificationStatus = "PENDING_VERIFICATION"
                            )
                            onSubmit(updatedHousehold)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("submit_family_profile_button")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Submit Profile")
                    }
                }
            }
        }
    }
}
