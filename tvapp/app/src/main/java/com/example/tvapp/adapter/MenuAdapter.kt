package com.example.tvapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvapp.MainActivity
import com.example.tvapp.R

data class MenuItem(val title: String, val iconUrl: String, val identificator: String)

class MenuAdapter(
    private val items: List<MenuItem>,
    private val context: Context,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.menu_title)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
                (context as MainActivity).collapseRecyclerView()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = items[position]


        holder.title.text = menuItem.title

        holder.itemView.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus) {
                holder.itemView.setBackgroundColor(Color.parseColor("#202020"))
                (context as MainActivity).expandRecyclerView()
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#0F0F0F"))
                (context as MainActivity).collapseRecyclerView()
                holder.itemView.clearFocus()
            }
        }
    }

    override fun getItemCount(): Int = items.size
}