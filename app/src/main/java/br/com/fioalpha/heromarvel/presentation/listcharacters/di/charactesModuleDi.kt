package br.com.fioalpha.heromarvel.presentation.listcharacters.di

import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.CharactersViewModel
import org.koin.dsl.module

val charactersModuleDI = module {
    factory <CharactersViewModel> (override = true) {
        CharactersViewModel(get(), get(), get(), get())
    }
}
