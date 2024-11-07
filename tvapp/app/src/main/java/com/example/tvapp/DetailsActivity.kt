package com.example.tvapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.tvapp.fragments.DetailsFragment
import com.example.tvapp.models.MoviesResponse

class DetailsActivity : FragmentActivity() {
    lateinit var detailsFragment: DetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val movie = intent.getParcelableExtra<MoviesResponse.Result.Detail>("movie")
        val identificator = intent.getStringExtra("identificator") ?: ""


        movie?.let {
            init(movie, identificator)
        }
    }

    fun init (movie: MoviesResponse.Result.Detail, identificator: String) {
        detailsFragment = DetailsFragment()

        val bundle = Bundle().apply {
            putParcelable("movie", movie)
            putString("identificator", identificator)
        }

        detailsFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_container, detailsFragment)
        transaction.commit()
    }
}