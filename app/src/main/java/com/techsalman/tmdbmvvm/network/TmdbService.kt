package com.techsalman.tmdbmvvm.network

import com.techsalman.tmdbmvvm.model.MovieDesc
import com.techsalman.tmdbmvvm.model.TrendingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbService {

    @GET("/3/trending/movie/week")
    suspend fun getPopularMovies() : Response<TrendingMovieResponse>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int) : Response<MovieDesc>
}