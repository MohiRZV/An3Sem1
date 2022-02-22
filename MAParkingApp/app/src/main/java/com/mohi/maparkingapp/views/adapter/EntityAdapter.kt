package com.mohi.maparkingapp.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohi.maparkingapp.R
import com.mohi.maparkingapp.domain.Lot
import com.mohi.maparkingapp.domain.LotDiffCallback

interface AdapterClickListener {
    fun onClick(lot: Lot)
}

class EntityAdapter(private val listener: AdapterClickListener):
ListAdapter<Lot, LotViewHolder>(LotDiffCallback()){
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.entity_display_model, parent, false)
        this.parent = parent
        return LotViewHolder(view)
    }

    override fun onBindViewHolder(holder: LotViewHolder, position: Int) {
        holder.address.text = currentList[position].address
        holder.count.text = currentList[position].count.toString()
        holder.number.text = currentList[position].number
        holder.status.text = currentList[position].status
        holder.container.setOnClickListener { listener.onClick(currentList[position]) }
    }

    fun remove(position: Int){
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}

class LotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var container: ViewGroup = itemView.findViewById(R.id.container)
    var number: TextView = itemView.findViewById(R.id.lot_tv_number)
    var count: TextView = itemView.findViewById(R.id.lot_tv_count)
    var status: TextView = itemView.findViewById(R.id.lot_tv_status)
    var address: TextView = itemView.findViewById(R.id.lot_tv_address)
}