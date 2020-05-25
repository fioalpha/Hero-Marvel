package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

class IsCharacterFavoriteUseCaseImpl(
    private val repository: Repository
): IsCharacterFavoriteUseCase {

    private val characters = mutableListOf<CharacterDomain>()

    override fun setCharacters(characters: List<CharacterDomain>) = apply {
        this.characters.clear()
        this.characters.addAll(characters)
    }

    override fun execute(): Observable<List<CharacterDomain>> {
        return repository.fetchCharacterFavorite()
            .flatMap { findFavorite(characters, it) }
    }

    private fun findFavorite(character: List<CharacterDomain>, favorite: List<CharacterDomain>): Observable<List<CharacterDomain>> {
        return Observable.fromIterable(character)
            .map { findFavorite(it, favorite) }
            .toList()
            .toObservable()
    }

    private fun findFavorite(character: CharacterDomain, favorite: List<CharacterDomain>): CharacterDomain {
        val contain = favorite.map { it.id }.binarySearch (character.id)
        return if(contain < 0) character
            else character.copy(favorite = true)
    }

}