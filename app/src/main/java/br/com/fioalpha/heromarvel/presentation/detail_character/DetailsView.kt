package br.com.fioalpha.heromarvel.presentation.detail_character

import androidx.annotation.DrawableRes
import br.com.fioalpha.heromarvel.presentation.detail_character.model.CharacterDetailViewData

interface DetailsView {
    fun showData(character: CharacterDetailViewData)
    fun updateFavorite(character: CharacterDetailViewData)
    fun showError(message: String)
    fun hideError()
    fun showWarning(message: String)
}