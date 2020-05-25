package br.com.fioalpha.heromarvel.core.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

fun Context?.showWarning(message: String) {
    this?.let { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun <T, U> List<T>.transform(actionTransformer: (T) -> U): List<U> {
    return this.map { actionTransformer(it) }
}

fun CharacterViewData.transform() = CharacterDomain(
    id = id,
    description = description,
    name = title,
    imagePath = imagePath,
    favorite = favorite
)


fun CharacterDomain.transform() = CharacterViewData(
    this.id,
    imagePath = this.imagePath,
    title = this.name,
    description = this.description,
    favorite = favorite
)

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
    checkNotNull(clazz) { "clazz is null" }
    return filter { !clazz.isInstance(it) }
}

