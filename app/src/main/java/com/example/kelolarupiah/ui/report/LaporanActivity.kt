package com.example.kelolarupiah.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kelolarupiah.databinding.ActivityLaporanBinding

class LaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLaporanInfo.text = "Halaman Laporan"
    }
}
