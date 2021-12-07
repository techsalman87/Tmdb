package com.techsalman.tmdbmvvm.remote

import com.techsalman.tmdbmvvm.model.MovieDesc
import com.techsalman.tmdbmvvm.model.TrendingMovieResponse
import com.techsalman.tmdbmvvm.network.TmdbService
import com.techsalman.tmdbmvvm.utils.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import com.techsalman.tmdbmvvm.model.Result
import com.techsalman.tmdbmvvm.model.Error
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


class TmdbRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchTrendingMovies(): Result<TrendingMovieResponse> {
        val movieService = retrofit.create(TmdbService::class.java);
        return getResponse(
            request = { movieService.getPopularMovies() },
            defaultErrorMessage = "Error fetching Movie list")

    }

    suspend fun fetchMovie(id: Int): Result<MovieDesc> {
        val movieService = retrofit.create(TmdbService::class.java);
        return getResponse(
            request = { movieService.getMovie(id) },
            defaultErrorMessage = "Error fetching Movie Description")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}