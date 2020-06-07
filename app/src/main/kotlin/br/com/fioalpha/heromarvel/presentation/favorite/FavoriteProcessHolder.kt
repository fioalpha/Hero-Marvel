package br.com.fioalpha.heromarvel.presentation.favorite

import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteAction
import br.com.fioalpha.heromarvel.presentation.favorite.model.FavoriteResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class FavoriteProcessHolder(
    private val handleStatusFavoriteUseCase: HandleFavoriteCharacterUseCase,
    private val fetchAllFavoriteUseCase: FetchAllFavoriteUseCase
) {

    internal val actionsProcessor = ObservableTransformer<FavoriteAction, FavoriteResult> {
            actions ->
        actions.publish { shared ->
            createMergerObservable(shared)
                .mergeWith(createFilterObservable(shared))
        }
    }

    private val loaderFavoriteProcessor = ObservableTransformer<
            FavoriteAction.Loader, FavoriteResult.FavoriteLoaderResult>{
        actions -> actions.flatMap {
            fetchAllFavoriteUseCase.execute()
                .map (this::handleResult)
                .onErrorReturn { FavoriteResult.FavoriteLoaderResult.Failure(it) }
                .cast(FavoriteResult.FavoriteLoaderResult::class.java)
                .startWith(FavoriteResult.FavoriteLoaderResult.Loader)
        }
    }

    private fun handleResult(result: List<CharacterDomain>) =
        if (result.isEmpty()) FavoriteResult.FavoriteLoaderResult.EmptyView
        else FavoriteResult.FavoriteLoaderResult.Success(result.transform { it.transform() })

    private val deleteFavoriteProcessor = ObservableTransformer<
            FavoriteAction.Delete, FavoriteResult
        > {
        actions -> actions.flatMap { character ->
            handleStatusFavoriteUseCase.setCharacter(character.characterViewData.transform())
                .execute()
                .flatMap { fetchAllFavoriteUseCase.execute() }
                .map { result -> handleStatusFavorite(result, character) }
                .cast(FavoriteResult::class.java)
        }
    }

    private fun handleStatusFavorite(
        result: List<CharacterDomain>,
        character: FavoriteAction.Delete
    ): FavoriteResult.FavoriteDeleteResult {
        return if (result.isEmpty()) FavoriteResult.FavoriteDeleteResult.EMPTY
        else FavoriteResult.FavoriteDeleteResult.Success(character.position)
    }

    private fun createFilterObservable(shared: Observable<FavoriteAction>): Observable<FavoriteResult>? {
        return shared.filter {
            filter -> (filter !is FavoriteAction.Delete && filter !is FavoriteAction.Loader)
        }.flatMap {
            action -> Observable.error<FavoriteResult>(
                IllegalArgumentException("Unknown Action type: $action")
            )
        }
    }

    private fun createMergerObservable(shared: Observable<FavoriteAction>): Observable<FavoriteResult>{
        return Observable.merge<FavoriteResult>(
            shared.ofType(FavoriteAction.Loader::class.java).compose(loaderFavoriteProcessor),
            shared.ofType(FavoriteAction.Delete::class.java).compose(deleteFavoriteProcessor)
        )
    }

}