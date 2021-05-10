package com.emmanuel.cookey.marvelcomics.view.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.emmanuel.cookey.marvelcomics.R
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.toolbar_view_custom_layout.*


class DetailsActivity : BaseActivity() {


    private val toolbar: Toolbar by lazy { toolbar_toolbar_view as Toolbar }

    override fun getToolbarInstance(): Toolbar? {
        toolbar.visibility = View.GONE
        return toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val comicDataFromMain = intent.getParcelableExtra<Comic>("comic")

        setUpViews(comicDataFromMain)

    }




    fun setUpViews(comic: Comic?){
        val radius = 20f
        val decorView: View = window.decorView
        val windowBackground: Drawable = decorView.getBackground()
        blurView.setupWith(decorView.findViewById(android.R.id.content))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(false)


        if (comic != null) {
            detail_comic_title.text = comic.getComicTitle()

            detail_comic_description.text = HtmlCompat.fromHtml(comic.getComicDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()



            Glide.with(this)
                .asBitmap()
                .load(comic.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap?>() {

                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val bitmapDrawable = BitmapDrawable(this@DetailsActivity.resources, resource)
                        detailLayout.background = bitmapDrawable
                    }
                })

        }

    }
}