package com.techsalman.tmdbmvvm.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techsalman.tmdbmvvm.model.TrendingMovieResponse
import com.techsalman.tmdbmvvm.remote.TmdbRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.techsalman.tmdbmvvm.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ListViewModel @Inject constructor(private val movieRepository: TmdbRepository) :
    ViewModel() {

    private val _movieList = MutableLiveData<Result<TrendingMovieResponse>>()
    val movieList = _movieList

    init {
        fetchMovies()
    }


    private fun fetchMovies() {
        viewModelScope.launch {
            movieRepository.fetchTrendingMovies().collect {
                _movieList.value = it
            }
        }
    }
}