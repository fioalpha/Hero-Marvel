package br.com.fioalpha.heromarvel.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import br.com.fioalpha.heromarvel.R

class ErrorViewCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val view by lazy { LayoutInflater.from(context).inflate(
        R.layout.error_custom_view, this, false) }

    private val messageView: TextView by lazy { view.findViewById<TextView>(
        R.id.error_custom_message
    ) }

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(view)
        orientation = VERTICAL
    }

    fun setMessage(error: String) {
        messageView.text = error
    }
}
