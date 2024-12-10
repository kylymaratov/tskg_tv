package com.example.tvapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tvapp.R
import com.example.tvapp.models.Movie

class HeaderFragment : Fragment() {
    lateinit var movieTitle: TextView
    lateinit var movieGenre: TextView
    lateinit var movieDescription: TextView
    lateinit var movieAdditionalInfo: TextView
    lateinit var movieCover: ImageView
    lateinit var gradientBanner: View
    lateinit var backButton: Button

    private var isInitialized: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val headerLayout: ConstraintLayout = view.findViewById(R.id.layout_header)
        val infoLayout: ConstraintLayout = headerLayout.findViewById(R.id.layout_info)

        movieCover = headerLayout.findViewById(R.id.movie_cover)
        movieTitle = infoLayout.findViewById(R.id.movie_title)
        movieGenre = infoLayout.findViewById(R.id.movie_genre)
        movieDescription = infoLayout.findViewById(R.id.movie_description)
        movieAdditionalInfo = infoLayout.findViewById(R.id.movie_additional_info)
        gradientBanner = view.findViewById(R.id.gradient_horizontal)
        backButton = view.findViewById(R.id.button_back)

        backButton.setOnClickListener{
            activity?.onBackPressed()
        }

        isInitialized = true
    }

    private fun fadeInView(view: View) {
        val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
        view.startAnimation(fadeIn)
    }

    private  fun fadeOutView(view: View) {
        val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
        view.startAnimation(fadeOut)
    }

    private fun fadeInImage(imageView: ImageView) {
        val fadeIn = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_in)
        imageView.startAnimation(fadeIn)
    }

    private fun fadeOutImage(imageView: ImageView) {
        val fadeOut = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_out)
        imageView.startAnimation(fadeOut)
    }

    fun updateBanner(movie: Movie, showBackButton: Boolean = false) {
        if (!isInitialized) {
            Handler(Looper.getMainLooper()).post {
                updateBanner(movie, showBackButton)
            }
            return
        }

        if (showBackButton) {
            backButton.visibility = View.VISIBLE
        }

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
