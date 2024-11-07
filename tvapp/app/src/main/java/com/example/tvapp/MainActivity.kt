package com.example.tvapp

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.adapter.MenuAdapter
import com.example.tvapp.adapter.MenuItem
import com.example.tvapp.api.Repository
import com.example.tvapp.fragments.HomeFragment
import com.example.tvapp.fragments.SearchFragment
import com.example.tvapp.utils.Common
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity(), View.OnKeyListener  {
    lateinit var repository: Repository
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view_menu)
        collapseRecyclerView()

        repository = (application as MyApplication).repository

        findViewById<TextView>(R.id.exit_button).setOnClickListener{
            finish()
        }

        lifecycleScope.launch {
            val menuList = repository.getMenu()

            if (menuList == null || menuList.result.isEmpty()) {
                findViewById<TextView>(R.id.error_message).apply {
                    visibility = View.VISIBLE
                }
                findViewById<TextView>(R.id.exit_button).apply {
                    visibility = View.VISIBLE
                    requestFocus()
                }

                return@launch
            }


            menuList?.let {
                val menuItems = mutableListOf<MenuItem>()


                menuItems.add(MenuItem("Поиск", "", "search"))

                menuList.result.forEach { menuItem ->
                    menuItems.add(MenuItem(menuItem.title, menuItem.icon, menuItem.identificator))
                }

                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                recyclerView.adapter = MenuAdapter(menuItems, this@MainActivity) { selectedItem ->

                    if (selectedItem.identificator == "search") {
                        changeFragment(SearchFragment())
                    } else {
                        val fragment = HomeFragment.newInstance(selectedItem.title, selectedItem.identificator)
                        changeFragment(fragment)
                    }
                }

                if (menuList.result.isNotEmpty()) {
                    val firstItem = menuList.result[0]
                    val firstFragment = HomeFragment.newInstance(firstItem.title, firstItem.identificator)
                    changeFragment(firstFragment)
                }
            }
        }

    }

    private fun changeFragment(fragment: Fragment) {
        repository.clearMovies()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    fun expandRecyclerView() {
        val targetWidth = Common.getWidthInPercent(this, 16)
        val animator = ObjectAnimator.ofInt(recyclerView, "width", recyclerView.width, targetWidth)
        animator.duration = 300
        animator.addUpdateListener { animation ->
            val newWidth = animation.animatedValue as Int
            recyclerView.layoutParams.width = newWidth
            recyclerView.requestLayout()
        }
        animator.start()
    }

    fun collapseRecyclerView() {
        val targetWidth = 3
        val animator = ObjectAnimator.ofInt(recyclerView, "width", recyclerView.width, targetWidth)
        animator.duration = 300
        animator.addUpdateListener { animation ->
            val newWidth = animation.animatedValue as Int
            recyclerView.layoutParams.width = newWidth
            recyclerView.requestLayout()
        }
        animator.start()

    }



    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (isAtLeftEdge()) {
                    expandRecyclerView()
                }
            }
        }
        return false
    }

    private fun isAtLeftEdge(): Boolean {
         val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
         val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: return false

         return firstVisibleItemPosition == 0
    }
}