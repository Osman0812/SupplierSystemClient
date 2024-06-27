package com.example.suppliersystemclient.ui.sendtoserverscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.suppliersystemclient.ui.SupplierViewModel

@Composable
fun SendToServerScreen(viewModel: SupplierViewModel) {
    val suppliers by viewModel.suppliers.collectAsState()
    var serverIp by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }

    Column {
        suppliers.forEach { supplier ->
            Text(text = supplier.info)
            // Checkbox for selecting reserved days
        }
        TextField(value = serverIp, onValueChange = { serverIp = it }, label = { Text("Server IP") })
        TextField(value = serverPort, onValueChange = { serverPort = it }, label = { Text("Server Port") })
        Button(
            onClick = {
            // To Server
        }) {
            Text("Send to Server")
        }
    }
}