package com.emmanuel.cookey.marvelcomics.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmanuel.cookey.marvelcomics.R
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.util.Resource
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

    private val adapterComic = ComicListAdapter(mutableListOf()) { comic -> gotoDetailActivity(comic) }



    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        comicsRecyclerView.apply {
            adapter = adapterComic
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        lifecycleScope.launchWhenStarted{
            getAPI()
        }


        lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MainViewModel.Event.ShowErrorMessage  ->
                        mainLayout.snack((event.error.localizedMessage), Snackbar.LENGTH_LONG) {
                            action(getString(R.string.ok)) {
                            }
                        }

                }
            }
        }

    }

    suspend fun getAPI(){

        viewModel.comics.collect {

            val result = it ?: return@collect

            result.data?.let { adapterComic.setComics(it) }

            progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
            text_view_error.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
            text_view_error.text = result.error?.localizedMessage


        }
    }


    override fun getToolbarInstance(): Toolbar? {
        return toolbar
    }


    private fun gotoDetailActivity(comic: Comic) {

    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }



}