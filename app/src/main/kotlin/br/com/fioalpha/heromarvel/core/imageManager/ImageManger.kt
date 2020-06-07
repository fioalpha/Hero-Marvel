package br.com.fioalpha.heromarvel.core.imageManager

import android.widget.ImageView

interface ImageManger {
    fun loaderImage(url: String, view: ImageView)
}