package com.example.tvapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.tvapp.R
import com.example.tvapp.models.MoviesResponse


class DetailsFragment : Fragment() {
    lateinit var movie_title: TextView
    lateinit var movie_genre: TextView
    lateinit var movie_description: TextView
    lateinit var movie_additional_info: TextView

    lateinit var movie_cover: ImageView
    lateinit var back_button: Button
    lateinit var seriesListFragment: SeriesListFragment
    lateinit var identificator : String
     lateinit var movie_id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val headerLayout: ConstraintLayout = view.findViewById(R.id.layout_header)
        val infoLayout: ConstraintLayout = headerLayout.findViewById(R.id.layout_info)


        movie_cover = headerLayout.findViewById(R.id.movie_cover)
        movie_title = infoLayout.findViewById(R.id.movie_title)
        movie_genre = infoLayout.findViewById(R.id.movie_genre)
        movie_description = infoLayout.findViewById(R.id.movie_description)
        movie_additional_info = infoLayout.findViewById(R.id.movie_additional_info)
        back_button = headerLayout.findViewById(R.id.button_back)

        seriesListFragment = SeriesListFragment()


        arguments?.getParcelable<MoviesResponse.Result.Detail>("movie")?.let { movie ->
            bindDetailsData(movie)
            movie.seasons?.let { seasons ->
                identificator = arguments?.getString("identificator").toString()
                movie_id = movie.movie_id
                seriesListFragment.bindSeriesData(seasons)
                init()
            }
        }

        back_button.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun getIdentification(): String {
        return identificator
    }


    fun getMovieId(): String {
        return movie_id
    }

    private fun init() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.series_list_fragment, seriesListFragment)
        transaction.commit()
    }

    fun bindDetailsData(movie: MoviesResponse.Result.Detail) {
        movie_title.text = movie.title
        movie_genre.text = movie.genre
        movie_description.text = movie.seasons?.description
        movie_additional_info.text = "Год: " + movie.year + " | " + "Сезонов: " + movie.seasons?.seasons?.size + " | " + "Страна: " + movie.country

        val url = movie.poster_url

        Glide.with(this).load(url).into(movie_cover)
    }

}