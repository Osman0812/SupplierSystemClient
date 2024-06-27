package com.example.suppliersystemclient.ui.editsupplierscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel
import com.example.suppliersystemclient.ui.component.CustomButton
import com.example.suppliersystemclient.ui.component.CustomTextField

@Composable
fun EditSupplierScreen(viewModel: SupplierViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val suppliers by viewModel.suppliers.collectAsState()
    var filteredSuppliers by remember { mutableStateOf(listOf<Supplier>()) }
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var isEditing by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeHolder = "Search",
            leadingIcon = Icons.Default.Search
        )
        CustomButton(
            onClick = {
                //Arama
                filteredSuppliers = suppliers.filter { it.info.contains(searchQuery, true) }
            },
            text = "Search"
        )
        LazyColumn {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "supplierInfo",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "supplierType",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "lastReservedDays",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(thickness = 2.dp)
            }
            items(filteredSuppliers) { supplier ->
                SupplierItem(
                    supplier = supplier,
                    onEdit = {
                        viewModel.updateSupplier(it)
                        selectedSupplier = supplier
                        isEditing = true
                    },
                    onDelete = {
                        viewModel.deleteSupplier(it)
                    }
                )
            }
        }
        if (isEditing && selectedSupplier != null) {
            EditSupplierDialog(
                supplier = selectedSupplier!!,
                viewModel = viewModel,
                onDismiss = {
                    isEditing = false
                    selectedSupplier = null
                }
            )
        }
    }
}

@Composable
fun SupplierItem(
    supplier: Supplier,
    onEdit: (Supplier) -> Unit,
    onDelete: (Supplier) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = supplier.info)

            Text(text = supplier.type)
            Text(text = supplier.reservedDays?.joinToString(", ") ?: "")
        }
        Row {
            IconButton(onClick = { onEdit(supplier) }) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = { onDelete(supplier) }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun EditSupplierDialog(
    supplier: Supplier,
    viewModel: SupplierViewModel,
    onDismiss: () -> Unit
) {
    var type by remember { mutableStateOf(supplier.type) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Edit Supplier", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Name: ${supplier.info}")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Edit Supplier Type:")
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = type == "Contract", onClick = { type = "Contract" })
                        Text("Only contract", modifier = Modifier.padding(start = 8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = type == "Stock", onClick = { type = "Stock" })
                        Text("Only stock", modifier = Modifier.padding(start = 8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = type == "Both", onClick = { type = "Both" })
                        Text("Both", modifier = Modifier.padding(start = 8.dp))
                    }

                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomButton(
                        onClick = {
                            viewModel.updateSupplier(supplier.copy(type = type))
                            onDismiss()
                        },
                        text = "Submit"
                    )
                    CustomButton(
                        onClick = onDismiss,
                        text = "Cancel"
                    )
                }
            }
        }
    }
}

