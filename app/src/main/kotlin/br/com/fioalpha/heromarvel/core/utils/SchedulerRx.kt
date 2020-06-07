package br.com.fioalpha.heromarvel.core.utils

import io.reactivex.Scheduler

interface SchedulerRx {
    fun ioSchedule(): Scheduler
    fun mainSchedule(): Scheduler
}