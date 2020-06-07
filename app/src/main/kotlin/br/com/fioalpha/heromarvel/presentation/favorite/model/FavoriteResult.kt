package br.com.fioalpha.heromarvel.presentation.favorite.model

import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData

sealed class FavoriteResult:
    MviResult {
    sealed class FavoriteLoaderResult: FavoriteResult() {
        object Loader: FavoriteLoaderResult()
        object EmptyView: FavoriteLoaderResult()
        data class Success(val data: List<CharacterViewData>): FavoriteLoaderResult()
        data class Failure(val error: Throwable) : FavoriteLoaderResult()
    }

    sealed class FavoriteDeleteResult: FavoriteResult() {
        object EMPTY: FavoriteDeleteResult()
        data class Success(val position: Int): FavoriteDeleteResult()
        data class Failure(val error: Throwable) : FavoriteDeleteResult()
    }
}