package ru.netology.multimedia.viewmodel

import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.multimedia.MainFragment
import ru.netology.multimedia.api.MediaApi
import ru.netology.multimedia.dto.Media
import ru.netology.multimedia.dto.Track

class MediaViewModel : ViewModel() {

    val _data = MutableLiveData<List<Track>>()
    val media = MutableLiveData<Media>()
    val data: LiveData<List<Track>>
        get() = _data

    fun loadMedia() {
        viewModelScope.launch {
            try {
                val response = MediaApi.retrofitService.getMedia()
                _data.value = response.tracks
                media.value = response
            } catch (e: Exception) {
            }
        }
    }

    fun onPause() {
        MainFragment.observer.onPause()
        _data.value?.forEach {
            it.playing = false
        }
    }

    fun setStateTracks(id: Long) {
        _data.value?.forEach {
            it.playing = it.id == id
            if (it.id != id) it.progress = 0
        }
    }

    fun playMediaplayer(track: Track, seekBar: SeekBar) {
        val url = MainFragment.BASE_URL + track.file
        MainFragment.observer.apply {
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(url)
        }.onPlay(seekBar)

    }
}