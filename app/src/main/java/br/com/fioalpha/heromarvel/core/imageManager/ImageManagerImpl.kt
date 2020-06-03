package br.com.fioalpha.heromarvel.core.imageManager

import android.widget.ImageView
import br.com.fioalpha.heromarvel.R
import com.squareup.picasso.Picasso

class ImageManagerImpl: ImageManger {
    override fun loaderImage(url: String, view: ImageView) {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.marvel)
            .into(view)
    }
}


