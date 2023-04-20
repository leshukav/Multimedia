package ru.netology.multimedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.multimedia.R
import ru.netology.multimedia.databinding.MediaCardBinding
import ru.netology.multimedia.dto.Track

interface OnPlayListener {
    fun onPlay(track: Track, seekBar: SeekBar)
    fun onPause()
}

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

        fun bind(track: Track, onPlayListener: OnPlayListener) = with(cardBinding) {

            titleView.text = "Track: ${track.file}"
            fabPlay.isChecked = track.playing
            seekBar.max = track.progress
            fabPlay.setOnClickListener {
                if (fabPlay.isChecked) {
                    onPlayListener.onPlay(track, seekBar)
                } else {
                    onPlayListener.onPause()

                }
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.playing == newItem.playing && oldItem.progress == newItem.progress
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

