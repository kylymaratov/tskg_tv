package com.example.tvapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tvapp.R
import com.example.tvapp.api.Response
import com.example.tvapp.models.Movie
import kotlinx.coroutines.launch

class HeaderFramgent : Fragment() {
    lateinit var movieTitle: TextView
    lateinit var movieGenre: TextView
    lateinit var movieDescription: TextView
    lateinit var movieAdditionalInfo: TextView
    lateinit var movieCover: ImageView
    lateinit var gradientBanner: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    fun init(view: View) {
        val headerLayout: ConstraintLayout = view.findViewById(R.id.layout_header)
        val infoLayout: ConstraintLayout = headerLayout.findViewById(R.id.layout_info)

        movieCover = headerLayout.findViewById(R.id.movie_cover)
        movieTitle = infoLayout.findViewById(R.id.movie_title)
        movieGenre = infoLayout.findViewById(R.id.movie_genre)
        movieDescription = infoLayout.findViewById(R.id.movie_description)
        movieAdditionalInfo= infoLayout.findViewById(R.id.movie_additional_info)

        gradientBanner = view.findViewById(R.id.gradient_horizontal)
    }

    fun fadeInView(view: View) {
        val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
        view.startAnimation(fadeIn)
    }

    fun fadeOutView(view: View) {
        val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
        view.startAnimation(fadeOut)
    }

    fun fadeInImage(imageView: ImageView) {
        val fadeIn = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_in)
        imageView.startAnimation(fadeIn)
    }

    fun fadeOutImage(imageView: ImageView) {
        val fadeOut = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_out)
        imageView.startAnimation(fadeOut)
    }

    fun updateBanner(movie: Movie) {
        fadeOutView(gradientBanner)
        gradientBanner.visibility = View.GONE

        movieTitle.text = movie.title
        movieGenre.text = movie.genre
        movieDescription.text = movie.details?.description
        movieAdditionalInfo.text = "Год: " + movie.year + " | " + "Сезонов: " + movie.details?.seasons?.size + " | " + "Страна: ${movie.country}"

        gradientBanner.visibility = View.VISIBLE

        fadeInView(gradientBanner)

        fadeOutImage(movieCover)
        Glide.with(this).load(movie.posterUrl).into(movieCover)
        fadeInImage(movieCover)
    }
}
