package br.com.fioalpha.heromarvel.presentation.detailcharacter

import br.com.fioalpha.heromarvel.presentation.detailcharacter.model.CharacterDetailViewData

interface DetailsView {
    fun showData(character: CharacterDetailViewData)
    fun updateFavorite(character: CharacterDetailViewData)
    fun showError(message: String)
    fun hideError()
    fun showWarning(message: String)
}
