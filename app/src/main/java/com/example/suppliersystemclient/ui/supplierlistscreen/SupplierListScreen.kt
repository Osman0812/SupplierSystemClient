package com.example.suppliersystemclient.ui.supplierlistscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.suppliersystemclient.data.model.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel

@Composable
fun SupplierListScreen(viewModel: SupplierViewModel) {
    val suppliers by viewModel.suppliers.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ){
        suppliers.forEach { supplier ->
            SupplierItem(
                supplier = supplier,
                onDelete = {
                    viewModel.deleteSupplier(it)
                }
            )
        }
    }
}

@Composable
fun SupplierItem(
    supplier: Supplier,
    onDelete: (Supplier) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(text = supplier.info)
            Text(text = supplier.type)
            Text(text = supplier.reservedDays?.joinToString(", ") ?: "")
        }
        Row {
            IconButton(onClick = { onDelete(supplier) }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

