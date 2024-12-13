package com.example.tvapp

import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.launch

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var retryButton: Button
    private lateinit var error_container: ConstraintLayout
    private lateinit var episode_source_list: List<String>
    private lateinit var movieTitle: TextView
    private var currentEpisodeIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        episode_source_list = intent.getStringArrayListExtra("EPISODE_SOURCE_LIST") ?: emptyList()
        currentEpisodeIndex = intent.getIntExtra("CURRENT_EPISODE", 0)
        movieTitle = findViewById(R.id.movie_title)

        player = SimpleExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.player_view)
        playerView.player = player
        error_container = findViewById(R.id.error_container)
        retryButton = error_container.findViewById(R.id.retry_button)


        if (episode_source_list.isNotEmpty()) {
            val episode_source_id = episode_source_list[currentEpisodeIndex]

            setupPlayerListener(episode_source_id)
            playEpisode(episode_source_id)
        }

    }

    private fun playEpisode(episode_source_id: String) {
        val request = (application as MyApplication).jsonRequest

        error_container.visibility = View.GONE
        error_container.findViewById<TextView>(R.id.text_error).text = ""

        lifecycleScope.launch {
            try {
                val result = request.getEpisode(episode_source_id)
                val body = result.body()

                body?.let {
                    movieTitle.text = it.showName + " - " + it.title
                    val mediaItem = MediaItem.fromUri(it.video.url)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.play()
                    playerView.keepScreenOn = true
                }
            } catch (error: Exception) {
                error_container.visibility = View.VISIBLE
                error_container.findViewById<TextView>(R.id.text_error).text = error.message
            }
        }

    }

    private fun setupPlayerListener(episodeUrl: String) {
        retryButton.setOnClickListener {
            retryPlayback(episodeUrl)
        }

        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                movieTitle.visibility = View.VISIBLE
            } else {
                movieTitle.visibility = View.GONE
            }
        }

        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error);
                handlePlayerError(error)
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    playNextEpisodeIfAvailable()
                }
            }

        })
    }

    private fun playNextEpisodeIfAvailable() {
        if (currentEpisodeIndex < episode_source_list.size - 1) {
            currentEpisodeIndex++
            playEpisode(episode_source_list[currentEpisodeIndex])
        } else {
            Toast.makeText(this, "Конец сезона", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handlePlayerError(error: PlaybackException) {
        val errorMessage = when (error.errorCode) {
            ExoPlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED  -> "Ошибка источника: ${error.message}"
            ExoPlaybackException.ERROR_CODE_DECODING_FAILED  -> "Ошибка рендерера: ${error.message}"
            ExoPlaybackException.TYPE_UNEXPECTED -> "Неожиданная ошибка: ${error.message}"
            else -> "Неизвестная ошибка: ${error.message}"
        }

        player.stop()

        error_container.visibility = View.VISIBLE
        playerView.visibility = View.GONE
        error_container.findViewById<TextView>(R.id.text_error).text = errorMessage
        retryButton.requestFocus()
    }

    private fun retryPlayback(videoUrl: String) {
        playerView.visibility = View.VISIBLE
        error_container.visibility= View.GONE
        player.setMediaItem(MediaItem.fromUri(videoUrl))
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}