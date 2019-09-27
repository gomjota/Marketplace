package com.juangomez.presentation.runner

import io.reactivex.plugins.RxJavaPlugins
import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx2.idler.Rx2Idler


class TestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(
            Rx2Idler.create("RxJava 2.x Computation Scheduler")
        )
        RxJavaPlugins.setInitIoSchedulerHandler(
            Rx2Idler.create("RxJava 2.x IO Scheduler")
        )
        RxJavaPlugins.setInitNewThreadSchedulerHandler(
            Rx2Idler.create("RxJava 2.x Thread Scheduler")
        )
        super.onStart()
    }
}