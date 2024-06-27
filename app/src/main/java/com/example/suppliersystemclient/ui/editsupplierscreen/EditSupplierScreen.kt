package com.example.suppliersystemclient.ui.editsupplierscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.ui.SupplierViewModel

@Composable
fun EditSupplierScreen(viewModel: SupplierViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val suppliers by viewModel.suppliers.collectAsState()

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") }
        )
        Button(onClick = {
            //Arama
        }) {
            Text("Search")
        }
        suppliers.filter { it.info.contains(searchQuery, true) }.forEach { supplier ->
            SupplierItem(
                supplier = supplier,
                onEdit = {
                    viewModel.updateSupplier(it)
                },
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
    onEdit: (Supplier) -> Unit,
    onDelete: (Supplier) -> Unit)
{
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(text = supplier.info)
            Text(text = supplier.type.toString())
            Text(text = supplier.reservedDays ?: "")
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

