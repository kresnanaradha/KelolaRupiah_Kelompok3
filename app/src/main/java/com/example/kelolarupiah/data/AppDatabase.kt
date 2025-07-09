package com.example.kelolarupiah.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.TypeConverters

@Database(entities = [Transaction::class], version = 2, exportSchema = false) // Update version ke 3
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Menambahkan kolom 'category' pada tabel 'transactions'
                database.execSQL("ALTER TABLE transactions ADD COLUMN category TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kelolarupiah.db"
                )
                    .addMigrations(MIGRATION_1_2)  // Menambahkan migrasi
                    .fallbackToDestructiveMigration()  // Opsional: jika migrasi gagal, hapus data lama
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}