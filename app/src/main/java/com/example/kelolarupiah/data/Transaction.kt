package com.example.kelolarupiah.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long,
    val date: String, // format: "YYYY-MM-DD"
    val type: String, // "INCOME" atau "EXPENSE"
    val note: String? = null, // Kolom tambahan untuk catatan (opsional)
    val category: String? = null // Menambahkan kolom kategori
)
