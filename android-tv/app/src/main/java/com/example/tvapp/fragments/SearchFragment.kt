package com.example.tvapp.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.MyApplication
import com.example.tvapp.R
import com.example.tvapp.models.Movie
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var progress_bar: ProgressBar
    lateinit var error_message: TextView
    lateinit var searchEditText: EditText

    lateinit var moviesGridFragment: MoviesGridFragment
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var savedSearchQuery: String? = null

    private lateinit var speechRecognitionLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechRecognitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = results?.get(0) ?: ""
                performSearch(spokenText)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesGridFragment = MoviesGridFragment()
        setFragments()
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        error_message = view.findViewById(R.id.error_message)
        progress_bar  = view.findViewById(R.id.progress_bar)

        searchEditText = view.findViewById(R.id.search_edit_text)


        searchEditText.setText(savedSearchQuery)

        searchEditText.requestFocus()


        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runnable?.let { handler.removeCallbacks(it) }

                runnable = Runnable {
                    performSearch(s.toString())
                }

                handler.postDelayed(runnable!!, 1000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun performSearch(query: String) {
        if (query.length > 2) {
            lifecycleScope.launch {
                try {
                    progress_bar.visibility = View.VISIBLE
                    error_message.text = ""
                    val jsonRequest = (requireActivity().application as MyApplication).jsonRequest

                    val response = jsonRequest.search("/shows/search/$query")

                    if (response.isSuccessful()) {
                        val body = response.body()
                        val movies: MutableList<Movie> = mutableListOf()

                        if (body != null && body.isNotEmpty()) {
                            for (item in body) {
                                movies.add(
                                    Movie(
                                        movieId = item.url,
                                        title = item.name,
                                        year = "",
                                        posterUrl = item.url,
                                        details = null,
                                        genre = "",
                                        country = ""
                                    )
                                )
                            }
                            setSearchResult(movies)
                        }else {
                            throw IllegalStateException("Не удалось ничего найти по запросу: $query")
                        }

                    }
                } catch (error: Exception) {
                    moviesGridFragment.clearMoviesData()
                    error_message.text = error.message
                }finally {
                    progress_bar.visibility = View.GONE
                }
            }
        }
    }

    private fun setFragments() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.movies_list_fragment, moviesGridFragment)
        transaction.commit()
    }


    private fun setSearchResult(movies: MutableList<Movie>) {
        moviesGridFragment.bindMoviesData(movies, 0, 0)
    }

    override fun onPause() {
        super.onPause()

        savedSearchQuery = searchEditText.text.toString()
    }
}