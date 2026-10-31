package com.example.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.MahalCommunity

@Composable
fun BankAndUpiSettingsDialog(
    activeMahal: MahalCommunity,
    onDismiss: () -> Unit,
    onSave: (upiId: String, payeeName: String, bankName: String, accountNo: String, ifsc: String, qrUri: String) -> Unit
) {
    var upiId by remember { mutableStateOf(activeMahal.upiId) }
    var payeeName by remember { mutableStateOf(activeMahal.payeeName) }
    var bankName by remember { mutableStateOf(activeMahal.bankName) }
    var accountNumber by remember { mutableStateOf(activeMahal.accountNumber) }
    var ifscCode by remember { mutableStateOf(activeMahal.ifscCode) }
    var qrCodeUri by remember { mutableStateOf(activeMahal.qrCodeUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                qrCodeUri = uri.toString()
            }
        }
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ബാങ്ക് & UPI ക്രമീകരണങ്ങൾ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "കുടുംബങ്ങൾക്ക് വരിസംഖ്യ UPI / ബാങ്ക് വഴി അടയ്ക്കാനുള്ള വിവരങ്ങൾ നൽകുക:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("Mahal UPI ID (e.g. name@upi / phone@sbi)") },
                    placeholder = { Text("alnoor.mahal@sbi") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upi_id_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = payeeName,
                    onValueChange = { payeeName = it },
                    label = { Text("Payee Name / കമ്മറ്റി പേര്") },
                    placeholder = { Text("Al-Noor Mahal Committee") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bankName,
                    onValueChange = { bankName = it },
                    label = { Text("Bank Name (ബാങ്ക് പേര്)") },
                    placeholder = { Text("State Bank of India / Kerala Bank") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account Number (അക്കൗണ്ട് നമ്പർ)") },
                    placeholder = { Text("67329104821") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ifscCode,
                    onValueChange = { ifscCode = it.uppercase() },
                    label = { Text("IFSC Code") },
                    placeholder = { Text("SBIN0008421") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "QR Code സ്കാനർ ഇമേജ്:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (qrCodeUri.isNotBlank()) {
                    AsyncImage(
                        model = Uri.parse(qrCodeUri),
                        contentDescription = "QR Code Preview",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(140.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = { qrCodeUri = "" },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("സ്കാനർ ഒഴിവാക്കുക (Remove QR)", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (qrCodeUri.isNotBlank()) "മറ്റൊരു QR സ്കാനർ തിരഞ്ഞെടുക്കുക" else "QR സ്കാനർ ചിത്രം അപ്‌ലോഡ് ചെയ്യുക")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("റദ്ദാക്കുക")
                    }

                    Button(
                        onClick = {
                            onSave(upiId, payeeName, bankName, accountNumber, ifscCode, qrCodeUri)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("save_bank_upi_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("സേവ് ചെയ്യുക", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
