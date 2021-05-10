package com.emmanuel.cookey.marvelcomics.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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
        loadViewModelData()
    }




    private fun initialzedRecyclerview(){

        val gridColumnCount = resources.getInteger(R.integer.grid_column_count)

        comicsRecyclerView.apply {
            adapter = adapterComic
            layoutManager = GridLayoutManager(this@MainActivity, gridColumnCount, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

//        var swipeDirs: Int = if (gridColumnCount > 4) {
//             0
//        } else {
//           ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        }
//        val helper = ItemTouchHelper(
//            object : ItemTouchHelper.SimpleCallback(
//                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//                ItemTouchHelper.LEFT
//            ) {
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: ViewHolder, target: ViewHolder
//                ): Boolean {
//                    val fromPos = viewHolder.adapterPosition
//                    val toPos = target.adapterPosition
//                    // move item in `fromPos` to `toPos` in adapter.
//                    return true // true if moved, false otherwise
//                }
//
//                override fun onSwiped(
//                    viewHolder: ViewHolder,
//                    direction: Int
//                ) {
//                    // remove from adapter
//                }
//            })




    }

    private fun loadViewModelData(){


        lifecycleScope.launchWhenStarted{
            viewModel.comics.collect {

                val result = it ?: return@collect

                result.data?.let { adapterComic.setComics(it) }

                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                text_view_error.isVisible = result is Resource.Error && result.data.isNullOrEmpty()

                if (text_view_error.isVisible){
                    when (result.error!!.localizedMessage){

                    }

                }
                text_view_error.text = result.error?.localizedMessage
            }
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



    private fun gotoDetailActivity(comic: Comic) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra("comic", comic)
        startActivity(intent)


    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }



}