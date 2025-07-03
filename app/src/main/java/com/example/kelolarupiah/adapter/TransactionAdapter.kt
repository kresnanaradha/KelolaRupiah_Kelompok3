package com.example.kelolarupiah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kelolarupiah.R
import com.example.kelolarupiah.data.Transaction
import com.example.kelolarupiah.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val onClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.tvTitle.text = transaction.title
            binding.tvAmount.text = "Rp${transaction.amount}"

            // Warna hijau untuk INCOME, merah untuk EXPENSE
            val incomeColor = ContextCompat.getColor(binding.root.context, R.color.green_600)
            val expenseColor = ContextCompat.getColor(binding.root.context, R.color.red_500)
            val color = if (transaction.type == "INCOME") incomeColor else expenseColor
            binding.tvAmount.setTextColor(color)
            binding.tvType.text = if (transaction.type == "INCOME") "Pemasukan" else "Pengeluaran"
            binding.tvType.setTextColor(color)

            // Format tanggal dari "yyyy-MM-dd" ke "dd MMMM yyyy"
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateObj = sdf.parse(transaction.date)
                binding.tvDate.text = dateObj?.let {
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it)
                } ?: transaction.date
            } catch (e: Exception) {
                binding.tvDate.text = transaction.date
            }

            // Set ikon dompet dalam lingkaran ungu
            binding.imgIcon.setImageResource(R.drawable.ic_wallet)
            binding.imgIcon.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_icon_circle)
            binding.imgIcon.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.purple_700))

            binding.root.setOnClickListener {
                onClick(transaction)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }
}