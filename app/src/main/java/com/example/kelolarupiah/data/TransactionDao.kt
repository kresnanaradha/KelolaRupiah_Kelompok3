package com.example.kelolarupiah.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date = date('now') ORDER BY date DESC")
    fun getTransactionsDaily(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE strftime('%W', date) = strftime('%W', 'now') AND strftime('%Y', date) = strftime('%Y', 'now') ORDER BY date DESC")
    fun getTransactionsWeekly(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE strftime('%m', date) = strftime('%m', 'now') AND strftime('%Y', date) = strftime('%Y', 'now') ORDER BY date DESC")
    fun getTransactionsMonthly(): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}
