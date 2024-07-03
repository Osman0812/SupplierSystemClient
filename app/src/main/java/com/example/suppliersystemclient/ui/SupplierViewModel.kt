package com.example.suppliersystemclient.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.suppliersystemclient.data.model.Supplier
import com.example.suppliersystemclient.data.model.SupplierAssignment
import com.example.suppliersystemclient.room.SupplierDao
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierDao: SupplierDao,
    private val application: Application,
) : AndroidViewModel(application) {

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers.asStateFlow()

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

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

    fun updateReservedDays(supplierId: Int, reservedDays: List<Int>) {
        viewModelScope.launch {
            val supplier = _suppliers.value.find { it.id == supplierId }
            supplier?.let {
                val updatedSupplier = it.copy(reservedDays = reservedDays)
                supplierDao.updateSupplier(updatedSupplier)
            }
        }
    }
    fun sendSuppliersToServer(suppliers: List<Supplier>, serverIp: String, serverPort: Int) {
        viewModelScope.launch {
            val client = HttpClient(Android) {
                install(ContentNegotiation) {
                    gson()
                }
                install(Logging) {
                    level = LogLevel.INFO
                }
            }
            val url = "http://$serverIp:$serverPort/suppliers"
            try {
                suppliers.map {
                    Log.d("reserved",it.reservedDays.toString())
                }

                val response: List<Assignment> = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(suppliers)
                }.body()
                Toast.makeText(application.applicationContext, "Request sent!", Toast.LENGTH_LONG)
                    .show()
                Log.d("try", "sending")
                _assignments.value = response
            } catch (e: Exception) {
                Log.d("error sending", e.toString())
            } finally {
                client.close()
            }
        }
    }

    fun insertAssignmentsToDb(response: List<Assignment>) {
        viewModelScope.launch {
            try {
                if (response.isNotEmpty()) {
                    println("Successful response: $response")

                    response.forEach { assignment ->
                        val month = getCurrentMonth()
                        val supplierAssignment = SupplierAssignment(
                            dayOfMonth = assignment.dayOfTheMonth,
                            contractSupplier = assignment.contractSupplier,
                            stockSupplier = assignment.stockSupplier,
                            month = month
                        )
                        supplierDao.insertAssignment(supplierAssignment)
                    }
                    println("Assignments saved to database.")
                } else {
                    println("No assignments received.")
                }
            } catch (e: Exception) {
                println("Error fetching assignments: ${e.message}")
            }
        }
    }

    private fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }
}
