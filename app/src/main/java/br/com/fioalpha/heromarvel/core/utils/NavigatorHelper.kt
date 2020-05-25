package br.com.fioalpha.heromarvel.core.utils

import android.os.Bundle
import androidx.navigation.findNavController
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.presentation.favorite.FavoriteFragment
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.ListCharacterFragment

const val CHARACTER_ID_EXTRA = "character"


fun ListCharacterFragment.goToDetailsCharacter(characterViewData: CharacterViewData) {
    view?.findNavController()?.navigate(
        R.id.listCharactersAction,
        handleBundle { putParcelable(CHARACTER_ID_EXTRA, characterViewData) }
    )
}

fun FavoriteFragment.goToDetailsCharacter(characterViewData: CharacterViewData) {
    view?.findNavController()?.navigate(
        R.id.favoriteAction,
        handleBundle { putParcelable(CHARACTER_ID_EXTRA, characterViewData) }
    )
}


fun handleBundle(block: Bundle.() -> Unit) = Bundle().apply {
    block()
}

