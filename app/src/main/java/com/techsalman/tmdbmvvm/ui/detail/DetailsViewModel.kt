package com.techsalman.tmdbmvvm.ui.detail

import androidx.lifecycle.*
import com.techsalman.tmdbmvvm.model.MovieDesc
import com.techsalman.tmdbmvvm.remote.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.techsalman.tmdbmvvm.model.Result
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(private val movieRepository: TmdbRepository) : ViewModel() {

    private var _id = MutableLiveData<Int>()
    private val _movie: LiveData<Result<MovieDesc>> = _id.distinctUntilChanged().switchMap {
        liveData {
            movieRepository.fetchMovie(it).onStart {
                emit(Result.loading())
            }.collect {
                emit(it)
            }
        }
    }
    val movie = _movie

    fun getMovieDetail(id: Int) {
        _id.value = id
    }
}