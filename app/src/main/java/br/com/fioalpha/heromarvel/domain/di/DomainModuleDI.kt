package br.com.fioalpha.heromarvel.domain.di

import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.FetchAllFavoriteUseCaseImpl
import br.com.fioalpha.heromarvel.domain.FetchCharactersUseCase
import br.com.fioalpha.heromarvel.domain.FetchCharactersUseCaseImp
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCase
import br.com.fioalpha.heromarvel.domain.HandleFavoriteCharacterUseCaseImpl
import br.com.fioalpha.heromarvel.domain.IsCharacterFavoriteUseCase
import br.com.fioalpha.heromarvel.domain.IsCharacterFavoriteUseCaseImpl
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCase
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCaseImpl
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
