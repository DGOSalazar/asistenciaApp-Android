package com.example.myapplication.core.utils.statusNetwork

import com.example.myapplication.R
import com.example.myapplication.core.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> makeCall(
    call: suspend () -> T
): ResponseStatus<T> = withContext(Dispatchers.IO) {
    try {
        ResponseStatus.Success(call())
    } catch (e: Throwable) {
        ResponseStatus.Error(R.string.throwable_exception)
    } catch (e: Exception) {
        ResponseStatus.Error(R.string.exception)
    }
}

suspend fun <T> makeCall2(
    call: T
): Resource2<T> = withContext(Dispatchers.IO) {
    try {
        Resource2.success(call)
    } catch (e: Throwable) {
        Resource2.error(R.string.throwable_exception)
    } catch (e: Exception) {
        Resource2.error(R.string.exception)
    }
}

class Resource2<T>(
    val status: Status,
    val data: T? = null,
    val message: Int? = null){
    companion object {
        fun <T> success(data: T?): Resource2<T> {
            return Resource2(Status.SUCCESS, data)
        }
        fun <T> error(msg: Int? = null): Resource2<T> {
            return Resource2(Status.ERROR, message = msg)
        }
    }
}