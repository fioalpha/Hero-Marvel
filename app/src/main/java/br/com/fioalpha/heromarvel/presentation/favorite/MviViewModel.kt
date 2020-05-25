package br.com.fioalpha.heromarvel.presentation.favorite

import br.com.fioalpha.heromarvel.presentation.favorite.model.MviIntent
import br.com.fioalpha.heromarvel.presentation.favorite.model.MviViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun processIntents(intents: Observable<I>): Disposable
    fun states(): Observable<S>
}
