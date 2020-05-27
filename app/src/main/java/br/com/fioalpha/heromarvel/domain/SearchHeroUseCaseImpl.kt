package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

class SearchHeroUseCaseImpl(
    private val repository: Repository
): SearchHeroUseCase {

    private lateinit var term: String

    override fun setTerm(term: String) = apply {
        this.term = term
    }

    override fun execute(): Observable<List<CharacterDomain>> {
        return if(term.isEmpty()) repository.fetchCharacter(1)
        else repository.characterByTerm(term)
    }

}