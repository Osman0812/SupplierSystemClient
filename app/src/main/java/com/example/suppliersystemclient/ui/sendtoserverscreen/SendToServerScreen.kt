package com.example.suppliersystemclient.ui.sendtoserverscreen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.suppliersystemclient.data.model.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel
import com.example.suppliersystemclient.component.CustomButton
import com.example.suppliersystemclient.component.CustomTextField
import com.example.suppliersystemclient.ui.editsupplierscreen.SupplierItem
import com.skydoves.flexible.core.screenHeight
import java.time.LocalDate
import java.util.Locale


@Composable
fun SendToServerScreen(viewModel: SupplierViewModel) {
    val suppliers by viewModel.suppliers.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val context = LocalContext.current

    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var serverIp by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }
    val reservedDaysMap = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
    var isSentClicked by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val checkBoxStates = remember { mutableStateMapOf<Int, MutableState<Boolean>>() }
    LaunchedEffect(key1 = assignments) {
        Log.d("response", assignments.toString())
        viewModel.insertAssignmentsToDb(assignments)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Select a supplier to define reserved days")
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            items(suppliers) { supplier ->
                SupplierItem(
                    supplier = supplier,
                    onEdit = {
                        selectedSupplier = supplier
                        isDialogOpen = true
                    },
                    onDelete = {

                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = serverIp,
            onValueChange = { serverIp = it },
            placeHolder = "Server IP",
            leadingIcon = Icons.Default.Info
        )
        CustomTextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            placeHolder = "Server Port",
            leadingIcon = Icons.Default.MailOutline
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(
            onClick = {
                if (serverIp.isNotEmpty() && serverPort.isNotEmpty()) {
                    isSentClicked = true
                    suppliers.map {
                        Log.d("reserved deneme", it.reservedDays.toString())
                    }
                    viewModel.sendSuppliersToServer(
                        suppliers = suppliers.map {
                            it.copy(
                                reservedDays = it.reservedDays ?: emptyList()
                            )
                        },
                        serverIp = serverIp,
                        serverPort = serverPort.toInt()
                    )
                    isSentClicked = false
                } else {
                    Toast.makeText(context, "Please enter ip and port!", Toast.LENGTH_LONG).show()
                }
            },
            text = "Send"
        )
    }

    if (isDialogOpen && selectedSupplier != null) {
        Dialog(onDismissRequest = {
            isDialogOpen = false
            checkBoxStates.clear()
        }) {
            Surface(
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .height(screenHeight() * 0.5f)
                        .padding(top = 5.dp, bottom = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    selectedSupplier?.let { supplier ->
                        Text("Reserved Days for ${supplier.info}")
                        LazyColumn(
                            modifier = Modifier
                                .weight(0.7f)
                                .width(screenHeight() * 0.25f),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            items(30) { day ->
                                val isChecked = checkBoxStates.getOrPut(day + 1) {
                                    mutableStateOf(
                                        supplier.reservedDays?.contains(day + 1) ?: false
                                    )
                                }
                                val date = formattedDate(day)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked.value,
                                        onCheckedChange = { checked ->
                                            isChecked.value = checked
                                            val reservedDays =
                                                reservedDaysMap.getOrPut(supplier.id) { mutableSetOf() }
                                            if (checked) {
                                                reservedDays.add(day + 1)
                                            } else {
                                                reservedDays.remove(day + 1)
                                            }
                                            reservedDaysMap[supplier.id] = reservedDays.toMutableSet()
                                        }
                                    )
                                    Text(text = date)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                val reservedDays =
                                    reservedDaysMap[supplier.id]?.toList() ?: emptyList()
                                viewModel.updateReservedDays(supplier.id, reservedDays)
                                isDialogOpen = false
                                checkBoxStates.clear()
                            }
                        ) {
                            Text("Save Reserved Days")
                        }
                    }
                }
            }
        }
    }
}


fun formattedDate(day: Int): String {
    val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDate.now()
        "${day + 1} ${
            currentDate.month.name.lowercase(Locale.ROOT)
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }
        } ${currentDate.year}"
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return formattedDate
}