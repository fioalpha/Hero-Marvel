package br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model

import br.com.fioalpha.heromarvel.presentation.favorite.model.MviViewState

sealed class CharacterViewStatus: MviViewState {
    object LOADING: CharacterViewStatus()
    object EMPTY: CharacterViewStatus()
    class Data(val data: List<CharacterViewData>): CharacterViewStatus()
    data class Error(val message: String): CharacterViewStatus()
}