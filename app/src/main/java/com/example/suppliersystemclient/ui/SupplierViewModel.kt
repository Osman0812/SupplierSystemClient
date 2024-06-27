package com.example.suppliersystemclient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.data.SupplierAssignment
import com.example.suppliersystemclient.room.SupplierDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierDao: SupplierDao
) : ViewModel() {

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers.asStateFlow()

    private val _assignments = MutableStateFlow<List<SupplierAssignment>>(emptyList())
    val assignments: StateFlow<List<SupplierAssignment>> = _assignments.asStateFlow()

    init {
        viewModelScope.launch {
            supplierDao.getAllSuppliers()
                .collect { supplierList ->
                    _suppliers.value = supplierList
                }
        }
    }

    fun insertSupplier(supplier: Supplier) {
        viewModelScope.launch {
            supplierDao.insertSupplier(supplier)
        }
    }

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch {
            supplierDao.updateSupplier(supplier)
        }
    }

    fun deleteSupplier(supplier: Supplier) {
        viewModelScope.launch {
            supplierDao.deleteSupplier(supplier)
        }
    }

    fun fetchAssignments(month: Int) {
        viewModelScope.launch {
            supplierDao.getAssignmentsForMonth(month)
                .collect { assignmentList ->
                    _assignments.value = assignmentList
                }
        }
    }

    suspend fun sendSuppliersToServer(suppliers: List<Supplier>, serverIp: String, serverPort: Int) {
        // Implementation of sending data to the server and handling the response
    }
}