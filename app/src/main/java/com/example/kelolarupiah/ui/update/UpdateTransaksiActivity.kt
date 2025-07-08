package com.example.kelolarupiah.ui.update

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kelolarupiah.R
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.data.Transaction
import com.example.kelolarupiah.ui.home.MainActivity
import com.example.kelolarupiah.ui.kategori.CategoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateTransaksiActivity : AppCompatActivity() {

    private var isIncome = true  // Default jenis transaksi adalah INCOME

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var etNote: EditText
    private lateinit var etCategory: EditText // EditText untuk kategori
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnIncome: Button  // Tombol untuk INCOME
    private lateinit var btnExpense: Button  // Tombol untuk EXPENSE

    private var transactionId: Long = -1L
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_transaksi)

        // Inisialisasi View
        etTitle = findViewById(R.id.et_title)
        etAmount = findViewById(R.id.et_amount)
        etDate = findViewById(R.id.et_date)
        etNote = findViewById(R.id.et_note)
        etCategory = findViewById(R.id.et_category) // EditText untuk kategori
        btnSave = findViewById(R.id.btn_save)
        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)
        btnIncome = findViewById(R.id.btn_income)
        btnExpense = findViewById(R.id.btn_expense)

        // Kategori dipilih melalui Activity lain
        etCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CATEGORY)  // Membuka CategoryActivity untuk memilih kategori
        }

        // Logika Tabs (Pemasukan / Pengeluaran)
        btnIncome.setOnClickListener {
            isIncome = true
            btnIncome.background = getDrawable(R.drawable.bg_tab_selected)
            btnIncome.setTextColor(resources.getColor(android.R.color.white))
            btnExpense.background = null
            btnExpense.setTextColor(resources.getColor(R.color.purple_700))
        }

        btnExpense.setOnClickListener {
            isIncome = false
            btnExpense.background = getDrawable(R.drawable.bg_tab_selected)
            btnExpense.setTextColor(resources.getColor(android.R.color.white))
            btnIncome.background = null
            btnIncome.setTextColor(resources.getColor(R.color.purple_700))
        }

        database = AppDatabase.getInstance(this)

        // Memastikan key yang dikirim dari MainActivity
        transactionId = intent.getLongExtra("trx_id", -1L)

        if (transactionId != -1L) {
            loadTransaction(transactionId)
        } else {
            Toast.makeText(this, "Transaksi tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSave.setOnClickListener {
            updateTransaction()
        }

        btnDelete.setOnClickListener {
            deleteTransaction()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun loadTransaction(id: Long) {
        lifecycleScope.launch {
            val transaction = withContext(Dispatchers.IO) {
                database.transactionDao().getTransactionById(id)
            }

            transaction?.let {
                etTitle.setText(it.title)
                etAmount.setText(it.amount.toString())
                etDate.setText(it.date)
                etNote.setText(it.note ?: "")
                etCategory.setText(it.category ?: "") // Menampilkan kategori yang dipilih sebelumnya
                // Set the appropriate type for INCOME or EXPENSE
                if (it.type == "INCOME") {
                    isIncome = true
                    btnIncome.background = getDrawable(R.drawable.bg_tab_selected)
                    btnIncome.setTextColor(resources.getColor(android.R.color.white))
                    btnExpense.background = null
                    btnExpense.setTextColor(resources.getColor(R.color.purple_700))
                } else {
                    isIncome = false
                    btnExpense.background = getDrawable(R.drawable.bg_tab_selected)
                    btnExpense.setTextColor(resources.getColor(android.R.color.white))
                    btnIncome.background = null
                    btnIncome.setTextColor(resources.getColor(R.color.purple_700))
                }
            } ?: run {
                Toast.makeText(this@UpdateTransaksiActivity, "Data transaksi tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateTransaction() {
        val title = etTitle.text.toString().trim()
        val amount = etAmount.text.toString().toLongOrNull()
        val date = etDate.text.toString().trim()
        val note = etNote.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val type = if (isIncome) "INCOME" else "EXPENSE"

        if (title.isEmpty() || amount == null || date.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val updatedTransaction = Transaction(
                id = transactionId,
                title = title,
                amount = amount,
                date = date,
                type = type, // Pemasukan atau Pengeluaran
                note = if (note.isEmpty()) null else note,
                category = category // Menyimpan kategori yang dipilih
            )

            withContext(Dispatchers.IO) {
                database.transactionDao().update(updatedTransaction)
            }

            Toast.makeText(this@UpdateTransaksiActivity, "Transaksi berhasil diupdate", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun deleteTransaction() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val transaction = database.transactionDao().getTransactionById(transactionId)
                transaction?.let {
                    database.transactionDao().delete(it)
                }
            }

            Toast.makeText(this@UpdateTransaksiActivity, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Menangani hasil dari CategoryActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CATEGORY && resultCode == RESULT_OK) {
            val selectedCategory = data?.getStringExtra("selectedCategory")
            etCategory.setText(selectedCategory)  // Menampilkan kategori yang dipilih ke EditText
        }
    }

    companion object {
        private const val REQUEST_CODE_CATEGORY = 1001  // Kode permintaan untuk CategoryActivity
    }
}
