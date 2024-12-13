package com.example.tvapp

import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.adapters.MenuAdapter
import com.example.tvapp.fragments.CategoryFragment
import com.example.tvapp.fragments.HomeFragment
import com.example.tvapp.fragments.SearchFragment
import com.example.tvapp.models.MenuItem


class MainActivity : FragmentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var container: FrameLayout

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setFramgents()
        recyclerView = findViewById(R.id.nav_categories)
        container = findViewById(R.id.container)

        val categories = listOf(MenuItem(id = 1, title = "Главная"),
            MenuItem(id = 2, title = "Поиск"),
            MenuItem(id = 12, title = "Аниме"),
            MenuItem(id = 19, title = "Сериалы"),
            MenuItem(id = 25, title = "Полный метр"),
            MenuItem(id = 3, title = "Для детей"), MenuItem(id = 18, title = "Турецкие фильмы"),
            MenuItem(id = 29, title = "Южная Корея"),
            MenuItem(id = 8, title = "Британские"),
            MenuItem(id = 31, title = "Китай"),
            MenuItem(id = 30, title = "Япония"),
            MenuItem(id = 4, title = "Российские"),
            MenuItem(id = 20, title = "Украинские"),
            MenuItem(id =5, title = "Cartoon Network")
        )

        val adapter = MenuAdapter(categories) {item ->
            if (item.id == 1) {
                setFramgents()
            }else if(item.id == 2) {
                val searchFragment = SearchFragment()
                changeFragment(searchFragment)
            }else {
                val categoryFragment = CategoryFragment()
                val bundle = Bundle().apply {
                    putInt("categoryId", item.id)
                    putString("categoryTitle", item.title)
                }
                categoryFragment.arguments = bundle
                changeFragment(categoryFragment)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }


    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun setFramgents() {
        val homeFragment: HomeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.commit()
    }
}