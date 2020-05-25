package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.domain.model.CharacterDomain
import io.reactivex.Observable

interface HandleFavoriteCharacterUseCase: UserCase<Observable<CharacterDomain>>{

    fun setCharacter(character: CharacterDomain?): HandleFavoriteCharacterUseCase

}

class HandleFavoriteCharacterUseCaseImpl(
    private val repository: Repository
): HandleFavoriteCharacterUseCase {

    private var character: CharacterDomain? = null

    override fun setCharacter(character: CharacterDomain?) = apply{
        this.character = character
    }

    override fun execute(): Observable<CharacterDomain> {
        return character?.let {
            if (it.favorite) repository.removeFavorite(it).blockingAwait()
            else repository.saveFavorite(it).blockingAwait()
            Observable.just(it.copy(favorite = it.favorite.not()))
        }?: Observable.error<CharacterDomain>(ExceptionInInitializerError())
    }

}