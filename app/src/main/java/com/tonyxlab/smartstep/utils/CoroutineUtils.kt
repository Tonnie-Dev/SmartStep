package com.tonyxlab.smartstep.utils

import com.tonyxlab.smartstep.domain.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeIoCall(block: suspend () -> T): Resource<T> {
    return try {
        withContext(Dispatchers.IO) {
            Resource.Success(block())
        }
    } catch (e: Exception) {
        Resource.Error(e)
    }
}