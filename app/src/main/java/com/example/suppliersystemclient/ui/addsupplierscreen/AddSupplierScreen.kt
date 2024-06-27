package com.example.suppliersystemclient.ui.addsupplierscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel

@Composable
fun AddSupplierScreen(viewModel: SupplierViewModel) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(1) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name and Surname") })
            Row {
                RadioButton(selected = type == 1, onClick = { type = 1 })
                Text("Only contract")
                RadioButton(selected = type == 2, onClick = { type = 2 })
                Text("Only stock")
                RadioButton(selected = type == 3, onClick = { type = 3 })
                Text("Both")
            }
            Button(onClick = {
                viewModel.insertSupplier(Supplier(info = name, type = type, reservedDays = null))
            }) {
                Text("Add Supplier")
            }
        }
    }
}