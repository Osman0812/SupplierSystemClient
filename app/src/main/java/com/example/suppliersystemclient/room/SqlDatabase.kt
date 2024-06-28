package com.example.suppliersystemclient.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.suppliersystemclient.data.model.Supplier
import com.example.suppliersystemclient.data.model.SupplierAssignment
import com.example.suppliersystemclient.util.Converters

@Database(
    entities = [Supplier::class, SupplierAssignment::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SqlDatabase : RoomDatabase() {
    abstract fun supplierDao(): SupplierDao
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS Suppliers")
        database.execSQL("DROP TABLE IF EXISTS SupplierAssignments")

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Suppliers (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                supplierInfo TEXT NOT NULL,
                supplierType TEXT NOT NULL,
                lastReservedDays TEXT
            )
        """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS SupplierAssignments (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                dayOfMonth INTEGER NOT NULL,
                contractSupplier TEXT NOT NULL,
                stockSupplier TEXT NOT NULL,
                month INTEGER NOT NULL
            )
        """.trimIndent()
        )
    }
}