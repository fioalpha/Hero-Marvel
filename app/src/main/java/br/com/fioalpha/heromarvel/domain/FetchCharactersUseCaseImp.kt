package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

class FetchCharactersUseCaseImp(
    private val repository: Repository
): FetchCharactersUseCase {

    private var page: Int = 1

    override fun nextPage() {
       page ++
    }

    override fun execute(): Observable<List<CharacterDomain>> {
        return repository.fetchCharacter(page)
    }

}