package br.com.fioalpha.heromarvel.domain.di

import br.com.fioalpha.heromarvel.domain.*
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCaseImpl
import org.koin.dsl.module

val domainModuleDI = module {
    factory<FetchCharactersUseCase> {
        FetchCharactersUseCaseImp(get())
    }
    factory<HandleFavoriteCharacterUseCase> {
        HandleFavoriteCharacterUseCaseImpl(get())
    }
    factory<IsCharacterFavoriteUseCase> {
        IsCharacterFavoriteUseCaseImpl(get())
    }
    factory<FetchAllFavoriteUseCase> {
        FetchAllFavoriteUseCaseImpl(get())
    }
    factory<SearchHeroUseCase> { SearchHeroUseCaseImpl(get()) }
}