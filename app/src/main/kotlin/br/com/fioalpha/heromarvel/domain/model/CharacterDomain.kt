package br.com.fioalpha.heromarvel.domain.model

import android.os.Parcelable
import br.com.fioalpha.heromarvel.presentation.detailcharacter.model.CharacterDetailViewData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDomain(
    val id: Long,
    val description: String,
    val imagePath: String,
    val name: String,
    val favorite: Boolean = false
): Parcelable {

    companion object {
        fun transform(characterDetail: CharacterDetailViewData) =
            CharacterDomain(
                id = characterDetail.id,
                name = characterDetail.name,
                description = characterDetail.descriptions,
                imagePath = characterDetail.imagePath,
                favorite = characterDetail.favorite
            )
    }



}