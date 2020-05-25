package br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model

import br.com.fioalpha.heromarvel.presentation.favorite.model.MviViewState

sealed class CharacterViewStatus : MviViewState {
    object LOADING : CharacterViewStatus()
    object MOREITEMLOADING : CharacterViewStatus()
    object EMPTY : CharacterViewStatus()
    class Data(val data: List<CharacterViewData>) : CharacterViewStatus()
    class AddItem(val data: List<CharacterViewData>) : CharacterViewStatus()
    data class Error(val message: String) : CharacterViewStatus()
}
