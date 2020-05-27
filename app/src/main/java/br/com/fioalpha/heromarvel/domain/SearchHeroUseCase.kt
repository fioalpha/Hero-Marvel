package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

interface SearchHeroUseCase:
    UserCase<Observable<List<CharacterDomain>>> {
    fun setTerm(term: String): SearchHeroUseCase
}