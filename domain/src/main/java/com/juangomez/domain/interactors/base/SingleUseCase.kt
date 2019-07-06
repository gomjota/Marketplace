package com.juangomez.domain.interactors.base

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, in Params> constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseSingle(params: Params? = null): Single<T>

    open fun execute(observer: DisposableSingleObserver<T>, params: Params? = null) {
        val single = this.buildUseCaseSingle(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler) as Single<T>
        addDisposable(single.subscribeWith(observer))
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

}