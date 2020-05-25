package br.com.fioalpha.heromarvel.core.imageManager

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageManagerImpl: ImageManger {
    override fun loaderImage(url: String, view: ImageView) {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .into(view)
    }
}


