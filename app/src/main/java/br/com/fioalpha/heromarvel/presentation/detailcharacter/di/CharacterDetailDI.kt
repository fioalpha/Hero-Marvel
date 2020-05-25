package br.com.fioalpha.heromarvel.presentation.detailcharacter.di

import br.com.fioalpha.heromarvel.core.utils.SchedulerRxImpl
import br.com.fioalpha.heromarvel.presentation.detailcharacter.DetailPresenter
import br.com.fioalpha.heromarvel.presentation.detailcharacter.DetailsView
import org.koin.dsl.module

val detailCharacterModuleDI = module {
    factory { (view: DetailsView) -> DetailPresenter(view, SchedulerRxImpl(), get()) }
}
