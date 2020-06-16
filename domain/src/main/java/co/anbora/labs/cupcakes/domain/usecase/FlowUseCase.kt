package co.anbora.labs.cupcakes.domain.usecase

import co.anbora.labs.cupcakes.domain.state.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [State.Error] to the result) is the subclasses's responsibility.
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    @ExperimentalCoroutinesApi
    operator fun invoke(parameters: P): Flow<State<R>> {
        return execute(parameters)
            .catch { e -> emit(State.error(e)) }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: P): Flow<State<R>>
}