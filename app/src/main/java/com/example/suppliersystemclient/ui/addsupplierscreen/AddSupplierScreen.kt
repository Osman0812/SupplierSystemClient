package com.example.suppliersystemclient.ui.addsupplierscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel
import com.example.suppliersystemclient.ui.component.CustomButton
import com.example.suppliersystemclient.ui.component.CustomTextField

@Composable
fun AddSupplierScreen(
    viewModel: SupplierViewModel,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Contract") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            value = name,
            onValueChange = { name = it },
            placeHolder = "Name Surname",
            leadingIcon = Icons.Outlined.Person
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = type == "Contract", onClick = { type = "Contract" })
            Text("Only contract")
            RadioButton(selected = type == "Stock", onClick = { type = "Stock" })
            Text("Only stock")
            RadioButton(selected = type == "Both", onClick = { type = "Both" })
            Text("Both")
        }
        Spacer(modifier = Modifier.height(12.dp))
        CustomButton(
            onClick = {
                viewModel.insertSupplier(Supplier(info = name, type = type, reservedDays = null))
            },
            text = "Add Supplier"
        )
    }
}