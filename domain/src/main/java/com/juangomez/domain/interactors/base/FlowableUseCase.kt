package com.juangomez.domain.interactors.base

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

abstract class FlowableUseCase<T, in Params> constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread) {

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseFlowable(params: Params? = null): Flowable<T>

    open fun execute(observer: DisposableSubscriber<T>, params: Params? = null) {
        val flowable = this.buildUseCaseFlowable(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler) as Flowable<T>
        addDisposable(flowable.subscribeWith(observer))
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

}