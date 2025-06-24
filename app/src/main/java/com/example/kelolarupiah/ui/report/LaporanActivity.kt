package com.example.kelolarupiah.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kelolarupiah.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReportActivity : AppCompatActivity() {

    private lateinit var btnIncome: Button
    private lateinit var btnExpense: Button
    private lateinit var tvTotal: TextView
    private lateinit var dropdownMonth: TextView
    private lateinit var reportList: LinearLayout
    private lateinit var chart: PieChart
    private lateinit var bottomNav: BottomNavigationView

    private var isIncomeSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        // Inisialisasi view
        btnIncome = findViewById(R.id.btn_income)
        btnExpense = findViewById(R.id.btn_expense)
        tvTotal = findViewById(R.id.tv_total)
        dropdownMonth = findViewById(R.id.dropdown_month)
        reportList = findViewById(R.id.report_list)
        chart = findViewById(R.id.chart)
        bottomNav = findViewById(R.id.bottom_navigation)

        setupTabs()
        setupBottomNav()
        setupPieChart() // Tampilkan grafik donat
    }

    private fun setupTabs() {
        btnIncome.setOnClickListener {
            isIncomeSelected = true
            btnIncome.setBackgroundResource(R.drawable.bg_tab_selected)
            btnIncome.setTextColor(resources.getColor(android.R.color.white))
            btnExpense.setBackgroundResource(android.R.color.transparent)
            btnExpense.setTextColor(resources.getColor(R.color.purple_700))

            // TODO: Update chart dengan data pemasukan
        }

        btnExpense.setOnClickListener {
            isIncomeSelected = false
            btnExpense.setBackgroundResource(R.drawable.bg_tab_selected)
            btnExpense.setTextColor(resources.getColor(android.R.color.white))
            btnIncome.setBackgroundResource(android.R.color.transparent)
            btnIncome.setTextColor(resources.getColor(R.color.purple_700))

            // TODO: Update chart dengan data pengeluaran
        }
    }

    private fun setupBottomNav() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // TODO: Navigasi ke HomeActivity jika diinginkan
                    true
                }
                R.id.navigation_report -> {
                    true
                }
                else -> false
            }
        }
        bottomNav.menu.findItem(R.id.navigation_report).isChecked = true
    }


    private fun setupPieChart() {
        val entries = listOf(
            PieEntry(2000000f, "Fashion"),
            PieEntry(545000f, "Listrik"),
            PieEntry(455000f, "Transportasi")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            Color.parseColor("#FFC107"), // Kuning
            Color.parseColor("#7E57C2"), // Ungu
            Color.parseColor("#EF5350")  // Merah
        )

        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)

        chart.data = data
        chart.setUsePercentValues(false)
        chart.setDrawHoleEnabled(true)
        chart.holeRadius = 60f
        chart.setHoleColor(Color.TRANSPARENT)
        chart.setDrawEntryLabels(false)
        chart.setDrawCenterText(true)
        chart.centerText = "Rp3.000.000"
        chart.setCenterTextSize(16f)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        chart.invalidate() // Refresh tampilan
    }
}
