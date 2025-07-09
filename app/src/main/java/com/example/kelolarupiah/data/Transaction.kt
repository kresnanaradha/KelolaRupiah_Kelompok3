package com.example.kelolarupiah.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long,       // Jumlah uang transaksi
    val date: String,       // Format: "YYYY-MM-DD"
    val type: String,       // Tipe transaksi: "INCOME" atau "EXPENSE"
    val category: String,   // Kategori transaksi
    val note: String? = null // Catatan tambahan
)