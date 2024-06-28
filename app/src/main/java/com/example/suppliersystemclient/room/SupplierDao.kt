package com.example.suppliersystemclient.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.suppliersystemclient.data.model.Supplier
import com.example.suppliersystemclient.data.model.SupplierAssignment
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: Supplier)
    @Update
    suspend fun updateSupplier(supplier: Supplier)
    @Delete
    suspend fun deleteSupplier(supplier: Supplier)
    @Query("SELECT * FROM Suppliers")
    fun getAllSuppliers(): Flow<List<Supplier>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: SupplierAssignment)
}