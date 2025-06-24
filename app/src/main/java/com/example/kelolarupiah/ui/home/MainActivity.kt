package com.example.kelolarupiah.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelolarupiah.R
import com.example.kelolarupiah.adapter.TransactionAdapter
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.databinding.ActivityMainBinding
import com.example.kelolarupiah.ui.report.LaporanActivity
import com.example.kelolarupiah.ui.update.UpdateTransaksiActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: TransactionAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup DB & ViewModel
        val dao = AppDatabase.getInstance(this).transactionDao()
        viewModel = ViewModelProvider(this, HomeViewModelFactory(dao))[HomeViewModel::class.java]

        binding.rvTransactions.layoutManager = LinearLayoutManager(this)

        // Filter buttons
        binding.btnBulanan.setOnClickListener { viewModel.setFilter(FilterType.BULANAN) }
        binding.btnMingguan.setOnClickListener { viewModel.setFilter(FilterType.MINGGUAN) }
        binding.btnHarian.setOnClickListener { viewModel.setFilter(FilterType.HARIAN) }

        // Observe LiveData
        viewModel.income.observe(this) { binding.tvIncomeValue.text = "Rp${it}" }
        viewModel.expense.observe(this) { binding.tvExpenseValue.text = "Rp${it}" }
        viewModel.balance.observe(this) { binding.tvBalance.text = "Rp${it}" }

        viewModel.filteredTransactions.observe(this) { list ->
            adapter = TransactionAdapter(list) { trx ->
                val intent = Intent(this, UpdateTransaksiActivity::class.java)
                intent.putExtra("trx_id", trx.id)
                startActivity(intent)
            }
            binding.rvTransactions.adapter = adapter
        }

        // FAB (Tetap digunakan untuk tambah transaksi)
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahTransaksiActivity::class.java))
        }

        // Bottom nav tanpa navigation_add
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> true // Tetap di halaman home
                R.id.navigation_report -> {
                    startActivity(Intent(this, LaporanActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
