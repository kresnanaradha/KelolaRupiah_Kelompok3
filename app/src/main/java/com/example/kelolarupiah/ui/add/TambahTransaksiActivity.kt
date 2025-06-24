package com.example.kelolarupiah.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kelolarupiah.R
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.data.Transaction
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TambahTransaksiActivity : AppCompatActivity() {

    private var isIncome = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_transaksi)

        val btnIncome = findViewById<Button>(R.id.btn_income)
        val btnExpense = findViewById<Button>(R.id.btn_expense)
        val spCategory = findViewById<Spinner>(R.id.sp_category)
        val etTitle = findViewById<EditText>(R.id.et_title)
        val etAmount = findViewById<EditText>(R.id.et_amount)
        val etDate = findViewById<EditText>(R.id.et_date)
        val etNote = findViewById<EditText>(R.id.et_note)
        val btnSave = findViewById<Button>(R.id.btn_save)

        // Set up spinner categories
        val incomeCategories = arrayOf("Gaji", "Bonus", "Penjualan", "Lainnya")
        val expenseCategories = arrayOf("Makan", "Transportasi", "Belanja", "Lainnya")
        spCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, incomeCategories)

        // Tabs logic
        btnIncome.setOnClickListener {
            isIncome = true
            btnIncome.background = getDrawable(R.drawable.bg_tab_selected)
            btnIncome.setTextColor(resources.getColor(android.R.color.white))
            btnExpense.background = null
            btnExpense.setTextColor(resources.getColor(R.color.purple_700))
            spCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, incomeCategories)
        }
        btnExpense.setOnClickListener {
            isIncome = false
            btnExpense.background = getDrawable(R.drawable.bg_tab_selected)
            btnExpense.setTextColor(resources.getColor(android.R.color.white))
            btnIncome.background = null
            btnIncome.setTextColor(resources.getColor(R.color.purple_700))
            spCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, expenseCategories)
        }

        // Date picker
        etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(this, { _, year, month, day ->
                etDate.setText(String.format("%02d/%02d/%04d", day, month+1, year))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val category = spCategory.selectedItem.toString()
            val amountStr = etAmount.text.toString()
            val dateStr = etDate.text.toString()
            val note = etNote.text.toString()
            val type = if (isIncome) "INCOME" else "EXPENSE"

            if (title.isBlank() || category.isBlank() || amountStr.isBlank() || dateStr.isBlank()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toLongOrNull() ?: 0
            val date = try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }

            // Gunakan AppDatabase singleton
            val transaction = Transaction(
                title = title,
                amount = amount,
                date = date,
                type = type
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
    }
}