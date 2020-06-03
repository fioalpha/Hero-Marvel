package br.com.fioalpha.heromarvel.presentation.favorite.model

import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData

sealed class FavoriteIntent:
    MviIntent {
    data class DeleteIntent(val characterViewData: CharacterViewData, val position: Int): FavoriteIntent()
    object LoaderIntent: FavoriteIntent()
}