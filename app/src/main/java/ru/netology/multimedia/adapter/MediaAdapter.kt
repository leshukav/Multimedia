package ru.netology.multimedia.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.multimedia.MainActivity
import ru.netology.multimedia.MainActivity.Companion.observer
import ru.netology.multimedia.R
import ru.netology.multimedia.databinding.MediaCardBinding
import ru.netology.multimedia.dto.Track

interface OnPlayListener {
    fun onPlay()
    fun onPause()
    fun onPlayAfterPause()
}

private var currentPlayTrack = -1L
private var lastPlayTrack = -1L

class MediaAdapter(
    private val onPlayListener: OnPlayListener,
) : ListAdapter<Track, MediaAdapter.MediaHolder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_card, parent, false)
        return MediaHolder(view)
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bind(getItem(position), onPlayListener)
    }

    class MediaHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val cardBinding = MediaCardBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(track: Track, onPlayListener: OnPlayListener) = with(cardBinding) {

            titleView.text = "Track: ${track.file}"
            seekBar.max = 0
            fabPlay.setOnClickListener {
                currentPlayTrack = track.id
                if (fabPlay.isChecked) {
                    if (currentPlayTrack == lastPlayTrack) {
                        onPlayListener.onPlayAfterPause()
                        observer.mediaPlayer?.start()
                    } else {
                        lastPlayTrack = track.id
                        onPlayListener.onPlay()

                        if (observer.mediaPlayer?.isPlaying() == true || observer.isPaused() == true) {
                            observer.mediaPlayer?.apply {
                                stop()
                                reset()
                                setDataSource("${MainActivity.BASE_URL}${track.file}")
                                setOnPreparedListener {
                                    it.apply {
                                        start()
                                        initSeekBar(seekBar)
                                        setOnCompletionListener {
                                            it.isLooping = true
                                        }
                                    }
                                }
                            }
                            observer.mediaPlayer?.prepareAsync()
                        } else {
                            observer.mediaPlayer?.apply {
                                setDataSource("${MainActivity.BASE_URL}${track.file}")
                                setOnPreparedListener {
                                    it.apply {
                                        start()
                                        initSeekBar(seekBar)
                                        setOnCompletionListener {
                                            it.isLooping = true
                                        }
                                    }
                                }
                            }
                            observer.mediaPlayer?.prepareAsync()
                        }
                    }
                } else {
                    onPlayListener.onPause()
                    observer.mediaPlayer?.pause()
                }
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
}

private fun initSeekBar(seekBar: SeekBar) {
    seekBar.max = observer.mediaPlayer!!.duration
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(object : Runnable {
        override fun run() {
            try {
                seekBar.progress = observer.mediaPlayer!!.currentPosition
                handler.postDelayed(this, 1000)
            } catch (e: Exception) {
                seekBar.progress = 0
            }
        }

    }, 0)

    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {
            if (fromUser) observer.mediaPlayer?.seekTo(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}