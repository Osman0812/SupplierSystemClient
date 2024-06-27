package com.example.suppliersystemclient.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "supplierInfo") val info: String,
    @ColumnInfo(name = "supplierType") val type: Int,
    @ColumnInfo(name = "lastReservedDays") val reservedDays: String?
)

@Entity(tableName = "SupplierAssignments")
data class SupplierAssignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "dayOfMonth") val dayOfMonth: Int,
    @ColumnInfo(name = "contractSupplier") val contractSupplier: String,
    @ColumnInfo(name = "stockSupplier") val stockSupplier: String,
    @ColumnInfo(name = "month") val month: Int
)