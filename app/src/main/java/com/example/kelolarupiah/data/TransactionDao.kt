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

    // Perbaikan: insert harus mengembalikan Long agar dapat ID transaksi
    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}
