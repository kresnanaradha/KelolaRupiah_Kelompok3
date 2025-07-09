package com.example.kelolarupiah.ui.kategori

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.kelolarupiah.R

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori)  // Layout kategori yang Anda buat

        // Tombol back
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            // Menangani klik tombol kembali
            onBackPressed()  // Kembali ke aktivitas sebelumnya
        }

        // Menyambungkan kategori-kategori yang ada di layout dengan ID
        val categoryAir: LinearLayout = findViewById(R.id.category_air)
        val categoryBelanja: LinearLayout = findViewById(R.id.category_belanja)
        val categoryBensin: LinearLayout = findViewById(R.id.category_bensin)
        val categoryBus: LinearLayout = findViewById(R.id.category_bus)
        val categoryElektronik: LinearLayout = findViewById(R.id.category_elektronik)
        val categoryMakanan: LinearLayout = findViewById(R.id.category_makanan)

        // Menambahkan kategori baru: Gaji, Bonus, Komisi
        val categoryGaji: LinearLayout = findViewById(R.id.category_gaji)
        val categoryBonus: LinearLayout = findViewById(R.id.category_bonus)
        val categoryKomisi: LinearLayout = findViewById(R.id.category_komisi)

        // Menambahkan listener untuk setiap kategori yang ada
        categoryAir.setOnClickListener { onCategorySelected("Air") }
        categoryBelanja.setOnClickListener { onCategorySelected("Belanja") }
        categoryBensin.setOnClickListener { onCategorySelected("Bensin") }
        categoryBus.setOnClickListener { onCategorySelected("Bus") }
        categoryElektronik.setOnClickListener { onCategorySelected("Elektronik") }
        categoryMakanan.setOnClickListener { onCategorySelected("Makanan") }

        // Listener untuk kategori baru
        categoryGaji.setOnClickListener { onCategorySelected("Gaji") }
        categoryBonus.setOnClickListener { onCategorySelected("Bonus") }
        categoryKomisi.setOnClickListener { onCategorySelected("Komisi") }
    }

    // Fungsi untuk mengirimkan kategori yang dipilih
    private fun onCategorySelected(category: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("selectedCategory", category)  // Mengirim kategori yang dipilih
        setResult(RESULT_OK, resultIntent)  // Mengirim hasil ke activity yang memanggil
        finish()  // Menutup CategoryActivity dan kembali ke activity yang memanggil
    }
}
