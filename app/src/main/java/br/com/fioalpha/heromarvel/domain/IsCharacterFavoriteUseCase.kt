package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

interface IsCharacterFavoriteUseCase: UserCase<Observable<List<CharacterDomain>>> {
    fun setCharacters(characters: List<CharacterDomain>): IsCharacterFavoriteUseCase
}