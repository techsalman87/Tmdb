package com.techsalman.tmdbmvvm.ui.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.techsalman.tmdbmvvm.R
import com.techsalman.tmdbmvvm.database.Movie
import com.techsalman.tmdbmvvm.model.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private val list = ArrayList<Movie>()
    private val viewModel by viewModels<ListViewModel>()
    private lateinit var tmdbAdapter: TmdbAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        init()
        subscribeUi()
    }

    private fun init() {
        title = "Trending Movies"
        val layoutManager = LinearLayoutManager(this)
        rvMovies.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            rvMovies.context,
            layoutManager.orientation
        )

        rvMovies.addItemDecoration(dividerItemDecoration)
        tmdbAdapter = TmdbAdapter(this, list)
        rvMovies.adapter = tmdbAdapter
    }

    private fun subscribeUi() {
        viewModel.movieList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.results?.let { list ->
                        tmdbAdapter.updateData(list)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }
}