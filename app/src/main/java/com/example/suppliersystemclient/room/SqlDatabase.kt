package com.example.suppliersystemclient.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.suppliersystemclient.data.Supplier
import com.example.suppliersystemclient.data.SupplierAssignment
import com.example.suppliersystemclient.util.Converters

@Database(
    entities = [Supplier::class, SupplierAssignment::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SqlDatabase : RoomDatabase() {
    abstract fun supplierDao(): SupplierDao

    companion object {
        @Volatile
        private var INSTANCE: SqlDatabase? = null
        fun getDatabase(context: Context): SqlDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SqlDatabase::class.java,
                    "suppliers.db"
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS Suppliers")
        database.execSQL("DROP TABLE IF EXISTS SupplierAssignments")

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS Suppliers (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                supplierInfo TEXT NOT NULL,
                supplierType TEXT NOT NULL,
                lastReservedDays TEXT
            )
        """.trimIndent())

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS SupplierAssignments (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                dayOfMonth INTEGER NOT NULL,
                contractSupplier TEXT NOT NULL,
                stockSupplier TEXT NOT NULL,
                month INTEGER NOT NULL
            )
        """.trimIndent())
    }
}