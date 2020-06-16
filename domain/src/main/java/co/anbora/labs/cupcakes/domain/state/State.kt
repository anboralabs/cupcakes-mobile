/*
 * MIT License
 *
 * Copyright (c) 2020 Shreyas Patil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package co.anbora.labs.cupcakes.domain.state


/**
 * State Management for UI & Data.
 */
sealed class State<out T> {

    fun <U> thenApply(transform: (value: T) -> U): State<U> {
        return when(this) {
            is Loading -> loading()
            is Success -> success(transform(this.data))
            is Error -> error(this.exception)
        }
    }

    suspend fun <U> thenApply(transform: suspend (value: T) -> U): State<U> {
        return when(this) {
            is Loading -> loading()
            is Success -> success(transform(this.data))
            is Error -> error(this.exception)
        }
    }

    class Loading<T> : State<T>()

    data class Success<T>(val data: T) : State<T>()

    data class Error<T>(@JvmField val exception: Throwable) : State<T>()

    private fun createFailure(exception: Throwable): Any =
        error<Throwable>(exception)

    companion object {

        /**
         * Returns [State.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [State.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T) =
            Success(data)

        /**
         * Returns [State.Error] instance.
         * @param exception Description of failure.
         */
        fun <T> error(exception: Throwable) =
            Error<T>(exception)
    }

}