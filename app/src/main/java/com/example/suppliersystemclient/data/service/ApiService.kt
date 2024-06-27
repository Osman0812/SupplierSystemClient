package com.example.suppliersystemclient.data.service

import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.data.SupplierAssignment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/suppliers")
    fun sendSuppliers(@Body suppliers: List<Supplier>): Call<List<SupplierAssignment>>
}