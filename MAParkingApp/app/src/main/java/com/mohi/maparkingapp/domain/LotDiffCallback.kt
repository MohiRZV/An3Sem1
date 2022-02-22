package com.mohi.maparkingapp.domain

import androidx.recyclerview.widget.DiffUtil

class LotDiffCallback: DiffUtil.ItemCallback<Lot>() {
    override fun areItemsTheSame(oldItem: Lot, newItem: Lot): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Lot, newItem: Lot): Boolean {
        return oldItem.address==newItem.address &&
                oldItem.number==newItem.number &&
                oldItem.id==newItem.id
    }
}