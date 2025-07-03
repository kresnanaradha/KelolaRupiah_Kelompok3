package com.example.kelolarupiah.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// Hapus baris ini jika kamu tidak pakai converter
import androidx.room.TypeConverters

// GANTI VERSI jika kamu ubah struktur entitas
@Database(entities = [Transaction::class], version = 2, exportSchema = false)
// Hapus @TypeConverters jika tidak pakai
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kelolarupiah.db"
                )
                    .fallbackToDestructiveMigration() // akan hapus data lama jika versi meningkat
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
