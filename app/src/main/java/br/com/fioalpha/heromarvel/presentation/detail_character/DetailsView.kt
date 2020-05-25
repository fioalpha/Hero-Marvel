package br.com.fioalpha.heromarvel.presentation.detail_character

import androidx.annotation.DrawableRes
import br.com.fioalpha.heromarvel.presentation.detail_character.model.CharacterDetailViewData

interface DetailsView {
    fun showData(character: CharacterDetailViewData)
    fun updateFavorite(@DrawableRes favorite: Int)
    fun showError(message: String)
    fun showWarning(message: String)
}