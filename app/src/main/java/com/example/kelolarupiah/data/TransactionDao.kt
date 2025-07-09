package com.example.kelolarupiah.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    // Filter Harian
    @Query("SELECT * FROM transactions WHERE date = date('now') ORDER BY date DESC")
    fun getTransactionsDaily(): LiveData<List<Transaction>>

    // Filter Mingguan
    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%W', date || ' 00:00:00') = strftime('%W', 'now')
          AND strftime('%Y', date || ' 00:00:00') = strftime('%Y', 'now')
        ORDER BY date DESC
    """)
    fun getTransactionsWeekly(): LiveData<List<Transaction>>

    // Filter Bulanan
    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%m', date || ' 00:00:00') = strftime('%m', 'now')
          AND strftime('%Y', date || ' 00:00:00') = strftime('%Y', 'now')
        ORDER BY date DESC
    """)
    fun getTransactionsMonthly(): LiveData<List<Transaction>>

    // Ambil transaksi berdasarkan ID
    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Long): Transaction?

    // Ambil transaksi berdasarkan kategori
    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    suspend fun getTotalByType(type: String): Long? // Mengembalikan Long? yang bisa null

    // Mendapatkan ringkasan kategori berdasarkan tipe (INCOME atau EXPENSE)
    @Query("SELECT category, SUM(amount) AS totalAmount FROM transactions WHERE type = :type GROUP BY category")
    suspend fun getCategorySummary(type: String): List<CategorySummary>

    // Insert transaksi dan kembalikan ID transaksi yang baru dimasukkan
    @Insert
    suspend fun insert(transaction: Transaction): Long

    // Update transaksi
    @Update
    suspend fun update(transaction: Transaction)

    // Hapus transaksi
    @Delete
    suspend fun delete(transaction: Transaction)
}