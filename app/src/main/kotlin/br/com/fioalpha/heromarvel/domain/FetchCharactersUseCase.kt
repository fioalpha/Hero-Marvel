package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

interface FetchCharactersUseCase: UserCase<Observable<List<CharacterDomain>>> {
    fun page(page:Int): FetchCharactersUseCase
}

interface UserCase<T> {
    fun execute(): T
}

