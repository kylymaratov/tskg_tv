package com.example.tvapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.models.MenuItem

class MenuAdapter(private val categories: List<MenuItem>, private val onClick: (MenuItem) -> Unit) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(categories[position])
        holder.itemView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    override fun getItemCount(): Int = categories.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.menu_item_text)

        fun bind(menuItem: MenuItem) {
            categoryTextView.text = menuItem.title

            itemView.setOnClickListener { onClick(menuItem) }
        }
        init {
            itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {

                    itemView.setBackgroundColor(android.graphics.Color.parseColor("#808080"))
                } else {

                    itemView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            }
        }
    }


}