package com.example.angkoot.ui.ordering

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.angkoot.databinding.ItemPlacesBinding
import com.example.angkoot.domain.model.Place

class PlacesAdapter : ListAdapter<Place, PlacesAdapter.PlacesViewHolder>(PlaceComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding = ItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        getItem(position).apply {
            if (this != null) holder.bind(this)
        }
    }

    inner class PlacesViewHolder(
        private val binding: ItemPlacesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            with(binding) {
                tvDescription.text = place.description
            }
        }
    }

    class PlaceComparator : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem
    }

    interface OnClickCallback {
        fun onClick(place: Place)
    }

    private var callback: OnClickCallback? = null

    fun setItemCallback(callback: OnClickCallback?) {
        this.callback = callback
    }
}