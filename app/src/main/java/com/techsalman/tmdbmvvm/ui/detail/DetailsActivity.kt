package com.techsalman.tmdbmvvm.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.techsalman.tmdbmvvm.R
import com.techsalman.tmdbmvvm.model.MovieDesc
import dagger.hilt.android.AndroidEntryPoint
import com.techsalman.tmdbmvvm.model.Result
import com.techsalman.tmdbmvvm.utils.Config
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.loading
import kotlinx.android.synthetic.main.activity_list.vParent
import android.content.Intent
import android.net.Uri


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailsViewModel>()
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        btnBookTicket.setOnClickListener {

            val uri: Uri =
                Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.getIntExtra(EXTRAS_MOVIE_ID, 0)?.let { id ->
            viewModel.getMovieDetail(id)
            subscribeUi()
        } ?: showError("Unknown Movie")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun subscribeUi() {
        viewModel.movie.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        updateUi(it)
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

    private fun updateUi(movie: MovieDesc) {
        title = movie.title
        link = Config.BASE_URL_MOVIE_TICKET + movie.id
        tvTitle.text = movie.title
        tvDescription.text = movie.overview
        Glide.with(this).load(Config.IMAGE_URL + movie.poster_path)
            .apply(
                RequestOptions().override(400, 400).centerInside()
                    .placeholder(R.drawable.placehoder)
            ).into(ivCover)

        val genreNames = mutableListOf<String>()
        movie.genres.map {
            genreNames.add(it.name)
        }

    }

    companion object {
        const val EXTRAS_MOVIE_ID = "movie_id"
    }
}