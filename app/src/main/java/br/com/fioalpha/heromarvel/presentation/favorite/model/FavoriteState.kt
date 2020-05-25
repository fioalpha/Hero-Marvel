package br.com.fioalpha.heromarvel.presentation.favorite.model

import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData

sealed class FavoriteState : MviViewState {
    object Loading : FavoriteState()
    object EmptyView : FavoriteState()
    data class Success(val data: List<CharacterViewData>) : FavoriteState()
    data class Fail(val message: String) : FavoriteState()
    data class Remove(val position: Int) : FavoriteState()
}
