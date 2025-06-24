package com.example.kelolarupiah.ui.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kelolarupiah.databinding.ActivityUpdateTransaksiBinding

class UpdateTransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUpdateInfo.text = "Halaman Update"
    }
}
