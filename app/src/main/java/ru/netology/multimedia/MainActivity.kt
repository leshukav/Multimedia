package ru.netology.multimedia


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.multimedia.adapter.MediaAdapter
import ru.netology.multimedia.adapter.OnPlayListener
import ru.netology.multimedia.api.MediaApi
import ru.netology.multimedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL =
            "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"
        val observer = MediaLifecycleObserver()
    }

    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MediaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)

        binding.mediaList.layoutManager = LinearLayoutManager(this)
        adapter = MediaAdapter(object : OnPlayListener {

            override fun onPlay() {}

            override fun onPause() {}

            override fun onPlayAfterPause() {}
        })

        binding.mediaList.adapter = adapter

        binding.getMedia.setOnClickListener {
            binding.progress.isVisible = true
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val media = MediaApi.retrofitService.getMedia()
                    runOnUiThread {
                        with(binding) {
                            titleView.text = media.title
                            authorView.text = media.artist
                            cardView.isVisible = true
                        }
                        binding.getMedia.isVisible = false
                        binding.progress.isVisible = false
                        adapter.submitList(media.tracks)

                    }
                } catch (e: Exception) {
                    binding.progress.isVisible = false
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Media list not found", Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        }

    }
}