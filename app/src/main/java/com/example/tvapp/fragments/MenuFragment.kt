package com.example.tvapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.adapters.MenuAdapter
import com.example.tvapp.models.MenuItem

class MenuFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.menu_recycler_view)


        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val menuItems = listOf(
            MenuItem("Главная"),
            MenuItem("Аниме"),
            MenuItem("Мультфильмы"),
            MenuItem("Фильмы"),
            MenuItem("Сериалы"),
        )

        val adapter = MenuAdapter(menuItems)
        recyclerView.adapter = adapter
    }
}