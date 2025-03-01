package com.example.tvapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.MyApplication
import com.example.tvapp.R
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var categoryId: Int = 0
    private  var categoryTitle: String = ""
    val moviesGridFragment: MoviesGridFragment = MoviesGridFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            categoryId = it.getInt("categoryId", 0)
            categoryTitle = it.getString("categoryTitle", "")
        }
        getCategoryProcess(view)
        setFramgents()

    }

    private fun setFramgents() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.movies_grid_fragment, moviesGridFragment)
        transaction.commit()
    }


    private fun getCategoryProcess(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorText =  view.findViewById<TextView>(R.id.errorText)
        val categoryText = view.findViewById<TextView>(R.id.category_text)

        lifecycleScope.launch {
            try{
                println(categoryId)
                val data = (requireActivity().application as MyApplication).getCategoryData(categoryId, 1)
                moviesGridFragment.bindMoviesData(data.movies, categoryId, 1)
                categoryText.text = "Категория $categoryTitle"
            } catch(error: Exception) {
                errorText.visibility = TextView.VISIBLE
                errorText.text = error.message
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }




}