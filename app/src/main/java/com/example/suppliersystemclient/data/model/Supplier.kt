package com.example.suppliersystemclient.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "supplierInfo") val info: String,
    @ColumnInfo(name = "supplierType") val type: String,
    @ColumnInfo(name = "lastReservedDays") val reservedDays: List<Int>?
)

