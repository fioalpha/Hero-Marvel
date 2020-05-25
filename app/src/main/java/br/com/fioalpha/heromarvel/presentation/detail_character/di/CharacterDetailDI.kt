package br.com.fioalpha.heromarvel.presentation.detail_character.di

import br.com.fioalpha.heromarvel.core.utils.SchedulerRxImpl
import br.com.fioalpha.heromarvel.presentation.detail_character.DetailPresenter
import br.com.fioalpha.heromarvel.presentation.detail_character.DetailsView
import org.koin.dsl.module

val detailCharacterModuleDI = module {
    factory { (view: DetailsView) -> DetailPresenter(view, SchedulerRxImpl(), get()) }
}