package com.example.kelolarupiah.ui.report

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kelolarupiah.R
import com.example.kelolarupiah.ui.home.MainActivity
import com.example.kelolarupiah.data.CategorySummary
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class LaporanActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var reportList: LinearLayout  // LinearLayout for the report list
    private val transactionViewModel: TransactionViewModel by viewModels()

    private lateinit var btnExpense: Button
    private lateinit var btnIncome: Button
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        // Initialize UI components
        pieChart = findViewById(R.id.chart)
        reportList = findViewById(R.id.report_list)  // Initialize reportList
        btnExpense = findViewById(R.id.btn_expense)
        btnIncome = findViewById(R.id.btn_income)
        bottomNav = findViewById(R.id.bottom_navigation)

        // Handle bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_report -> {
                    // When "Laporan" is clicked, show Pengeluaran (expenses) first
                    showExpenseData()  // Display Pengeluaran by default when navigating to Laporan
                    true
                }
                else -> false
            }
        }
        bottomNav.menu.findItem(R.id.navigation_report).isChecked = true

        // Default setup: Show Pemasukan (Income) when activity is created
        showIncomeData()  // Initially, show Pemasukan data

        // Observe pemasukan category summary
        transactionViewModel.incomeCategorySummary.observe(this, Observer { incomeCategories ->
            if (incomeCategories.isNotEmpty()) {
                displayPieChart(incomeCategories, "Pemasukan")
                displayReportList(incomeCategories, "Pemasukan")
            } else {
                displayEmptyPieChart()
                displayEmptyReportList()
            }
        })

        // Observe pengeluaran category summary
        transactionViewModel.expenseCategorySummary.observe(this, Observer { expenseCategories ->
            if (expenseCategories.isNotEmpty()) {
                displayPieChart(expenseCategories, "Pengeluaran")
                displayReportList(expenseCategories, "Pengeluaran")
            } else {
                displayEmptyPieChart()
                displayEmptyReportList()
            }
        })

        // Handle button clicks for pemasukan and pengeluaran
        btnExpense.setOnClickListener {
            btnExpense.setTextColor(Color.WHITE) // Change text color to white for selected
            btnExpense.setBackgroundResource(R.drawable.bg_tab_selected) // Change background color for selected
            btnIncome.setTextColor(Color.parseColor("#A020F0")) // Reset text color for unselected
            btnIncome.setBackgroundResource(android.R.color.transparent) // Reset background for unselected

            showExpenseData()  // Show expense data when "Pengeluaran" is clicked
        }

        btnIncome.setOnClickListener {
            btnIncome.setTextColor(Color.WHITE) // Change text color to white for selected
            btnIncome.setBackgroundResource(R.drawable.bg_tab_selected) // Change background color for selected
            btnExpense.setTextColor(Color.parseColor("#A020F0")) // Reset text color for unselected
            btnExpense.setBackgroundResource(android.R.color.transparent) // Reset background for unselected

            showIncomeData()  // Show income data when "Pemasukan" is clicked
        }
    }

    private fun showExpenseData() {
        transactionViewModel.expenseCategorySummary.observe(this, Observer { expenseCategories ->
            if (expenseCategories.isNotEmpty()) {
                displayPieChart(expenseCategories, "Pengeluaran")
                displayReportList(expenseCategories, "Pengeluaran")
            } else {
                displayEmptyPieChart()
                displayEmptyReportList()
            }
        })
    }

    private fun showIncomeData() {
        transactionViewModel.incomeCategorySummary.observe(this, Observer { incomeCategories ->
            if (incomeCategories.isNotEmpty()) {
                displayPieChart(incomeCategories, "Pemasukan")
                displayReportList(incomeCategories, "Pemasukan")
            } else {
                displayEmptyPieChart()
                displayEmptyReportList()
            }
        })
    }

    private fun displayPieChart(categories: List<CategorySummary>, type: String) {
        val entries = categories.map { PieEntry(it.totalAmount.toFloat(), it.category) }

        val dataSet = PieDataSet(entries, "Categories")
        dataSet.setColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()  // To refresh the PieChart
    }

    private fun displayEmptyPieChart() {
        val emptyList = listOf<PieEntry>()  // Empty list if no data
        val dataSet = PieDataSet(emptyList, "No Data")
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()  // To refresh the PieChart
    }

    // Function to display the report list dynamically
    private fun displayReportList(categories: List<CategorySummary>, type: String) {
        reportList.removeAllViews()  // Clear existing views
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)

        for ((index, category) in categories.withIndex()) {
            val textView = TextView(this)
            textView.text = "${category.category}: Rp ${category.totalAmount}"
            textView.textSize = 16f
            textView.setTextColor(colors[index % colors.size])  // Set color to match PieChart segments
            textView.setPadding(0, 8, 0, 8)
            reportList.addView(textView)  // Add dynamically to reportList
        }
    }

    // Display empty report list if no data available
    private fun displayEmptyReportList() {
        val textView = TextView(this)
        textView.text = "No data available"
        textView.textSize = 16f
        textView.setPadding(0, 8, 0, 8)
        reportList.addView(textView)
    }
}
