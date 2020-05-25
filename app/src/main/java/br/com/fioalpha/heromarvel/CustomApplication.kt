package br.com.fioalpha.heromarvel

import android.app.Application
import br.com.fioalpha.heromarvel.core.imageManager.di.imageManagerModuleDI
import br.com.fioalpha.heromarvel.core.network.di.netWorkModuleDI
import br.com.fioalpha.heromarvel.core.utils.GeneratorHash
import br.com.fioalpha.heromarvel.data.di.databaseModuleDI
import br.com.fioalpha.heromarvel.domain.di.domainModuleDI
import br.com.fioalpha.heromarvel.presentation.detailcharacter.di.detailCharacterModuleDI
import br.com.fioalpha.heromarvel.presentation.favorite.di.favoriteModuleDI
import br.com.fioalpha.heromarvel.presentation.listcharacters.di.charactersModuleDI
import java.util.Date
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencies(this)
    }
}

internal fun initDependencies(application: Application) {
    startKoin {
        androidContext(application.applicationContext)
        androidLogger()
        fileProperties()
        modules(
            arrayListOf(
                netWorkModuleDI,
                imageManagerModuleDI,
                moduleGeneralDI,
                favoriteModuleDI,
                domainModuleDI,
                charactersModuleDI,
                databaseModuleDI,
                detailCharacterModuleDI
            )
        )
    }
}

internal val moduleGeneralDI = module {
    single {
        GeneratorHash(
            BuildConfig.API_PUBLIC_KEY,
            BuildConfig.API_PRIVATE_KEY,
            Date()
        )
    }
}
