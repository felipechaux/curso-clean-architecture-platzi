package com.platzi.android.rickandmorty.presentation

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {
    // es importante reasignar los hilos para hacer pruebas, en prueba unitaria no se tiene acceso a hilo principal(mainThread)
    // asi que se sobrescribe para hacer simulacion
    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                // inicializar
                RxAndroidPlugins.reset()
                // trabajar pruebas reconfigurar
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }

                // para rx java
                RxJavaPlugins.reset()
                // todos los hilos
                RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }

                base.evaluate()
            }
        }

    companion object {
        private val SCHEDULER_INSTANCE = Schedulers.trampoline()
    }
}
