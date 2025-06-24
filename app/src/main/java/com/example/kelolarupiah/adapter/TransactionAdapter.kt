package com.example.kelolarupiah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kelolarupiah.data.Transaction
import java.text.SimpleDateFormat
import java.util.*
import com.example.kelolarupiah.R

class TransactionAdapter(
    private val items: List<Transaction>,
    private val onClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvType: TextView = view.findViewById(R.id.tvType)
        init {
            view.setOnClickListener {
                onClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(item.date))
        holder.tvAmount.text = "Rp${item.amount}"
        holder.tvAmount.setTextColor(if (item.type == "INCOME") 0xFF21B573.toInt() else 0xFFE53935.toInt())
        holder.tvType.text = if (item.type == "INCOME") "Pemasukan" else "Pengeluaran"
        holder.tvType.setTextColor(if (item.type == "INCOME") 0xFF21B573.toInt() else 0xFFE53935.toInt())
    }
}