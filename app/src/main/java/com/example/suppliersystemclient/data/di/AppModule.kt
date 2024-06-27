package com.example.suppliersystemclient.data.di

import android.content.Context
import androidx.room.Room
import com.example.suppliersystemclient.room.MIGRATION_2_3
import com.example.suppliersystemclient.room.SqlDatabase
import com.example.suppliersystemclient.room.SupplierDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SqlDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SqlDatabase::class.java,
            "suppliers.db"
        )
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideSupplierDao(db: SqlDatabase): SupplierDao {
        return db.supplierDao()
    }
}