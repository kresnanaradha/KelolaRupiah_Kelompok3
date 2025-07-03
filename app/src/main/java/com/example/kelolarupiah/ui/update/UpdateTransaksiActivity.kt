package com.example.kelolarupiah.ui.update

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kelolarupiah.R
import com.example.kelolarupiah.data.AppDatabase
import com.example.kelolarupiah.data.Transaction
import com.example.kelolarupiah.ui.home.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class UpdateTransaksiActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var etNote: EditText
    private lateinit var spType: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: ImageButton

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
        spType = findViewById(R.id.sp_category)
        btnSave = findViewById(R.id.btn_save)
        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)

        setupTypeSpinner()

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        database = AppDatabase.getInstance(this)

        // FIXED: Pastikan key sesuai dengan yang dikirim dari MainActivity
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

    private fun setupTypeSpinner() {
        val types = arrayOf("INCOME", "EXPENSE")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spType.adapter = adapter
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
                val position = (spType.adapter as ArrayAdapter<String>).getPosition(it.type)
                spType.setSelection(position)
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
        val type = spType.selectedItem.toString()

        if (title.isEmpty() || amount == null || date.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val updatedTransaction = Transaction(
                id = transactionId,
                title = title,
                amount = amount,
                date = date,
                type = type,
                note = if (note.isEmpty()) null else note
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
}