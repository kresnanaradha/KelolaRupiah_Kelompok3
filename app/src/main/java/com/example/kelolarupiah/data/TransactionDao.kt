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

    // Filter Mingguan (memastikan date string compatible dengan strftime)
    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%W', date || ' 00:00:00') = strftime('%W', 'now')
          AND strftime('%Y', date || ' 00:00:00') = strftime('%Y', 'now')
        ORDER BY date DESC
    """)
    fun getTransactionsWeekly(): LiveData<List<Transaction>>

    // Filter Bulanan (memastikan date string compatible dengan strftime)
    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%m', date || ' 00:00:00') = strftime('%m', 'now')
          AND strftime('%Y', date || ' 00:00:00') = strftime('%Y', 'now')
        ORDER BY date DESC
    """)
    fun getTransactionsMonthly(): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}