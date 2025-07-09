package com.example.kelolarupiah.ui.add

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kelolarupiah.R
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.data.Transaction
import com.example.kelolarupiah.ui.kategori.CategoryActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TambahTransaksiActivity : AppCompatActivity() {

    private var isIncome = true

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var etNote: EditText
    private lateinit var etCategory: EditText  // Menggunakan EditText untuk kategori
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnIncome: Button
    private lateinit var btnExpense: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_transaksi)

        // Menyambungkan UI dengan elemen layout
        etTitle = findViewById(R.id.et_title)
        etAmount = findViewById(R.id.et_amount)
        etDate = findViewById(R.id.et_date)
        etNote = findViewById(R.id.et_note)
        etCategory = findViewById(R.id.et_category)  // EditText untuk kategori
        btnSave = findViewById(R.id.btn_save)
        btnBack = findViewById(R.id.btn_back)
        btnIncome = findViewById(R.id.btn_income)
        btnExpense = findViewById(R.id.btn_expense)

        // Kategori dipilih melalui Activity lain
        etCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CATEGORY)  // Membuka CategoryActivity untuk memilih kategori
        }

        // Logika Tabs
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

        // Tanggal Picker
        etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(this, { _, year, month, day ->
                etDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        // Menyimpan transaksi
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val category = etCategory.text.toString()  // Kategori dari EditText
            val amountStr = etAmount.text.toString()
            val dateStr = etDate.text.toString()
            val note = etNote.text.toString()
            val type = if (isIncome) "INCOME" else "EXPENSE"

            if (title.isBlank() || category.isBlank() || amountStr.isBlank() || dateStr.isBlank()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toLongOrNull() ?: 0

            // Format tanggal dari dd/MM/yyyy ke yyyy-MM-dd untuk database
            val formattedDate = try {
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateObj = inputFormat.parse(dateStr)
                outputFormat.format(dateObj!!)
            } catch (e: Exception) {
                ""
            }

            // Menyimpan transaksi ke dalam database
            val transaction = Transaction(
                title = title,
                amount = amount,
                date = formattedDate,
                type = type,
                note = if (note.isEmpty()) null else note,
                category = category // Menyimpan kategori yang dipilih
            )

            lifecycleScope.launch {
                val db = AppDatabase.getInstance(this@TambahTransaksiActivity)
                db.transactionDao().insert(transaction)
                runOnUiThread {
                    Toast.makeText(this@TambahTransaksiActivity, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // Tombol kembali
        btnBack.setOnClickListener {
            finish()  // Kembali ke aktivitas sebelumnya
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