package com.example.kelolarupiah.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.data.CategorySummary
import com.example.kelolarupiah.data.TransactionDao
import kotlinx.coroutines.Dispatchers

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDao: TransactionDao = AppDatabase.getInstance(application).transactionDao()

    // Mendapatkan total pemasukan secara asinkron
    val totalIncome: LiveData<Long> = liveData(Dispatchers.IO) {
        val total = transactionDao.getTotalByType("INCOME") ?: 0L  // Menangani null dengan nilai default 0L
        emit(total)
    }

    // Mendapatkan total pengeluaran secara asinkron
    val totalExpense: LiveData<Long> = liveData(Dispatchers.IO) {
        val total = transactionDao.getTotalByType("EXPENSE") ?: 0L  // Menangani null dengan nilai default 0L
        emit(total)
    }

    // Mendapatkan ringkasan kategori pemasukan
    val incomeCategorySummary: LiveData<List<CategorySummary>> = liveData(Dispatchers.IO) {
        val summary = transactionDao.getCategorySummary("INCOME") ?: emptyList()  // Menangani null dengan list kosong
        emit(summary)
    }

    // Mendapatkan ringkasan kategori pengeluaran
    val expenseCategorySummary: LiveData<List<CategorySummary>> = liveData(Dispatchers.IO) {
        val summary = transactionDao.getCategorySummary("EXPENSE") ?: emptyList()  // Menangani null dengan list kosong
        emit(summary)
    }
}