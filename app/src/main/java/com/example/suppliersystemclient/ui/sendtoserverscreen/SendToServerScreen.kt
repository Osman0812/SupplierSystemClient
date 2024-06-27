package com.example.suppliersystemclient.ui.sendtoserverscreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel
import com.example.suppliersystemclient.ui.component.CustomButton
import com.example.suppliersystemclient.ui.component.CustomTextField
import com.example.suppliersystemclient.ui.editsupplierscreen.SupplierItem


@Composable
fun SendToServerScreen(viewModel: SupplierViewModel) {
    val suppliers by viewModel.suppliers.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val context = LocalContext.current

    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var serverIp by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }
    val reservedDaysMap = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
    var isSentClicked by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = assignments) {
        Log.d("response",assignments.toString())
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
            modifier = Modifier.fillMaxHeight(0.4f)
        ) {
            items(suppliers) { supplier ->
                SupplierItem(
                    supplier = supplier,
                    onEdit = {
                        selectedSupplier = supplier
                    },
                    onDelete = {
                     
                    }
                )
            }
        }
        selectedSupplier?.let { supplier ->
            Text("Reserved Days for ${supplier.info}")
            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(10){day->
                    val isChecked = reservedDaysMap[supplier.id]?.contains(day+1) == true
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            val reservedDays = reservedDaysMap.getOrPut(supplier.id) { mutableSetOf() }
                            if (isChecked) {
                                reservedDays.remove(day+1)
                            } else {
                                reservedDays.add(day+1)
                            }
                            reservedDaysMap[supplier.id] = reservedDays.toMutableSet()
                        }
                    )
                    Text((day+1).toString())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val reservedDays = reservedDaysMap[supplier.id]?.toList() ?: emptyList()
                viewModel.updateReservedDays(supplier.id, reservedDays)
            }) {
                Text("Save Reserved Days")
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
                isSentClicked = true

            },
            text = "Send"
        )
        LaunchedEffect(key1 = isSentClicked) {
            if (isSentClicked){
                viewModel.sendSuppliersToServer(
                    suppliers.map {
                        it.copy(
                            reservedDays = reservedDaysMap[it.id]?.toList() ?: emptyList()
                        )
                    },
                    serverIp,
                    serverPort.toInt()
                )
                isSentClicked = false
            }
        }
    }
}