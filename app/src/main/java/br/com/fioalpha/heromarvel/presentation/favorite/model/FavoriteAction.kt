package br.com.fioalpha.heromarvel.presentation.favorite.model

import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData

sealed class FavoriteAction:
    MviAction {
    data class Delete(val characterViewData: CharacterViewData, val position: Int): FavoriteAction()
    object Loader: FavoriteAction()
}