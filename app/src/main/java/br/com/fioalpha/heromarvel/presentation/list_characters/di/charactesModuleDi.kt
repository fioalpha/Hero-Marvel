package br.com.fioalpha.heromarvel.presentation.list_characters.di

import br.com.fioalpha.heromarvel.presentation.list_characters.presentation.CharactersViewModel
import org.koin.dsl.module

val charactersModuleDI = module {
    factory {
        CharactersViewModel(get(), get(), get())
    }

}