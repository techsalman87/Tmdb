package com.techsalman.tmdbmvvm.database

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie order by popularity DESC")
    fun getAll(): List<Movie>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Delete
    fun delete(movie: Movie)

    @Delete
    fun deleteAll(movie: List<Movie>)
}