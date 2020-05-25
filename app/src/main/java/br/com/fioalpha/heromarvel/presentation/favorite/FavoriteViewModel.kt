package br.com.fioalpha.heromarvel.presentation.favorite

import br.com.fioalpha.heromarvel.core.utils.notOfType
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteAction
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteIntent
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteResult
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class FavoriteViewModel(
    private val processor: FavoriteProcessHolder
) : MviViewModel<FavoriteIntent, FavoriteState> {
    private val intentsSubject: PublishSubject<FavoriteIntent> =
        PublishSubject.create()

    private val statesObservable: Observable<FavoriteState> = compose()

    private val intentFilter: ObservableTransformer<FavoriteIntent, FavoriteIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<FavoriteIntent>(
                    shared.ofType(FavoriteIntent.LoaderIntent::class.java).take(1),
                    shared.notOfType(FavoriteIntent.LoaderIntent::class.java)
                )
            }
        }

    private fun compose(): Observable<FavoriteState> {
        return intentsSubject
            .compose<FavoriteIntent>(intentFilter)
            .map(this::actionFromIntent)
            .compose(processor.actionsProcessor)
            .map(this::handleStatus)
            .replay(1)
            .autoConnect(0)
    }

    private fun handleStatus(result: FavoriteResult): FavoriteState {
        return when (result) {
            FavoriteResult.FavoriteLoaderResult.Loader -> FavoriteState.Loading
            FavoriteResult.FavoriteLoaderResult.EmptyView -> FavoriteState.EmptyView
            is FavoriteResult.FavoriteLoaderResult.Success -> FavoriteState.Success(result.data)
            is FavoriteResult.FavoriteLoaderResult.Failure -> FavoriteState.Fail(result.error.message.orEmpty())
            is FavoriteResult.FavoriteDeleteResult.Success -> FavoriteState.Remove(result.position)
            is FavoriteResult.FavoriteDeleteResult.Failure -> FavoriteState.Fail(result.error.message.orEmpty())
            FavoriteResult.FavoriteDeleteResult.EMPTY -> FavoriteState.EmptyView
        }
    }

    private fun actionFromIntent(intent: FavoriteIntent): FavoriteAction {
        return when (intent) {
            FavoriteIntent.LoaderIntent -> FavoriteAction.Loader
            is FavoriteIntent.DeleteIntent -> FavoriteAction.Delete(
                intent.characterViewData,
                intent.position
            )
        }
    }

    override fun processIntents(intents: Observable<FavoriteIntent>): Disposable {
        return intents.subscribe(intentsSubject::onNext)
    }

    override fun states(): Observable<FavoriteState> = statesObservable
}
