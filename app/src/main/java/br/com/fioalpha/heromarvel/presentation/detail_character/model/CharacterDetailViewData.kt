package br.com.fioalpha.heromarvel.presentation.detail_character.model

import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData

data class CharacterDetailViewData(
    val id: Long,
    val name: String,
    val descriptions: String,
    val imagePath: String,
    val favorite: Boolean
) {

    companion object {

        fun transform(character: CharacterViewData) = CharacterDetailViewData (
            id = character.id,
            name = character.title,
            descriptions = character.description,
            imagePath = character.imagePath,
            favorite = character.favorite
        )
//
//        fun transform(character: CharacterViewData) = CharacterDomain (
//            id = character.id,
//            name = character.title,
//            description = character.description,
//            imagePath = character.imagePath,
//            favorite = character.favorite
//        )
    }

}