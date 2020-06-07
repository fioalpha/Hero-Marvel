package br.com.fioalpha.heromarvel.domain

import br.com.fioalpha.heromarvel.data.Repository

class FetchAllFavoriteUseCaseImpl(
    private val repository: Repository
): FetchAllFavoriteUseCase {

    override fun execute() = repository.fetchCharacterFavorite()

}