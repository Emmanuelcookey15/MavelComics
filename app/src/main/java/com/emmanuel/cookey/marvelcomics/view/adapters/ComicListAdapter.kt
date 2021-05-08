package com.emmanuel.cookey.marvelcomics.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emmanuel.cookey.marvelcomics.R
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import kotlinx.android.synthetic.main.comic_item.view.*

class ComicListAdapter(private val comics: MutableList<Comic>,
                        private var listener: (Comic) -> Unit):
    RecyclerView.Adapter<ComicListAdapter.ComicHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comic_item, parent, false)
        return ComicHolder(view)
    }
    override fun getItemCount() = comics.size ?: 0

    override fun onBindViewHolder(holder: ComicHolder, position: Int) {
        holder.bind(comics[position], position)
    }

    fun setComics(comicList: List<Comic>) {
        this.comics.clear()
        this.comics.addAll(comicList)
        notifyDataSetChanged()
    }


    inner class ComicHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(comic: Comic, position: Int) = with(view) {
            text_view_title.text = comic.title
            view.setOnClickListener { listener(comics.get(position)) }

        }
    }
}