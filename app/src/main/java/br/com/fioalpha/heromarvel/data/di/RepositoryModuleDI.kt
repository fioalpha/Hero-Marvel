package br.com.fioalpha.heromarvel.data.di

import androidx.room.Room
import br.com.fioalpha.heromarvel.core.room_local.AppDatabase
import br.com.fioalpha.heromarvel.data.Repository
import br.com.fioalpha.heromarvel.data.RepositoryImpl
import org.koin.dsl.module

val databaseModuleDI = module {
    single<Repository> {
        RepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            generator = get()
        )
    }
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()
    }

}