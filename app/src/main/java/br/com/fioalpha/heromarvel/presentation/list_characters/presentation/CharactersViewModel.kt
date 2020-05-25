package br.com.fioalpha.heromarvel.presentation.list_characters.presentation

import br.com.fioalpha.heromarvel.core.utils.transform
import br.com.fioalpha.heromarvel.domain.FetchCharactersUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.IsCharacterFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewData
import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.model.CharacterViewStatus
import io.reactivex.Observable

class CharactersViewModel(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val handleFavoriteCharacterUseCase: HandleFavoriteCharacterUseCase,
    private val isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase
) {

    fun loadData(): Observable<CharacterViewStatus> {
        return fetchCharactersUseCase.execute()
            .flatMap { isCharacterFavoriteUseCase.setCharacters(it).execute() }
            .map (::transform)
            .map (::handleQuantityItem)
            .onErrorReturn(::handleError)
            .cast(CharacterViewStatus::class.java)
            .startWith(CharacterViewStatus.LOADING)
    }

    fun handleFavorite(characterViewData: CharacterViewData, position: Int): Observable<Pair<CharacterDomain, Int>> {
        return handleFavoriteCharacterUseCase.setCharacter(characterViewData.transform())
            .execute()
            .onErrorReturn { characterViewData.transform() }
            .map { Pair(it, position) }
    }

    private fun handleQuantityItem(items: List<CharacterViewData>): CharacterViewStatus {
        return if (items.isEmpty()) CharacterViewStatus.EMPTY
               else CharacterViewStatus.Data(items)
    }

    private fun transform(items: List<CharacterDomain>): List<CharacterViewData> {
        return items.transform { it.transform() }
    }

    private fun handleError(error: Throwable) = CharacterViewStatus.Error(error.message.orEmpty())

}