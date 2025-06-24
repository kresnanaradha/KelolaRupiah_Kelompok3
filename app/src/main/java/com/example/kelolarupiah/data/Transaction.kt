package com.example.kelolarupiah.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long,
    val date: Long,
    val type: String // "INCOME" atau "EXPENSE"
)