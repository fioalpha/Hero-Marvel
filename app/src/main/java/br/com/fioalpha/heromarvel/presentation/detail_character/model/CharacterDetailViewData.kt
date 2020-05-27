package br.com.fioalpha.heromarvel.presentation.detail_character.model

import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData

data class CharacterDetailViewData(
    val id: Long,
    val name: String,
    val descriptions: String,
    val imagePath: String,
    val favorite: Boolean,
    val iconFavorite: Int = R.drawable.un_favorite
) {

    companion object {

        fun transform(character: CharacterViewData, handleFavorite:(Boolean) -> Int) = CharacterDetailViewData (
            id = character.id,
            name = character.title,
            descriptions = character.description,
            imagePath = character.imagePath,
            favorite = character.favorite,
            iconFavorite = handleFavorite.invoke(character.favorite)
        )
    }

}