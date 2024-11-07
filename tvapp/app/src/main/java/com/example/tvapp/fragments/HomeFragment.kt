package com.example.tvapp.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.tvapp.MyApplication
import com.example.tvapp.R
import com.example.tvapp.api.Response
import com.example.tvapp.api.Repository
import com.example.tvapp.models.MoviesResponse
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var movie_title: TextView
    lateinit var movie_genre: TextView
    lateinit var movie_description: TextView
    lateinit var movie_additional_info: TextView

    lateinit var movie_cover: ImageView
    lateinit var identificator: String
    lateinit var moviesListFragment: MoviesListFragment
    lateinit var progress_bar: ProgressBar
    lateinit var error_content: LinearLayout
    lateinit var retry_button: Button
    lateinit var gradient_banner: View

    private lateinit var repository: Repository

    companion object {
        fun newInstance(title: String, identificator: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("identificator", identificator)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = (requireActivity().application as MyApplication).repository

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragments()
        updateBunnerListener()
        setMovieInfo(view)

        retry_button.setOnClickListener {
            getContent()
        }
    }

    fun setFragments() {
        moviesListFragment = MoviesListFragment()

        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.movies_list_fragment, moviesListFragment);
        transaction.commit()
    }

    fun updateBunnerListener() {
        moviesListFragment.setOnContentSelectedListener {
            updateBanner(it)
        }
    }

    fun getContent () {
        arguments?.let {
            identificator = arguments?.getString("identificator").toString()

            lifecycleScope.launch {
                progress_bar.visibility = View.VISIBLE
                error_content.visibility = View.GONE
                repository.getHome(identificator)
            }
        }

    }

    fun getIdentification(): String {
        return identificator
    }

    fun setMovieInfo(view: View) {
          val headerLayout: ConstraintLayout = view.findViewById(R.id.layout_header)
          val infoLayout: ConstraintLayout = headerLayout.findViewById(R.id.layout_info)

          movie_cover = headerLayout.findViewById(R.id.movie_cover)
          movie_title = infoLayout.findViewById(R.id.movie_title)
          movie_genre = infoLayout.findViewById(R.id.movie_genre)
          movie_description = infoLayout.findViewById(R.id.movie_description)
          movie_additional_info= infoLayout.findViewById(R.id.movie_additional_info)

          progress_bar  = view.findViewById(R.id.progress_bar)
          error_content = view.findViewById(R.id.error_content)
          retry_button = view.findViewById(R.id.retry_button)
          gradient_banner = view.findViewById(R.id.gradient_horizontal)

          getContent()
          setMovies()
    }


    fun setMovies() {
        repository.movies.removeObservers(viewLifecycleOwner)

        repository.movies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Response.Success -> {
                    response.data?.let {
                        moviesListFragment.bindMoviesData(response.data)
                    }
                    progress_bar.visibility = View.GONE
                    error_content.visibility = View.GONE
                }
                is Response.Error -> {
                    progress_bar.visibility = View.GONE
                    error_content.visibility = View.VISIBLE
                    retry_button.requestFocus()
                }

                null -> {
                }
                else -> {
                }
            }
        })

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

    fun updateBanner(movie: MoviesResponse.Result.Detail) {
       movie.let {
           fadeOutView(gradient_banner)
           gradient_banner.visibility = View.GONE

           movie_title.text = movie.title
           movie_genre.text = movie.genre
           movie_description.text = movie.seasons?.description
           movie_additional_info.text = "Год: " + movie.year + " | " + "Сезонов: " + movie.seasons?.seasons?.size + " | " + "Страна: ${movie.country}"

           gradient_banner.visibility = View.VISIBLE

           fadeInView(gradient_banner)

           val url = movie.poster_url

           fadeOutImage(movie_cover)
           Glide.with(this).load(url).into(movie_cover)
           fadeInImage(movie_cover)
       }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        repository.movies.removeObservers(viewLifecycleOwner)
        identificator = ""
        progress_bar.visibility = View.GONE
        error_content.visibility = View.GONE
    }
    }
