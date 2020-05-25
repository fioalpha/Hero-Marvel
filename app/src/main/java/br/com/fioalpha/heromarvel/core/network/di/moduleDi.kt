package br.com.fioalpha.heromarvel.core.network.di

import br.com.fioalpha.heromarvel.core.network.RetrofitClient
import br.com.fioalpha.heromarvel.core.network.Service
import br.com.fioalpha.heromarvel.core.network.createConvertJson
import br.com.fioalpha.heromarvel.core.network.createHttpClient
import br.com.fioalpha.heromarvel.core.network.createHttpLogging
import org.koin.dsl.module

internal const val BASE_URL_LABEL = "base_url"

val netWorkModuleDI = module {
    single { createConvertJson() }
    single<Service>(override = true) {
        RetrofitClient(
            getProperty(BASE_URL_LABEL),
            createHttpClient(createHttpLogging()),
            Service::class.java,
            get()
        ).client()
    }
}
