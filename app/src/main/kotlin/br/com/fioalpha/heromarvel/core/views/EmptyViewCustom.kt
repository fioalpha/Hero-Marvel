package br.com.fioalpha.heromarvel.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.fioalpha.heromarvel.R

class EmptyViewCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val view by lazy { LayoutInflater.from(context).inflate(
        R.layout.empty_custom_view, this, false) }

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(view)
        orientation = VERTICAL
    }

}