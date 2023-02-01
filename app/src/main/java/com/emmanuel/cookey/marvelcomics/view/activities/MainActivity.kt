package com.emmanuel.cookey.marvelcomics.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.emmanuel.cookey.marvelcomics.R
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.Resource
import com.emmanuel.cookey.marvelcomics.util.action
import com.emmanuel.cookey.marvelcomics.util.snack
import com.emmanuel.cookey.marvelcomics.view.adapters.ComicListAdapter
import com.emmanuel.cookey.marvelcomics.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_view_custom_layout.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val toolbar: Toolbar by lazy { toolbar_toolbar_view as Toolbar }

    override fun getToolbarInstance(): Toolbar? {
        toolbar.visibility = GONE
        return toolbar
    }

    private val adapterComic = ComicListAdapter(mutableListOf()) { comic -> gotoDetailActivity(comic) }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialzedRecyclerview()
        viewModel.fetchComics()
        loadViewModelData()
    }




    private fun initialzedRecyclerview(){

        val gridColumnCount = resources.getInteger(R.integer.grid_column_count)

        comicsRecyclerView.apply {
            adapter = adapterComic
            layoutManager = GridLayoutManager(this@MainActivity, gridColumnCount, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

    }

    private fun loadViewModelData(){

        viewModel.insertComics.observe(this, Observer {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    progressBar.isVisible = false
                    text_view_error.isVisible = false
                    it.data?.let { comics -> viewModel.insertComicsToDB(comics) }
                }
                Resource.Status.ERROR -> {
                    progressBar.isVisible = false
                    text_view_error.isVisible = true
                    text_view_error.text = it.error
                    mainLayout.snack((it.error?:"Error"), Snackbar.LENGTH_LONG) {
                        action(getString(R.string.ok)) {
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    progressBar.isVisible = true
                    text_view_error.isVisible = false
                }
            }

        })


        viewModel.comics.observe(this, Observer {
            it?.let { adapterComic.setComics(it) }
        })
    }



    private fun gotoDetailActivity(comic: Comic) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra("comic", comic)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
    }

}