package com.example.suppliersystemclient.ui.sendtoserverscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.suppliersystemclient.ui.SupplierViewModel


@Composable
fun SendToServerScreen(viewModel: SupplierViewModel) {
    val suppliers by viewModel.suppliers.collectAsState()
    var serverIp by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }
    val reservedDaysMap = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn {
                items(suppliers) { supplier ->
                    val reservedDays = reservedDaysMap.getOrPut(supplier.id) { supplier.reservedDays?.toMutableSet() ?: mutableSetOf() }
                    Column (
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = supplier.info)
                        LazyRow {
                            items(10){day ->
                                val isChecked = reservedDays.contains(day+1)
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        if (isChecked) {
                                            reservedDays.remove(day+1)
                                        } else {
                                            reservedDays.add(day+1)
                                        }
                                        reservedDaysMap[supplier.id] = reservedDays
                                    }
                                )
                                Text(text = (day+1).toString())
                            }
                        }
                    }
                }
            }
            TextField(value = serverIp, onValueChange = { serverIp = it }, label = { Text("Server IP") })
            TextField(value = serverPort, onValueChange = { serverPort = it }, label = { Text("Server Port") })
            Button(
                onClick = {
            }) {
                Text("Send to Server")
            }
        }

}