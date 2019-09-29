package com.juangomez.domain.interactors.base

import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params? = null): Either<Failure, Type>

    open operator fun invoke(
        scope: CoroutineScope,
        params: Params? = null,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }

    class None
}