package com.juangomez.presentation.util

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.minDelay(
    time: Long,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    scheduler: Scheduler = Schedulers.computation()
): Single<T> {
    val timeStart = scheduler.now(TimeUnit.MILLISECONDS)
    val delayInMillis = TimeUnit.MILLISECONDS.convert(time, unit)
    return Single.zip(
        Single.timer(time, unit, scheduler),
        this.onErrorResumeNext { error: Throwable ->
            val afterError = scheduler.now(TimeUnit.MILLISECONDS)
            val millisPassed = afterError - timeStart

            val needWaitDelay = delayInMillis - millisPassed
            if (needWaitDelay > 0)
                Single.error<T>(error)
                    .delay(needWaitDelay, TimeUnit.MILLISECONDS, scheduler, true)
            else
                Single.error<T>(error)
        },
        BiFunction { _, t2 -> t2 }
    )
}