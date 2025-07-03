package com.example.kelolarupiah.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelolarupiah.R
import com.example.kelolarupiah.adapter.TransactionAdapter
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.databinding.ActivityMainBinding
import com.example.kelolarupiah.ui.report.LaporanActivity
import com.example.kelolarupiah.ui.add.TambahTransaksiActivity
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

        // Inisialisasi RecyclerView dan Adapter SEKALI SAJA
        adapter = TransactionAdapter { trx ->
            val intent = Intent(this, UpdateTransaksiActivity::class.java)
            intent.putExtra("trx_id", trx.id)
            startActivity(intent)
        }
        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = adapter

        // Fungsi untuk update tampilan tombol filter
        fun updateFilterButtons(selected: Int) {
            val selectedBg = ContextCompat.getDrawable(this, R.drawable.filter_chip_selected)
            val unselectedBg = ContextCompat.getDrawable(this, R.drawable.filter_chip_unselected)
            val selectedTextColor = ContextCompat.getColor(this, android.R.color.white)
            val unselectedTextColor = ContextCompat.getColor(this, R.color.purple_700)

            binding.btnBulanan.background = if (selected == 0) selectedBg else unselectedBg
            binding.btnBulanan.setTextColor(if (selected == 0) selectedTextColor else unselectedTextColor)
            binding.btnMingguan.background = if (selected == 1) selectedBg else unselectedBg
            binding.btnMingguan.setTextColor(if (selected == 1) selectedTextColor else unselectedTextColor)
            binding.btnHarian.background = if (selected == 2) selectedBg else unselectedBg
            binding.btnHarian.setTextColor(if (selected == 2) selectedTextColor else unselectedTextColor)
        }

        // Default: filter mingguan aktif (ubah jika ingin default lain)
        updateFilterButtons(1)

        binding.btnBulanan.setOnClickListener {
            viewModel.setFilter(FilterType.BULANAN)
            updateFilterButtons(0)
        }
        binding.btnMingguan.setOnClickListener {
            viewModel.setFilter(FilterType.MINGGUAN)
            updateFilterButtons(1)
        }
        binding.btnHarian.setOnClickListener {
            viewModel.setFilter(FilterType.HARIAN)
            updateFilterButtons(2)
        }

        // Observe LiveData
        viewModel.income.observe(this) { binding.tvIncomeValue.text = "Rp${it}" }
        viewModel.expense.observe(this) { binding.tvExpenseValue.text = "Rp${it}" }
        viewModel.balance.observe(this) { binding.tvBalance.text = "Rp${it}" }

        viewModel.filteredTransactions.observe(this) { list ->
            adapter.submitList(list)
        }

        // FAB untuk tambah transaksi
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahTransaksiActivity::class.java))
        }

        // Bottom nav
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_report -> {
                    startActivity(Intent(this, LaporanActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}