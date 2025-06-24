package com.example.kelolarupiah.ui.home

import androidx.lifecycle.*
import com.example.kelolarupiah.data.Transaction
import com.example.kelolarupiah.data.TransactionDao
import kotlinx.coroutines.launch
import java.util.*

enum class FilterType { BULANAN, MINGGUAN, HARIAN }

class HomeViewModel(private val dao: TransactionDao) : ViewModel() {
    private val filterType = MutableLiveData(FilterType.BULANAN)

    val filteredTransactions = filterType.switchMap { type ->
        val (start, end) = getDateRange(type)
        dao.getByDateRange(Date(start), Date(end)) // <-- perbaikan di sini
    }

    val income: LiveData<Long> = filteredTransactions.map { list ->
        list.filter { it.type == "INCOME" }.sumOf { it.amount }
    }
    val expense: LiveData<Long> = filteredTransactions.map { list ->
        list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    }
    val balance: LiveData<Long> = MediatorLiveData<Long>().apply {
        addSource(income) { value = (income.value ?: 0) - (expense.value ?: 0) }
        addSource(expense) { value = (income.value ?: 0) - (expense.value ?: 0) }
    }

    fun setFilter(type: FilterType) {
        filterType.value = type
    }

    private fun getDateRange(type: FilterType): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        val end = cal.timeInMillis
        when (type) {
            FilterType.BULANAN -> cal.set(Calendar.DAY_OF_MONTH, 1)
            FilterType.MINGGUAN -> cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
            FilterType.HARIAN -> {} // hari ini saja
        }
        val start = cal.timeInMillis
        return Pair(start, end)
    }
}