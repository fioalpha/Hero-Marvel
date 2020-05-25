package br.com.fioalpha.heromarvel.data

import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    fun fetchCharacter(page: Int): Observable<List<CharacterDomain>>
    fun fetchCharacterFavorite(): Observable<List<CharacterDomain>>
    fun removeFavorite(character: CharacterDomain): Completable
    fun saveFavorite(character: CharacterDomain): Completable
    fun characterIsFavorite(character: CharacterDomain): Single<Boolean>
}