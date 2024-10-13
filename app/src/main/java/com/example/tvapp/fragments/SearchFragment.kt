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
import com.example.tvapp.api.Response
import com.example.tvapp.api.Repository
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var progress_bar: ProgressBar
    lateinit var error_message: TextView
    lateinit var searchEditText: EditText
    private lateinit var repository: Repository
    private lateinit var moviesListFragment: MoviesListFragment
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val REQUEST_CODE_MICROPHONE = 100
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as MyApplication).repository

        error_message = view.findViewById(R.id.error_message)
        progress_bar  = view.findViewById(R.id.progress_bar)

        moviesListFragment = MoviesListFragment()

        searchEditText = view.findViewById(R.id.search_edit_text)
        val searchButton: Button = view.findViewById(R.id.search_button)

        searchEditText.setText(savedSearchQuery)

        searchEditText.requestFocus()

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            performSearch(query)
        }

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

        view.findViewById<Button>(R.id.voice_search_button).setOnClickListener {
            checkMicrophonePermission()
        }

        setFragments()
        setSearchResult()
    }

    private fun performSearch(query: String) {
        if (query.length > 2) {
            lifecycleScope.launch {
                progress_bar.visibility = View.VISIBLE
                error_message.text = ""
                repository.searchMovies(query)
            }
        }
    }

    private fun setFragments() {
        moviesListFragment = MoviesListFragment()

        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.movies_list_fragment, moviesListFragment)
        transaction.commit()
    }

    private fun checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_MICROPHONE
            )
        } else {
            startVoiceRecognition()
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите сейчас")
        }
        speechRecognitionLauncher.launch(intent)
    }

    private fun setSearchResult() {
        repository.searchResult.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data != null) {
                        moviesListFragment.bindMoviesData(response.data)

                        if (response.data.result.isEmpty()) {
                            error_message.text = "Ничего не удалось найти :("
                        }
                    }
                    progress_bar.visibility = View.GONE
                }
                is Response.Error -> {
                    progress_bar.visibility = View.GONE
                    error_message.text = "Сервис временно недоступен"
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        savedSearchQuery = searchEditText.text.toString()
    }
}