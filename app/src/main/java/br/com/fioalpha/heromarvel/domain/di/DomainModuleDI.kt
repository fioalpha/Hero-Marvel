package br.com.fioalpha.heromarvel.domain.di

import br.com.fioalpha.heromarvel.domain.*
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCaseImpl
import org.koin.dsl.module

val domainModuleDI = module {
    single<FetchCharactersUseCase> {
        FetchCharactersUseCaseImp(get())
    }
    single<HandleFavoriteCharacterUseCase> {
        HandleFavoriteCharacterUseCaseImpl(get())
    }
    single<IsCharacterFavoriteUseCase> {
        IsCharacterFavoriteUseCaseImpl(get())
    }
    single<FetchAllFavoriteUseCase> {
        FetchAllFavoriteUseCaseImpl(get())
    }
    single<SearchHeroUseCase> { SearchHeroUseCaseImpl(get()) }
}