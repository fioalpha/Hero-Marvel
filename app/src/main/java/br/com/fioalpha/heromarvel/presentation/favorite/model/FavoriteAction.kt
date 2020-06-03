package br.com.fioalpha.heromarvel.presentation.favorite.model

import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData

sealed class FavoriteAction:
    MviAction {
    data class Delete(val characterViewData: CharacterViewData, val position: Int): FavoriteAction()
    object Loader: FavoriteAction()
}