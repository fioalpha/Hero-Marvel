package br.com.fioalpha.heromarvel.core.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerRxImpl:
    SchedulerRx {

    override fun ioSchedule(): Scheduler =
        Schedulers.io()

    override fun mainSchedule(): Scheduler =
        AndroidSchedulers.mainThread()

}