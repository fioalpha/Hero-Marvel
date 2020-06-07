package br.com.fioalpha.heromarvel.presentation.favorite.di

import br.com.fioalpha.heromarvel.presentation.favorite.FavoriteProcessHolder
import br.com.fioalpha.heromarvel.presentation.favorite.FavoriteViewModel
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCase
import br.com.fioalpha.heromarvel.domain.SearchHeroUseCaseImpl
import org.koin.dsl.module

val favoriteModuleDI = module {
    factory {
        FavoriteViewModel(
            FavoriteProcessHolder(
                get(),
                get()
            )
        )
    }
}