package com.techsalman.tmdbmvvm.remote

import com.techsalman.tmdbmvvm.database.MovieDao
import com.techsalman.tmdbmvvm.model.MovieDesc
import com.techsalman.tmdbmvvm.model.TrendingMovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.techsalman.tmdbmvvm.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


class TmdbRepository @Inject constructor(
    private val movieRemoteDataSource: TmdbRemoteDataSource,
    private val movieDao: MovieDao
) {

    suspend fun fetchTrendingMovies(): Flow<Result<TrendingMovieResponse>?> {
        return flow {
            emit(fetchTrendingMoviesCached())
            emit(Result.loading())
            val result = movieRemoteDataSource.fetchTrendingMovies()

            //Cache to database if response is successful
            if (result.status == Result.Status.SUCCESS) {
                result.data?.results?.let { it ->
                    movieDao.deleteAll(it)
                    movieDao.insertAll(it)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchTrendingMoviesCached(): Result<TrendingMovieResponse>? =
        movieDao.getAll()?.let {
            Result.success(TrendingMovieResponse(it))
        }

    suspend fun fetchMovie(id: Int): Flow<Result<MovieDesc>> {
        return flow {
            emit(Result.loading())
            emit(movieRemoteDataSource.fetchMovie(id))
        }.flowOn(Dispatchers.IO)
    }
}