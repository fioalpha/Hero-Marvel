package br.com.fioalpha.heromarvel.core.imageManager.di

import br.com.fioalpha.heromarvel.core.imageManager.ImageManagerImpl
import br.com.fioalpha.heromarvel.core.imageManager.ImageManger
import org.koin.dsl.module

val imageManagerModuleDI = module {
    single<ImageManger> {
        ImageManagerImpl(
//            get()
        )
    }
}