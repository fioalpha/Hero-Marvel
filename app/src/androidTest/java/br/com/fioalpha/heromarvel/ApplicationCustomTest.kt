package br.com.fioalpha.heromarvel

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

class ApplicationCustomTest: CustomApplication() {

    internal fun loadModules(module: Module, block: () -> Unit)  {
        loadKoinModules(module)
        block()
        unloadKoinModules(module)
    }

}

class MyRunnerTest: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ApplicationCustomTest::class.java.name, context)
    }

}